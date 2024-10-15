package nl.group9.lda.aggregation

import nl.group9.lda.filedata.TaggedResults
import nl.group9.lda.filedata.TeamTagResults

data class Aggregation(
    val overall: List<TaggedResults>,
    val synCpq: List<TaggedResults>,
    val realCpq: List<TaggedResults>,
    val synRpq: List<TaggedResults>,
    val realRpq: List<TaggedResults>,
    val teams: Map<String, List<TeamTagResults>>,
    var diff: List<String>?
)