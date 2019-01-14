package io.alank.reactiveplayground.domain.trade

interface TradeEvent

class BuySellTradeEvent(val trade: Trade) : TradeEvent {
    override fun toString(): String {
        return "BuySellTradeEvent(trade=$trade)"
    }
}

class EndOfTradeStreamEvent(val marketPrice: Double? = null) : TradeEvent