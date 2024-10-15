package nl.group9.lda.aggregation

import org.springframework.stereotype.Component

@Component
class AggregationsImpl : Aggregations {

    private val map: MutableMap<AggregationId, Aggregation> = HashMap()

    override fun store(aggregationId: AggregationId, aggregation: Aggregation) {
        map[aggregationId] = aggregation
    }

    override fun exists(aggregationId: AggregationId): Boolean {
        return map.containsKey(aggregationId)
    }

    override fun load(aggregationId: AggregationId): Aggregation {
        return map[aggregationId]!!
    }
}
