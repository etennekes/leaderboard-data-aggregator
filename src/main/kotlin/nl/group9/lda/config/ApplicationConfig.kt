package nl.group9.lda.config

import jakarta.annotation.PreDestroy
import nl.group9.lda.aggregator.AggregatorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.devtools.filewatch.FileSystemWatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.time.Duration

@Configuration
class ApplicationConfig(val service: AggregatorService) {

    @Value("\${app.feed.folder}")
    private lateinit var feedFolder: String

    @Bean(name = ["fileSystemWatcher"])
    fun fileSystemWatcher(): FileSystemWatcher {
        val fileSystemWatcher = FileSystemWatcher(true, Duration.ofMillis(5000L), Duration.ofMillis(3000L))
        fileSystemWatcher.addSourceDirectory(File(feedFolder))
        fileSystemWatcher.addListener(FeedChangeListener(service))
        fileSystemWatcher.start()
        println("File system watcher started; detecting changes.")
        return fileSystemWatcher
    }

    @PreDestroy
    @Throws(Exception::class)
    fun onDestroy() {
        fileSystemWatcher().stop()
    }
}