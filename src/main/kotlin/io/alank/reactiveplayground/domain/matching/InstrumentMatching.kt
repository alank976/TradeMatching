package io.alank.reactiveplayground.domain.matching


import io.alank.reactiveplayground.domain.trade.*

data class InstrumentMatching(val ticker: String,
                              val buyTrades: List<Trade> = listOf(),
                              val sellTrades: List<Trade> = listOf(),
                              val results: List<MatchingResult> = listOf()) {

    fun handle(tradeEvent: TradeEvent): InstrumentMatching = when (tradeEvent) {
        is EndOfTradeStreamEvent -> copy(buyTrades = listOf(),
                sellTrades = listOf(),
                results = (buyTrades + sellTrades).map { UnmatchedResult(it, tradeEvent.marketPrice) })
        is BuySellTradeEvent -> {
            val trade = tradeEvent.trade
            if (trade.way.opposite().trades().isEmpty()) {
                withTrade(trade)
            } else {
                val finalMatchInProgress = trade.way.opposite().trades()
                        .fold(MatchInProgress(trade.quantity)) { mip, oppTrade ->
                            when {
                                mip.remainingQuantity == 0L ->
                                    mip.copy(remainingOppTrades = mip.remainingOppTrades + oppTrade)
                                mip.remainingQuantity >= oppTrade.quantity ->
                                    mip.copy(remainingQuantity = mip.remainingQuantity - oppTrade.quantity,
                                            results = mip.results + matchCompletely(trade, oppTrade, oppTrade.quantity))
                                else ->
                                    mip.copy(remainingQuantity = 0,
                                            remainingOppTrades = mip.remainingOppTrades + oppTrade.copy(quantity = oppTrade.quantity - mip.remainingQuantity),
                                            results = mip.results + matchCompletely(trade, oppTrade, mip.remainingQuantity))
                            }
                        }
                val remainingSameWayTrades = if (finalMatchInProgress.remainingQuantity > 0) {
                    listOf(trade.copy(quantity = finalMatchInProgress.remainingQuantity))
                } else listOf()

                if (trade.way == Way.B) {
                    this.copy(buyTrades = remainingSameWayTrades,
                            sellTrades = finalMatchInProgress.remainingOppTrades,
                            results = finalMatchInProgress.results)
                } else this.copy(buyTrades = finalMatchInProgress.remainingOppTrades,
                        sellTrades = remainingSameWayTrades,
                        results = finalMatchInProgress.results)
            }
        }
        else -> throw UnsupportedOperationException()
    }


    data class MatchInProgress(val remainingQuantity: Long,
                               val remainingOppTrades: List<Trade> = listOf(),
                               val results: List<MatchingResult> = listOf())

    private fun withTrade(trade: Trade) = if (trade.way == Way.B)
        copy(buyTrades = (buyTrades + trade))
    else
        copy(sellTrades = (sellTrades + trade))

    private fun matchCompletely(trade1: Trade, trade2: Trade, quantity: Long): MatchedResult =
            if (trade1.way == Way.B) {
                MatchedResult(trade1.id!!, trade1.price, trade2.id!!, trade2.price, quantity)
            } else {
                MatchedResult(trade2.id!!, trade2.price, trade1.id!!, trade1.price, quantity)
            }

    private fun Way.opposite() = if (Way.B == this) Way.S else Way.B
    private fun Way.trades() = if (Way.B == this) buyTrades else sellTrades
}