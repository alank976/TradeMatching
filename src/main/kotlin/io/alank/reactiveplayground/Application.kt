package io.alank.reactiveplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication
class ReactivePlaygroundApplication

fun main(args: Array<String>) {
	runApplication<ReactivePlaygroundApplication>(*args)
}

