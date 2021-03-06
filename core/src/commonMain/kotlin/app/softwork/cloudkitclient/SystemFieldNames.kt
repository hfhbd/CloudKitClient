package app.softwork.cloudkitclient

public enum class SystemFieldNames(internal val fieldName: String, public val systemFieldName: String) {
    RecordName("___recordId", "recordName"),
    Share("___share", "share"),
    Parent("___parent", "parent"),
    CreatedBy("___createdBy", "createdUserRecordName"),
    CreationTime("___createTime", "createdTimestamp"),
    ModificationTime("___modTime", "modifiedTimestamp"),
    ModifiedBy("___modifiedBy", "modifiedUserRecordName")
}