package io.alank.tradematching.interfaces

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig {
    @Bean
    fun apiRouter(handler: TradeHandler) = router {
        "/api".nest {
            (accept(APPLICATION_JSON) and "/v1").nest {
                "/trades".nest {
                    GET("/", handler::getAll)
                    POST("/", handler::save)
//                        "/{id}".nest {
//                            GET("/", handler::getOne)
//                            DELETE("/", handler::delete)
//                        }
                }
            }
        }
    }
}