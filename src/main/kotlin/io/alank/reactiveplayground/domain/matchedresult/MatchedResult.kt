package io.alank.reactiveplayground.domain.matchedresult

import io.alank.reactiveplayground.domain.trade.Way

open class MatchedResult(open val quantity: Long)

data class CompleteMatchedResult(val buyTradeId: String,
                                 val buyPrice: Double,
                                 val sellTradeId: String,
                                 val sellPrice: Double,
                                 override val quantity: Long) : MatchedResult(quantity) {

}

data class InCompleteMatchedResult(val tradeId: String,
                                   val way: Way,
                                   val price: Double,
                                   val marketPrice: Double,
                                   override val quantity: Long) : MatchedResult(quantity) {

}


data class GroupedMatchResult(val ticker: String, val results: List<MatchedResult>)