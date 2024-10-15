package nl.group9.lda

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.modulith.Modulithic

@Modulithic
@ConfigurationPropertiesScan
@SpringBootApplication
class LeaderboardDataAggregatorApplication

fun main(args: Array<String>) {
	runApplication<LeaderboardDataAggregatorApplication>(*args)
}
