package com.home.config.p6spy

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.P6SpyOptions
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import jakarta.annotation.PostConstruct
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.util.*

@Configuration
class P6spyPrettySqlFormatter : MessageFormattingStrategy {

    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = this::class.java.name
    }

    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String
    ): String {
        val formattedSql = formatSql(category, sql)
        val currentDate = Date()

        val format = SimpleDateFormat("yy.MM.dd HH:mm:ss")

        return "${format.format(currentDate)} | OperationTime : ${elapsed}ms$formattedSql"
    }

    private fun formatSql(category: String, sql: String?): String {
        var formattedSql = sql ?: return ""
        formattedSql = formattedSql.trim()
        if (formattedSql.isEmpty()) return formattedSql

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.name == category) {
            val tmpSql = formattedSql.lowercase(Locale.ROOT)
            formattedSql = when {
                tmpSql.startsWith("create") ||
                        tmpSql.startsWith("alter") ||
                        tmpSql.startsWith("comment") ->
                    FormatStyle.DDL.formatter.format(formattedSql)

                else ->
                    FormatStyle.BASIC.formatter.format(formattedSql)
            }
            formattedSql = "|\nHeFormatSql(P6Spy sql,Hibernate format):$formattedSql"
        }

        return formattedSql
    }
}