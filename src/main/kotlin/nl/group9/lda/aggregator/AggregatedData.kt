package nl.group9.lda.aggregator

data class AggregatedData(
    val overall: List<TaggedResults>,
    val synCpq: List<TaggedResults>,
    val realCpq: List<TaggedResults>,
    val synRpq: List<TaggedResults>,
    val realRpq: List<TaggedResults>,
    val teams: Map<String, List<TeamTagResults>>,
    var diff: List<String>?
) {

}
