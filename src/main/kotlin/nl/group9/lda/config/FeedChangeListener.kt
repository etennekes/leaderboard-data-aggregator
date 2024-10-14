package nl.group9.lda.config

import nl.group9.lda.aggregator.DataAggregatorService
import org.springframework.boot.devtools.filewatch.ChangedFiles
import org.springframework.boot.devtools.filewatch.FileChangeListener

class FeedChangeListener(val service: DataAggregatorService) : FileChangeListener {

    override fun onChange(changeSet: MutableSet<ChangedFiles>?) {
        println("Change detected")
        service.aggregate()
    }

}