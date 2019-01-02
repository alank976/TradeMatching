package io.alank.reactiveplayground

import io.alank.reactiveplayground.domain.matchedresult.MatchingService
import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.TradeRepository
import io.alank.reactiveplayground.domain.trade.Way.B
import io.alank.reactiveplayground.domain.trade.Way.S
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
@SpringBootTest
class ReactivePlaygroundApplicationTests {
    @Autowired
    private lateinit var tradeRepository: TradeRepository
    @Autowired
    private lateinit var matchingService: MatchingService

    @Test
    fun contextLoads() {
    }

    @Test
    fun fooTest() {
        val pipeline = tradeRepository
                .saveAll(Flux.just(
                        Trade(ticker = "5.HK", way = B, price = 1.0, quantity = 20),
                        Trade(ticker = "SOFTBANK", way = B, price = 111.0, quantity = 90),
                        Trade(ticker = "5.HK", way = S, price = 2.0, quantity = 20)
                ))
                // wait until upstream done
                .flatMap { matchingService.match() }

        val result = pipeline.collectList().block()
        assertThat(result)
                .hasSize(2)
//        println(result)

    }

}

