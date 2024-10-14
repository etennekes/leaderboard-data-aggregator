package nl.group9.lda.aggregator

import java.math.BigDecimal

data class TaggedResults(
    val tag: String,
    val team: String,
    val build: Int,
    val data: TaggedResultsData
)

data class TaggedResultsBuilder(
    val tag: String,
    val fileData: FileData

) {
    fun build(): TaggedResults {
        val teamAndBuild = TagUtil.toTeamAndBuild(tag)
        return TaggedResults(
            tag, teamAndBuild.first, teamAndBuild.second,
            TaggedResultsData(
                fileData.prep,
                fileData.load,
                fileData.eval,
                fileData.score
            )
        )
    }
}

data class TaggedResultsData(
    val prep: Long,
    val load: Long,
    val eval: Long,
    val score: BigDecimal,
) {

}


