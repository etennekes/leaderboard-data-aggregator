package nl.group9.lda.aggregator

data class TeamTagResults(
    val tag: String,
    val build: Int,
    val synCpq: List<TaggedResults>,
    val realCpq: List<TaggedResults>,
    val synRpq: List<TaggedResults>,
    val realRpq: List<TaggedResults>,
)
