package io.alank.tradematching.interfaces

import io.alank.tradematching.domain.matching.MatchingService
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Service
class MatchingHandler(private val matchingService: MatchingService) {
    fun match(request: ServerRequest): Mono<ServerResponse> = ServerResponse.ok().body(matchingService.match())
}