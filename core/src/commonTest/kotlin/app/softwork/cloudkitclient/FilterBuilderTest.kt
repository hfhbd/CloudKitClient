package app.softwork.cloudkitclient

import kotlin.test.*

class FilterBuilderTest {
    @Test
    fun create() {
        Filter.Builder<UserRecord.Fields>().apply {
            UserRecord.Fields::firstName eq ""
        }
    }
}
