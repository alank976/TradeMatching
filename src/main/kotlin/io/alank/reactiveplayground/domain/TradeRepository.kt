package io.alank.reactiveplayground.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TradeRepository : ReactiveMongoRepository<Trade, String> {
}