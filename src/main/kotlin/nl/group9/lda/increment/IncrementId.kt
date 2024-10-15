package nl.group9.lda.increment

data class IncrementId(
    val number: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncrementId

        return number == other.number
    }

    override fun hashCode(): Int {
        return number
    }

}
