package io.alank.reactiveplayground.domain.matching

import io.alank.reactiveplayground.domain.trade.TradeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class MatchingService(private val tradeRepository: TradeRepository) {
    fun match() = tradeRepository
            .findAll()
            .groupBy { it.ticker }
            .flatMap { instrumentGroup ->
                val initial = InstrumentMatching(ticker = instrumentGroup.key()!!)
                instrumentGroup.scan(initial) { matchingInProgress, trade ->
                    matchingInProgress.handle(trade)
                }
            }
            .flatMap { Flux.fromIterable(it.results) }
}

