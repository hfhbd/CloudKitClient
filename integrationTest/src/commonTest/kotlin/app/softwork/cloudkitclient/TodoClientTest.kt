package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.internal.*
import app.softwork.cloudkitclient.values.*
import kotlin.test.*
import kotlinx.coroutines.*
import kotlinx.uuid.*

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
        assertNotNull(todo.recordName.toUUIDOrNull())
        assertEquals("Hello", todo.fields.subtitle.value)
    }

    @Test
    fun createTodo() = runTest {
        val todo = client.publicDB.create(TodoRecord(UUID().toString(), TodoRecord.Companion.Fields(subtitle = Value.String("Hello World"))), TodoRecord)
        delay(timeMillis = 2000)
        val todos = client.publicDB.query(TodoRecord)
        assertEquals(2, todos.size)
        client.publicDB.delete(todo, TodoRecord)
        delay(timeMillis = 1000)
        val todos2 = client.publicDB.query(TodoRecord)
        assertEquals(1, todos2.size)
    }


    @Test
    fun queryTodosDomain() = runTest {
        val todos = client.publicDB.query(TodoRecord).map { it.toDomain() }
        assertEquals(1, todos.size)
        val todo = todos.first()
        assertEquals("Hello", todo.subtitle)
    }

    @Test
    fun createTodoDomain() = runTest {
        val todo = client.publicDB.create(TodoRecord(Todo(UUID(), "Hello World")), TodoRecord).toDomain()
        delay(timeMillis = 2000)
        val todos = client.publicDB.query(TodoRecord).map { it.toDomain() }
        assertEquals(2, todos.size)
        client.publicDB.delete(TodoRecord(todo), TodoRecord)
        delay(timeMillis = 1000)
        val todos2 = client.publicDB.query(TodoRecord).map { it.toDomain() }
        assertEquals(1, todos2.size)
    }
}
