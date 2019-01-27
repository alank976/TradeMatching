package io.alank.tradematching

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

