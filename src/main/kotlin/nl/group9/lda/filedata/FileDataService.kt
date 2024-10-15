package nl.group9.lda.filedata

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileDataService(val objectMapper: ObjectMapper) {

    fun loadFile(fileName: String, dataType: DataType): FileData? {
        val file = File(fileName)
        if (file.exists()) {
            val jsonString: String = File(fileName).readText(Charsets.UTF_8)
            val fileDataBuilder = objectMapper.readValue(jsonString, FileDataBuilder::class.java)
            return fileDataBuilder.build(dataType)
        }
        return null
    }
}

