package nl.group9.lda.increment

interface Increments {
    fun store(incrementId: IncrementId, increment: Increment)
    fun exists(incrementId: IncrementId): Boolean
    fun load(incrementId: IncrementId): Increment
    fun nextId(): IncrementId
    fun latestId(): IncrementId
}
