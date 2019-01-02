package io.alank.reactiveplayground.domain.matchedresult

import io.alank.reactiveplayground.domain.trade.Trade
import io.alank.reactiveplayground.domain.trade.TradeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class MatchingService(private val tradeRepository: TradeRepository) {
    fun match(): Flux<GroupedMatchResult> {
        return tradeRepository
                .findAll()
                .groupBy { it.ticker }
                .flatMap {
                    it.collectList()
                            .map { trades ->
                                GroupedMatchResult(it.key()!!, trades
                                        .map { CompleteMatchedResult("", 1.0, "", 1.0, 1) })
                            }
                }
    }

    private fun computeMatchResult(trades: Flux<Trade>): Flux<MatchedResult> {
        //TODO: correct this naive impl
        return trades.flatMap {
            Flux.just(
                    InCompleteMatchedResult(tradeId = it.id!!,
                            way = it.way,
                            price = it.price,
                            quantity = it.quantity,
                            marketPrice = 0.0)
            )
        }
    }

}

