package nl.group9.lda

import nl.group9.lda.aggregation.Aggregation
import nl.group9.lda.aggregator.AggregatorService
import org.springframework.stereotype.Service

@Service
class LdaService(val aggregatorService: AggregatorService) {

    fun latestIncrement(): Int {
        return aggregatorService.latestIncrement()
    }

    fun getAggregation(increment: Int, previous: Int?): Aggregation {
        val current = aggregatorService.getIncrement(increment)
        if (previous == null) {
            return current
        }
        val former = aggregatorService.getIncrement(previous)
        return current.copy().apply { diff = getDiff(current, former) }
    }

    private fun getDiff(current: Aggregation, former: Aggregation): List<String> {
        val currentList = current.overall.map { taggedResults -> taggedResults.tag }.toMutableList()
        val formerList = former.overall.map { taggedResults -> taggedResults.tag }.toList()
        currentList.removeAll(formerList)
        return currentList.toList()
    }
}
