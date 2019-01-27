package io.alank.tradematching.domain.matching

import io.alank.tradematching.domain.account.AccountGroup
import io.alank.tradematching.domain.trade.Trade
import io.alank.tradematching.domain.trade.Way

open class MatchingResult(open val quantity: Long, open val accountGroup: AccountGroup)

data class MatchedResult(val buyTradeId: String,
                         val buyPrice: Double,
                         val sellTradeId: String,
                         val sellPrice: Double,
                         override val quantity: Long,
                         override val accountGroup: AccountGroup) : MatchingResult(quantity, accountGroup)

data class UnmatchedResult(val tradeId: String,
                           val way: Way,
                           val price: Double,
                           val marketPrice: Double?,
                           override val quantity: Long,
                           override val accountGroup: AccountGroup) : MatchingResult(quantity, accountGroup) {
    constructor(trade: Trade, marketPrice: Double?, accountGroup: AccountGroup) : this(trade.id!!, trade.way, trade.price, marketPrice, trade.quantity, accountGroup)
}
