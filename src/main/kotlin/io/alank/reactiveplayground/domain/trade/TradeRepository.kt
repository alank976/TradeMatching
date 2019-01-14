package io.alank.reactiveplayground.domain.trade

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TradeRepository : ReactiveMongoRepository<Trade, String>