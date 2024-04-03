package kr.zziririt.zziririt.infra.postgres

import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

@Profile("prod")
class RoutingDataSource : AbstractRoutingDataSource() {
    // readOnly 옵션에 따른 트랜잭션 분기 처리
    override fun determineCurrentLookupKey(): Any {
        return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) "secondary"
        else "primary"
    }
}