package io.alank.reactiveplayground.domain.trade

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Trade(@Id val id: String? = null,
                 val ticker: String,
                 val way: Way,
                 val price: Double,
                 val quantity: Long,
                 val account: String? = null,
                 @CreatedDate val insertionDate: LocalDateTime? = null) {
    override fun equals(other: Any?) = id == (other as Trade).id
}

enum class Way {
    B, S
}