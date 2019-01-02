package io.alank.reactiveplayground.domain.trade

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

@Service
class TradeHandler(private val repository: TradeRepository) {

    fun getAll(request: ServerRequest): Mono<ServerResponse> =
            ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(repository.findAll(), Trade::class.java)

    fun save(request: ServerRequest): Mono<ServerResponse> =
            request.bodyToMono(Trade::class.java)
                    .flatMap { repository.save(it) }
                    .flatMap {
                        ServerResponse.created(URI.create("/api/v1/trades/${it.id}"))
                                .body(Mono.just(it), Trade::class.java)
                    }
                    .switchIfEmpty(ServerResponse.badRequest().build())
}