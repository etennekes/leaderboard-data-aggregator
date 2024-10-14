package nl.group9.lda

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class LeaderboardDataAggregatorApplication

@Value("\${app.feed.folder}")
private lateinit var feedFolder: String

fun main(args: Array<String>) {
	runApplication<LeaderboardDataAggregatorApplication>(*args)
}
