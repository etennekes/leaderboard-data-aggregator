package nl.group9.lda.aggregation

data class AggregationId(val directories: Array<out String>) {

    override fun hashCode(): Int {
        return directories.contentHashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AggregationId

        return directories.contentEquals(other.directories)
    }
}
