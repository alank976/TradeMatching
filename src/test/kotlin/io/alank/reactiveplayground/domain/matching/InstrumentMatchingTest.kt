package io.alank.reactiveplayground.domain.matching

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.Way.B
import io.alank.reactiveplayground.domain.trade.Way.S
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class InstrumentMatchingTest {

    @Test
    fun `test handle when brand new state`() {
        val instrumentMatching = InstrumentMatching(ticker = "HSBC")
        val givenTrade = Trade(id = "1",
                ticker = "HSBC",
                way = B,
                price = 1.0,
                quantity = 10)

        val actual = instrumentMatching.handle(givenTrade)
        assertThat(actual)
                .isEqualTo(InstrumentMatching(ticker = "HSBC",
                        buyTrades = listOf(givenTrade),
                        sellTrades = listOf(),
                        results = listOf()))
    }

    @Test
    fun `test handle when a buy exists, expect a completely matched result`() {
        val givenTrade = Trade(id = "1",
                ticker = "HSBC",
                way = B,
                price = 1.0,
                quantity = 10)
        val instrumentMatching = InstrumentMatching(ticker = "HSBC", buyTrades = listOf(givenTrade))

        val newSellTrade = givenTrade.copy(id = "2", way = S)
        val actual = instrumentMatching.handle(newSellTrade)
        assertThat(actual)
                .isEqualTo(InstrumentMatching(ticker = "HSBC",
                        buyTrades = listOf(),
                        sellTrades = listOf(),
                        results = listOf(MatchedResult("1", 1.0, "2", 1.0, 10))))
    }

    @Test
    fun `test handle when a smaller buy exists, expect a completely matched result and sell trade residual`() {
        val givenTrade = Trade(id = "1",
                ticker = "HSBC",
                way = B,
                price = 1.0,
                quantity = 2)
        val instrumentMatching = InstrumentMatching(ticker = "HSBC", buyTrades = listOf(givenTrade))

        val newSellTrade = givenTrade.copy(id = "2", way = S, quantity = 10)
        val actual = instrumentMatching.handle(newSellTrade)
        assertThat(actual)
                .isEqualTo(InstrumentMatching(ticker = "HSBC",
                        buyTrades = listOf(),
                        sellTrades = listOf(newSellTrade.copy(quantity = 8)),
                        results = listOf(MatchedResult("1", 1.0, "2", 1.0, 2))))
    }

    @Test
    fun `test handle when more than 1 buy exist, expect completely matched result and residual buy trades remain`() {
        val buyTrades = listOf(
                Trade(id = "1",
                        ticker = "HSBC",
                        way = B,
                        price = 1.0,
                        quantity = 2),
                Trade(id = "2",
                        ticker = "HSBC",
                        way = B,
                        price = 2.0,
                        quantity = 3))
        val instrumentMatching = InstrumentMatching(ticker = "HSBC", buyTrades = buyTrades)
        val sellTrade =Trade(id = "3",
                ticker = "HSBC",
                way = S,
                price = 1.5,
                quantity = 3)

        val actual = instrumentMatching.handle(sellTrade)
        assertThat(actual)
                .isEqualTo(InstrumentMatching(ticker = "HSBC",
                        buyTrades = listOf( Trade(id = "2",
                                ticker = "HSBC",
                                way = B,
                                price = 2.0,
                                quantity = 2)),
                        sellTrades = listOf(),
                        results = listOf(MatchedResult("1", 1.0, "3", 1.5, 2),
                                MatchedResult("2", 2.0, "3", 1.5, 1))))
    }
}