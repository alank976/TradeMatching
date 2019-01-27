package io.alank.tradematching.domain.account

data class AccountGroup(var name: String? = null,
                        var accounts: List<String> = listOf())