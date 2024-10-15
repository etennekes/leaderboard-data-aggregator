package nl.group9.lda.directorydata

import nl.group9.lda.filedata.FileData

data class DirectoryData(
    val directory: String,
    val fileDataList: Map<String, FileData>
)
