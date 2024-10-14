package nl.group9.lda.aggregator

data class DirectoryData(
    val directory: String,
    val fileDataList: Map<String, FileData>
) {

}
