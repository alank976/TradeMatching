package io.alank.reactiveplayground.domain.matchedresult

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.Way
import io.alank.reactiveplayground.domain.trade.Way.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MatchingInProgressTest {

    @Test
    fun `handleTrade when brand new state`() {
        val matchingInProgress = MatchingInProgress()
        val givenTrade = Trade(id = "1",
                ticker = "HSBC",
                way = B,
                price = 1.0,
                quantity = 10)

        assertThat(matchingInProgress.handleTrade(givenTrade).collectList().block())
                .isEmpty()
        assertThat(matchingInProgress)
                .hasFieldOrPropertyWithValue("buyTrades", listOf(givenTrade))
    }

    @Test
    fun `handleTrade when a buy exists, expect a complete matched result`() {
        val givenTrade = Trade(id = "1",
                ticker = "HSBC",
                way = B,
                price = 1.0,
                quantity = 10)
        val matchingInProgress = MatchingInProgress(buyTrades = mutableListOf(givenTrade))

        assertThat(matchingInProgress.handleTrade(givenTrade.copy(way = S)).collectList().block())
                .isNotEmpty
    }
}