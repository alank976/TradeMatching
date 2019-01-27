package io.alank.tradematching.domain.matching

import io.alank.tradematching.domain.account.AccountGroup
import io.alank.tradematching.domain.account.AccountProperties
import io.alank.tradematching.domain.marketdata.MarketDataService
import io.alank.tradematching.domain.trade.Trade
import io.alank.tradematching.domain.trade.TradeRepository
import io.alank.tradematching.domain.trade.Way.B
import io.alank.tradematching.domain.trade.Way.S
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.core.publisher.Flux.just
import reactor.core.publisher.Mono


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MatchingServiceTest {
    private val tradeRepository: TradeRepository = mockk()
    private val marketDataService: MarketDataService = mockk()
    private val accountGroupAll = AccountGroup("All")
    private val accountGroupSmall = AccountGroup("small", listOf("b"))
    private val accountProperties: AccountProperties = AccountProperties(listOf(accountGroupAll, accountGroupSmall))
    private val matchingService = MatchingService(tradeRepository, marketDataService, accountProperties)

    @BeforeEach
    internal fun setUp() {
        every {
            marketDataService.getPrice(any())
        } returns Mono.just(0.0)
    }

    @Test
    fun `test match`() {
        every {
            tradeRepository.findAll()
        } returns just(
                Trade(id = "1", ticker = "5.HK", way = B, price = 1.0, quantity = 20),
                Trade(id = "2", ticker = "SOFTBANK", way = B, price = 111.0, quantity = 90),
                Trade(id = "3", ticker = "5.HK", way = S, price = 2.0, quantity = 5),
                Trade(id = "4", ticker = "5.HK", way = S, price = 3.0, quantity = 10),
                Trade(id = "5", ticker = "SOFTBANK", way = S, price = 112.0, quantity = 90)
        )
        val result = matchingService.match()
        assertThat(result.collectList().block())
                .hasSize(4)
                .isEqualTo(listOf(
                        MatchedResult(buyTradeId = "1", buyPrice = 1.0, sellTradeId = "3", sellPrice = 2.0, quantity = 5, accountGroup = accountGroupAll),
                        MatchedResult(buyTradeId = "1", buyPrice = 1.0, sellTradeId = "4", sellPrice = 3.0, quantity = 10, accountGroup = accountGroupAll),
                        MatchedResult(buyTradeId = "2", buyPrice = 111.0, sellTradeId = "5", sellPrice = 112.0, quantity = 90, accountGroup = accountGroupAll),
                        UnmatchedResult(tradeId = "1", way = B, price = 1.0, marketPrice = 0.0, quantity = 5, accountGroup = accountGroupAll)
                ))
    }

    @Test
    fun `test match, when trades have different account, expect several result sets`() {
        every {
            tradeRepository.findAll()
        } returns just(
                Trade(id = "1", ticker = "5.HK", way = B, price = 1.0, quantity = 20),
                Trade(id = "2", ticker = "SOFTBANK", way = B, price = 111.0, quantity = 90, account = "b"),
                Trade(id = "3", ticker = "5.HK", way = S, price = 2.0, quantity = 5, account = "a"),
                Trade(id = "4", ticker = "5.HK", way = S, price = 3.0, quantity = 10, account = "c"),
                Trade(id = "5", ticker = "SOFTBANK", way = S, price = 112.0, quantity = 90, account = "b")
        )
        val result = matchingService.match()
        val blockedResult = result.collectList().block()

        assertThat(blockedResult)
                .filteredOn { accountGroupAll == it.accountGroup }
                .hasSize(4)
                .isEqualTo(listOf(
                        MatchedResult(buyTradeId = "1", buyPrice = 1.0, sellTradeId = "3", sellPrice = 2.0, quantity = 5, accountGroup = accountGroupAll),
                        MatchedResult(buyTradeId = "1", buyPrice = 1.0, sellTradeId = "4", sellPrice = 3.0, quantity = 10, accountGroup = accountGroupAll),
                        MatchedResult(buyTradeId = "2", buyPrice = 111.0, sellTradeId = "5", sellPrice = 112.0, quantity = 90, accountGroup = accountGroupAll),
                        UnmatchedResult(tradeId = "1", way = B, price = 1.0, marketPrice = 0.0, quantity = 5, accountGroup = accountGroupAll)
                ))
        assertThat(blockedResult)
                .filteredOn { accountGroupSmall == it.accountGroup }
                .hasSize(1)
                .first()
                .isEqualTo(MatchedResult(buyTradeId = "2", buyPrice = 111.0, sellTradeId = "5", sellPrice = 112.0, quantity = 90, accountGroup = accountGroupSmall))
    }
}

