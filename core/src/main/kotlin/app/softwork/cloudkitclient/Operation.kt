package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.Record.Fields
import kotlinx.serialization.Serializable

@Serializable
public data class OperationsRequest<F : Fields, R : Record<F>, O : Operation<F, R>>(
    val operations: List<O>,
    val zoneID: ZoneID = ZoneID.default,
    val atomic: Boolean? = null,
    val desiredKeys: List<String>? = null,
    val numbersAsStrings: Boolean = false
)

public interface Operation<F : Fields, T : Record<F>> {
    public val operationType: String
    public val record: T
    public val desiredKeys: List<String>?
}

@Serializable
public data class Create<F : Fields, R : Record<F>>(override val record: R) : Operation<F, R> {
    override val operationType: String = "create"
    override val desiredKeys: List<String>? = null
}

@Serializable
public data class Update<F : Fields, R : Record<F>>(override val record: R) : Operation<F, R> {
    override val operationType: String = "replace"
    override val desiredKeys: List<String>? = null
}

@Serializable
public data class Delete<F : Fields, R : Record<F>>(override val record: R) : Operation<F, R> {
    override val operationType: String = "delete"
    override val desiredKeys: List<String>? = null
}
