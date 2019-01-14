package io.alank.reactiveplayground.domain.matching

import io.alank.reactiveplayground.domain.marketdata.MarketDataService
import io.alank.reactiveplayground.domain.trade.BuySellTradeEvent
import io.alank.reactiveplayground.domain.trade.EndOfTradeStreamEvent
import io.alank.reactiveplayground.domain.trade.TradeEvent
import io.alank.reactiveplayground.domain.trade.TradeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class MatchingService(private val tradeRepository: TradeRepository,
                      private val marketDataService: MarketDataService) {
    fun match(): Flux<MatchingResult> =
            tradeRepository
                    .findAll()
                    .groupBy { it.ticker }
                    .flatMap { instrumentGroup ->
                        val ticker = instrumentGroup.key()!!
                        val initial = InstrumentMatching(ticker = ticker)
                        instrumentGroup
                                .map<TradeEvent> { BuySellTradeEvent(it) }
                                .concatWith(marketDataService.getPrice(ticker)
                                        .map { EndOfTradeStreamEvent(it) })
                                .scan<InstrumentMatching>(initial) { matchingInProgress, event ->
                                    matchingInProgress.handle(event)
                                }
                    }
                    .flatMap { Flux.fromIterable(it.results) }
}

