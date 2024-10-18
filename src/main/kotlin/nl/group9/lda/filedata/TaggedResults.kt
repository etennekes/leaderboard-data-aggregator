package nl.group9.lda.filedata

import nl.group9.lda.util.TagUtil
import java.math.BigDecimal
import java.math.RoundingMode

data class TaggedResults(
    val tag: String,
    val team: String,
    val build: String,
    val data: TaggedResultsData
)

data class TaggedResultsBuilder(
    val tag: String,
    val fileData: FileData

) {
    fun build(): TaggedResults {
        val tagAndTeamAndBuild = TagUtil.toTagAndTeamAndBuild(tag)
        return TaggedResults(
            tagAndTeamAndBuild.first,
            tagAndTeamAndBuild.second,
            tagAndTeamAndBuild.third,
            TaggedResultsData(
                fileData.prep,
                fileData.load,
                fileData.eval,
                fileData.score.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                fileData.score
            )
        )
    }
}

data class TaggedResultsData(
    val prep: Long,
    val load: Long,
    val eval: Long,
    val score: String,
    val comp: BigDecimal
)
