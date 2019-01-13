package io.alank.reactiveplayground.domain.matching

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.TradeRepository
import io.alank.reactiveplayground.domain.trade.Way
import io.alank.reactiveplayground.domain.trade.Way.S
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.core.publisher.Flux.just


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MatchingServiceTest {
    private val tradeRepository: TradeRepository = mockk()
    private val matchingService = MatchingService(tradeRepository)

    @Test
    fun `test match`() {
        every {
            tradeRepository.findAll()
        } returns just(
                Trade(id = "1", ticker = "5.HK", way = Way.B, price = 1.0, quantity = 20),
                Trade(id = "2", ticker = "SOFTBANK", way = Way.B, price = 111.0, quantity = 90),
                Trade(id = "3", ticker = "5.HK", way = S, price = 2.0, quantity = 5),
                Trade(id = "4", ticker = "5.HK", way = S, price = 3.0, quantity = 10),
                Trade(id = "5", ticker = "SOFTBANK", way = S, price = 112.0, quantity = 90)
        )
        val result = matchingService.match()
        assertThat(result.collectList().block())
                .isEqualTo(listOf(
                        MatchedResult(buyTradeId = "1", buyPrice = 1.0, sellTradeId = "3", sellPrice = 2.0, quantity = 5),
                        MatchedResult(buyTradeId = "1", buyPrice = 1.0, sellTradeId = "4", sellPrice = 3.0, quantity = 10),
                        MatchedResult(buyTradeId = "2", buyPrice = 111.0, sellTradeId = "5", sellPrice = 112.0, quantity = 90)
                ))
    }
}

