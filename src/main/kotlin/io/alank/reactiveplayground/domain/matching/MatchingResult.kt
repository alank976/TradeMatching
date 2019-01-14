package io.alank.reactiveplayground.domain.matching

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.Way

open class MatchingResult(open val quantity: Long)

data class MatchedResult(val buyTradeId: String,
                         val buyPrice: Double,
                         val sellTradeId: String,
                         val sellPrice: Double,
                         override val quantity: Long) : MatchingResult(quantity)

data class UnmatchedResult(val tradeId: String,
                           val way: Way,
                           val price: Double,
                           val marketPrice: Double?,
                           override val quantity: Long) : MatchingResult(quantity) {
    constructor(trade: Trade, marketPrice: Double?) : this(trade.id!!, trade.way, trade.price, marketPrice, trade.quantity)
}
