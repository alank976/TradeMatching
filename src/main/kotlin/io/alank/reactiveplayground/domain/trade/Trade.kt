package io.alank.reactiveplayground.domain.trade

import org.springframework.data.annotation.Id

data class Trade(@Id val id: String? = null,
                 val ticker: String,
                 val way: Way,
                 val price: Double,
                 val quantity: Long,
                 val account: String? = null) {
    override fun equals(other: Any?) = id == (other as Trade).id
}

enum class Way {
    B, S
}