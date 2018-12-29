package io.alank.reactiveplayground.domain

import org.springframework.data.annotation.Id

data class Trade(@Id val id: String?,
                 val way: Way,
                 val price: Double,
                 val quantity: Long)

enum class Way {
    B, S
}