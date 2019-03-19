package io.alank.tradematching.interfaces

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig {
    @Bean
    fun apiRouter(tradeHandler: TradeHandler,
                  matchingHandler: MatchingHandler) = router {
        GET("/") {
            ServerResponse.ok().build()
        }
        "/api".nest {
            (accept(APPLICATION_JSON) and "/v1").nest {
                "/trades".nest {
                    GET("/", tradeHandler::getAll)
                    POST("/", tradeHandler::save)
//                        "/{id}".nest {
//                            GET("/", tradeHandler::getOne)
//                            DELETE("/", tradeHandler::delete)
//                        }
                }
                "/matching".nest {
                    GET("/", matchingHandler::match)
                }
            }
        }
    }
}