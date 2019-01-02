package io.alank.reactiveplayground.domain.matchedresult

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.Way
import io.alank.reactiveplayground.domain.trade.Way.B
import io.alank.reactiveplayground.domain.trade.Way.S
import reactor.core.publisher.Flux

class MatchingInProgress(private var buyTrades: MutableList<Trade> = mutableListOf(),
                         private var sellTrades: MutableList<Trade> = mutableListOf()) {

    fun handle(trades: Flux<Trade>) {

    }

    fun handleTrade(trade: Trade): Flux<MatchedResult> {
        if (trade.way.opposite().trades().isEmpty()) {
            trade.way.trades().add(trade)
            return Flux.empty()
        } else {
            Flux.fromIterable(trade.way.opposite().trades())
                    .flatMap { oppTrade ->
                        val a: Flux<MatchedResult> = Flux.generate(trade::quantity) { remainingQuantity, sink ->
                            if (remainingQuantity >= oppTrade.quantity) {
                                sink.next(matchCompletely(trade, oppTrade, oppTrade.quantity))
                                trade.way.opposite().trades().remove(oppTrade)
                                remainingQuantity - oppTrade.quantity
                            } else {
                                trade.way.opposite().trades()[i] = oppTrade.copy(quantity = oppTrade.quantity - remainingQuantity)
//                    sink.next(matchCompletely(trade, oppTrade, remainingQuantity))
//                    remainingQuantity = 0
                                remainingQuantity
                            }
//                        sink.next("")
//                        remainingQuantity
                        }
                        a
                    }

        }
//        return Flux.generate { sink ->
//
//
//            trade.way.opposite().trades().forEachIndexed { i, oppTrade ->
//                if (remainingQuantity >= oppTrade.quantity) {
//                    sink.next(matchCompletely(trade, oppTrade, oppTrade.quantity))
//                    trade.way.opposite().trades().remove(oppTrade)
//                    remainingQuantity -= oppTrade.quantity
//                } else {
//                    trade.way.opposite().trades()[i] = oppTrade.copy(quantity = oppTrade.quantity - remainingQuantity)
//                    sink.next(matchCompletely(trade, oppTrade, remainingQuantity))
//                    remainingQuantity = 0
//                }
//            }
//            if (remainingQuantity > 0) {
//                trade.way.trades().add(trade.copy(quantity = remainingQuantity))
//                sink.complete()
//            }
//        }
    }

    private fun matchCompletely(trade1: Trade, trade2: Trade, quantity: Long): CompleteMatchedResult =
            if (trade1.way == B) {
                CompleteMatchedResult(trade1.id!!, trade1.price, trade2.id!!, trade2.price, quantity)
            } else {
                CompleteMatchedResult(trade2.id!!, trade2.price, trade1.id!!, trade1.price, quantity)
            }

    private fun Way.opposite() = if (B == this) S else B

    private fun Way.trades() = if (B == this) buyTrades else sellTrades

    private fun List<Trade>.totalQuantity() = map { it.quantity }.sum()
}