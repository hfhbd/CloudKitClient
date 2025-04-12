package app.softwork.cloudkitclient

import kotlinx.serialization.Serializable

@Serializable
public data class OperationsRequest<Fields, R : Record<Fields>, O : Operation<Fields, R>>(
    val operations: List<O>,
    val zoneID: ZoneID = ZoneID.default,
    val atomic: Boolean? = null,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false
)

public interface Operation<Fields, T : Record<Fields>> {
    public val operationType: String
    public val record: T
    public val desiredKeys: List<String>?
}

@Serializable
public data class Create<Fields, R : Record<Fields>>(override val record: R) : Operation<Fields, R> {
    override val operationType: String = "create"
    override val desiredKeys: List<String>? = null
}

@Serializable
public data class Update<Fields, R : Record<Fields>>(override val record: R) : Operation<Fields, R> {
    override val operationType: String = "replace"
    override val desiredKeys: List<String>? = null
}

@Serializable
public data class Delete<Fields, R : Record<Fields>>(override val record: R) : Operation<Fields, R> {
    override val operationType: String = "delete"
    override val desiredKeys: List<String>? = null
}
