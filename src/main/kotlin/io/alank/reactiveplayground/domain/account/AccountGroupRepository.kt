package io.alank.reactiveplayground.domain.account

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Repository

interface AccountGroupRepository {
    fun getAccountGroups(): List<AccountGroup>
}

@Repository
@EnableConfigurationProperties(AccountProperties::class)
class AccountGroupRepositoryImpl(val accountProperties: AccountProperties) : AccountGroupRepository {
    override fun getAccountGroups(): List<AccountGroup> {
        // TODO: may use SpEL to write filtering rules for accounts
        return listOf()
    }
}

@ConfigurationProperties("app.accounts")
data class AccountProperties(var groups: List<AccountGroup> = listOf())
