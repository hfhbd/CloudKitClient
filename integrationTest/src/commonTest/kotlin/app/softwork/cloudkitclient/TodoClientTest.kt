package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.internal.*
import kotlin.test.*

class TodoClientTest {
    @Test
    fun lookupUser() = runTest {
        val lookup =
            client.publicDB.lookup(Lookup.byMail("foo@example.com"))
        assertEquals(1, lookup.size)
    }

    @Test
    fun queryTodos() = runTest {
        val todos = client.publicDB.query(TodoRecord)
        assertEquals(1, todos.size)
        val todo = todos.first()
        assertEquals("FirstTodo", todo.recordName)
    }
}
