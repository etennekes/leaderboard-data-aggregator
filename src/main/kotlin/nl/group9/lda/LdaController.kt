package nl.group9.lda

import nl.group9.lda.aggregation.Aggregation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/data")
class LdaController(private val service: LdaService) {

    @GetMapping( "/increment")
    fun getIncrement(): Int {
        return service.latestIncrement()
    }

    @GetMapping( "/{increment}")
    fun getIncrementAggregatedData(@PathVariable increment: Int): Aggregation {
        return service.aggregatedData(increment, null)
    }

    @GetMapping( "/{increment}/{previous}")
    fun getIncrementAggregatedData(@PathVariable increment: Int, @PathVariable previous: Int): Aggregation {
        return service.aggregatedData(increment, previous)
    }

    @GetMapping
    fun getAggregatedData(): Aggregation {
        val increment = service.latestIncrement()
        return service.aggregatedData(increment, null)
    }
}