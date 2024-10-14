package nl.group9.lda

import nl.group9.lda.aggregator.AggregatedData
import nl.group9.lda.aggregator.DataAggregatorService
import org.springframework.stereotype.Service

@Service
class LdaService(val dataAggregatorService: DataAggregatorService) {

    fun latestIncrement(): Int {
        return dataAggregatorService.latestIncrement()
    }

    fun aggregatedData(increment: Int, previous: Int?): AggregatedData {
        val current = dataAggregatorService.getIncrement(increment)
        if (previous == null) {
            return current
        }
        val former = dataAggregatorService.getIncrement(previous!!)
        return current.copy().apply { diff = getDiff(current, former) }
    }

    private fun getDiff(current: AggregatedData, former: AggregatedData): List<String>? {
        val currentList = current.overall.map { taggedResults -> taggedResults.tag }.toMutableList()
        val formerList = former.overall.map { taggedResults -> taggedResults.tag }.toList()
        currentList.removeAll(formerList)
        return currentList.toList()
    }
}
