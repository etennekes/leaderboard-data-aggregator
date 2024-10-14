package nl.group9.lda.aggregator

import java.math.BigDecimal

data class FileData(
    val prep: Long,
    val load: Long,
    val eval: Long,
    val score: BigDecimal,
    val dataType: String
) {

}

data class FileDataBuilder(
    val prep: Long,
    val load: Long,
    val eval: Long,
    val score: BigDecimal
) {
    fun build(dataType: DataType): FileData {
        return FileData(prep, load, eval, score, dataType.name)
    }

}
