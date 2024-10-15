package nl.group9.lda.directory

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class DirectoryService {

    @Value("\${app.feed.folder}")
    private lateinit var feedFolder: String

    fun load(): Array<String> {
        val file = File(feedFolder)
        return file.list { current, name -> File(current, name).isDirectory }!!.sortedArray()

    }
}
