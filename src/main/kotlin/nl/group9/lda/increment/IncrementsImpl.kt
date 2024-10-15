package nl.group9.lda.increment

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class IncrementsImpl : Increments {

    private val latestId: AtomicInteger = AtomicInteger(0)

    private val map: MutableMap<IncrementId, Increment> = HashMap()

    override fun store(incrementId: IncrementId, increment: Increment) {
        map[incrementId] = increment
    }

    override fun exists(incrementId: IncrementId): Boolean {
        return map.containsKey(incrementId)
    }

    override fun load(incrementId: IncrementId): Increment {
        return map[incrementId]!!
    }

    override fun nextId(): IncrementId {
        return IncrementId(latestId.incrementAndGet())
    }

    override fun latestId(): IncrementId {
        return IncrementId(latestId.get())
    }
}
