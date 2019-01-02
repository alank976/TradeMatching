package io.alank.reactiveplayground.domain.matchedresult

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.TradeRepository
import io.alank.reactiveplayground.domain.trade.Way
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.core.publisher.Flux

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MatchingServiceTest {
    private val tradeRepository: TradeRepository = mockk()
    private val matchingService = MatchingService(tradeRepository)

    @Test
    fun match() {
        every {
            tradeRepository.findAll()
        } returns Flux.just(
                Trade(ticker = "5.HK", way = Way.B, price = 1.0, quantity = 20),
                Trade(ticker = "SOFTBANK", way = Way.B, price = 111.0, quantity = 90),
                Trade(ticker = "5.HK", way = Way.S, price = 2.0, quantity = 20)
        )
        val result = matchingService.match()

        result.subscribe { println(it) }
    }

    @Test
    fun foo() {
        val foo = Flux.just(1, 2, 3, 4, 5)
                .compose { o ->
//                    Flux.defer {
                        var state = 0
                        o.map {
                            state += it
                            state
                        }
//                    }
                }
//                .flatMap {
//                    val a: Flux<String> = Flux.generate({ 0 }) { state, sink ->
//                        sink.next(state.toString())
//                        state + it
//                    }
//                    Flux.defer { a }
//                }
        foo.subscribe { println("1: $it") }
        foo.subscribe { println("2: $it") }
    }
}

