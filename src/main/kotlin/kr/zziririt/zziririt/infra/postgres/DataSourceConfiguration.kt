package kr.zziririt.zziririt.infra.postgres

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.*
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource


@Profile("prod")
@Configuration
class DataSourceConfiguration {
    @Bean(PRIMARY_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari")
    fun primaryDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean(SECONDARY_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.secondary.hikari")
    fun secondaryDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    @Primary
    @DependsOn(PRIMARY_DATASOURCE, SECONDARY_DATASOURCE) //(괄호) 안의 빈이 등록된 후 등록 진행.
    fun routingDataSource(
        @Qualifier(
            PRIMARY_DATASOURCE
        ) primaryDataSource: DataSource,
        @Qualifier(
            SECONDARY_DATASOURCE
        ) secondaryDataSource: DataSource
    ): DataSource {
        val routingDataSource = RoutingDataSource()
        val dataSourceMap: MutableMap<Any, Any> = HashMap()

        dataSourceMap["primary"] = primaryDataSource
        dataSourceMap["secondary"] = secondaryDataSource

        routingDataSource.setTargetDataSources(dataSourceMap)
        routingDataSource.setDefaultTargetDataSource(primaryDataSource)

        return routingDataSource
    }

    @Bean
    @DependsOn("routingDataSource")
    fun dataSource(routingDataSource: DataSource): LazyConnectionDataSourceProxy {
        //스프링 자체가 트랜잭션시 커넥션을 가져가는 로직 때문에 커넥션 고갈이 생길 수 있으므로 필요할 때만 가져가도록 설정(Lazy)
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    companion object {
        const val PRIMARY_DATASOURCE: String = "primaryDataSource"
        const val SECONDARY_DATASOURCE: String = "secondaryDataSource"
    }
}