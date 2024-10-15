package nl.group9.lda.filedata

data class TeamTagResults(
    val tag: String,
    val build: String,
    val synCpq: List<TaggedResults>,
    val realCpq: List<TaggedResults>,
    val synRpq: List<TaggedResults>,
    val realRpq: List<TaggedResults>,
)
