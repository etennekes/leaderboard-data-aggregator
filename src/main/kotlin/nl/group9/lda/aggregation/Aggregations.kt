package nl.group9.lda.aggregation

interface Aggregations {
    fun store(aggregationId: AggregationId, aggregation: Aggregation)
    fun exists(aggregationId: AggregationId): Boolean
    fun load(aggregationId: AggregationId): Aggregation
}
