package io.alank.reactiveplayground.domain.marketdata

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.random.Random

interface MarketDataService {
    fun getPrice(ticker: String): Mono<Double>
}

@Service
class MarketDataServiceImpl : MarketDataService {
    override fun getPrice(ticker: String): Mono<Double> = Mono.just(Random.nextDouble())
}