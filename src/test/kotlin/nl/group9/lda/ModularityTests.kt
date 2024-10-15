package nl.group9.lda

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.modulith.core.ApplicationModules

@SpringBootTest
class ModularityTests {

	@Test
	fun contextLoads() {
		val modules = ApplicationModules.of(LeaderboardDataAggregatorApplication::class.java)
		modules.verify()
	}

}
