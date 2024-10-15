package nl.group9.lda.config

import nl.group9.lda.aggregator.AggregatorService
import org.springframework.boot.devtools.filewatch.ChangedFiles
import org.springframework.boot.devtools.filewatch.FileChangeListener

class FeedChangeListener(private val service: AggregatorService) : FileChangeListener {

    override fun onChange(changeSet: MutableSet<ChangedFiles>?) {
        println("Change detected")
        service.aggregate()
    }

}