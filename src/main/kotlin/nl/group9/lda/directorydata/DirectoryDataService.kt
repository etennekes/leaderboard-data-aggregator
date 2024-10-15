package nl.group9.lda.directorydata

import nl.group9.lda.filedata.DataType
import nl.group9.lda.filedata.FileDataBuilder
import nl.group9.lda.filedata.FileDataService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class DirectoryDataService(val fileDataService: FileDataService) {

    @Value("\${app.feed.folder}")
    private lateinit var feedFolder: String

    fun load(directory: String): DirectoryData? {
        val location = feedFolder + File.separator + directory + File.separator
        val list = listOfNotNull(
            fileDataService.loadFile(location + "output-real-cpq.json", DataType.REAL_CPQ),
            fileDataService.loadFile(location + "output-real-rpq.json", DataType.REAL_RPQ),
            fileDataService.loadFile(location + "output-syn-cpq.json", DataType.SYN_CPQ),
            fileDataService.loadFile(location + "output-syn-rpq.json", DataType.SYN_RPQ)
        )

        if (list.size != 4) {
            println("Directory skipped (data incomplete): $directory.")
            return null
        }

        val scores: List<BigDecimal> = list.map { fileData -> fileData.score }
        val sum = scores.sumOf { it }
        val average = sum.divide(BigDecimal(4), 2, RoundingMode.HALF_UP)

        val list4 = FileDataBuilder(-1L, -1L, -1L, average).build(DataType.OVERALL)

        return DirectoryData(
            directory,
            mapOf(
                Pair(list[0].dataType, list[0]),
                Pair(list[1].dataType, list[1]),
                Pair(list[2].dataType, list[2]),
                Pair(list[3].dataType, list[3]),
                Pair(list4.dataType, list4)
            )
        )
    }
}

