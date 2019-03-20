package io.alank.tradematching.domain.matching


import io.alank.tradematching.domain.account.AccountGroup
import io.alank.tradematching.domain.trade.*

data class InstrumentMatching(val accountGroup: AccountGroup,
                              val ticker: String,
                              val buyTrades: List<Trade> = listOf(),
                              val sellTrades: List<Trade> = listOf(),
                              val results: List<MatchingResult> = listOf()) {

    fun handle(tradeEvent: TradeEvent): InstrumentMatching = when (tradeEvent) {
        is EndOfTradeStreamEvent -> copy(
                buyTrades = listOf(),
                sellTrades = listOf(),
                results = (buyTrades + sellTrades).map { UnmatchedResult(it, tradeEvent.marketPrice, accountGroup) })
        is BuySellTradeEvent -> tradeEvent.trade.run {
            if (this.way.opposite().trades().isEmpty())
                appendSameWayTrade(this)
            else
                matchWithOppositeWayTrades(this)
        }
        else -> throw UnsupportedOperationException()
    }

    private fun matchWithOppositeWayTrades(trade: Trade): InstrumentMatching {
        val matchingProgressAfterConsumingOppositeTrades = trade.way.opposite().trades()
                .fold(MatchInProgress(trade.quantity)) { mip, oppTrade ->
                    when {
                        // loop opp-way trades and accumulate intermediate results and remaining data
                        mip.remainingQuantity == 0L ->
                            mip.copy(remainingOppTrades = mip.remainingOppTrades + oppTrade)
                        mip.remainingQuantity >= oppTrade.quantity ->
                            mip.copy(
                                    remainingQuantity = mip.remainingQuantity - oppTrade.quantity,
                                    results = mip.results + createMatchedResult(trade, oppTrade, oppTrade.quantity))
                        else ->
                            mip.copy(
                                    remainingQuantity = 0,
                                    remainingOppTrades = listOf(oppTrade.copy(quantity = oppTrade.quantity - mip.remainingQuantity)),
                                    results = mip.results + createMatchedResult(trade, oppTrade, mip.remainingQuantity))
                    }
                }
        // after consuming all opp-way trades, the concerning trade might still have quantity left
        val remainingTrades = if (matchingProgressAfterConsumingOppositeTrades.remainingQuantity > 0)
            listOf(trade.copy(quantity = matchingProgressAfterConsumingOppositeTrades.remainingQuantity))
        else
            listOf()

        return if (trade.way == Way.B)
            this.copy(buyTrades = remainingTrades,
                    sellTrades = matchingProgressAfterConsumingOppositeTrades.remainingOppTrades,
                    results = matchingProgressAfterConsumingOppositeTrades.results)
        else
            this.copy(buyTrades = matchingProgressAfterConsumingOppositeTrades.remainingOppTrades,
                    sellTrades = remainingTrades,
                    results = matchingProgressAfterConsumingOppositeTrades.results)
    }

    private fun appendSameWayTrade(trade: Trade) =
            if (trade.way == Way.B)
                copy(buyTrades = (buyTrades + trade), results = listOf())
            else
                copy(sellTrades = (sellTrades + trade), results = listOf())

    private fun createMatchedResult(trade1: Trade,
                                    trade2: Trade,
                                    quantity: Long): MatchedResult =
            if (trade1.way == Way.B)
                MatchedResult(trade1.id!!, trade1.price, trade2.id!!, trade2.price, quantity, accountGroup)
            else
                MatchedResult(trade2.id!!, trade2.price, trade1.id!!, trade1.price, quantity, accountGroup)

    private fun Way.opposite() = if (Way.B == this) Way.S else Way.B
    private fun Way.trades() = if (Way.B == this) buyTrades else sellTrades

    private data class MatchInProgress(val remainingQuantity: Long,
                                       val remainingOppTrades: List<Trade> = listOf(),
                                       val results: List<MatchingResult> = listOf())
}
