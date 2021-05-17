package app.softwork.cloudkitclient

import app.softwork.cloudkitclient.internal.*
import app.softwork.cloudkitclient.values.*
import kotlinx.coroutines.*
import kotlinx.uuid.*
import kotlin.test.*

class TodoClientTest {
    private val clients = listOf(client(), TestClient())

    @Test
    fun createTodo() = runTest(clients) { client ->
        val list = client.publicDB.create(TodoListRecord("MainList"), TodoListRecord)

        val todo = client.publicDB.create(
            TodoRecord(
                UUID().toString(),
                TodoRecord.Fields(subtitle = Value.String("Hello World"), list = Value.Reference(list))
            ), TodoRecord
        )
        delay(timeMillis = client.timeout)
        val todos = client.publicDB.query(TodoRecord)
        assertEquals(1, todos.size)

        val todosOfList = client.publicDB.query(TodoRecord) {
            TodoRecord.Fields::list eq list
        }
        assertEquals(1, todosOfList.size)

        client.publicDB.delete(todo, TodoRecord)
        delay(timeMillis = client.timeout)
        val todos2 = client.publicDB.query(TodoRecord)
        assertEquals(0, todos2.size)
        client.publicDB.delete(list, TodoListRecord)
    }

    @Test
    fun createTodoDomain() = runTest(clients) { client ->
        val list = client.publicDB.create(TodoListRecord("MainList"), TodoListRecord)
        val todo = client.publicDB.create(
            TodoRecord(Todo(UUID(), "Hello World", asset = null, listID = "MainList")),
            TodoRecord
        ).toDomain()
        delay(timeMillis = client.timeout)
        val todos = client.publicDB.query(TodoRecord).map { it.toDomain() }
        assertEquals(1, todos.size)
        client.publicDB.delete(TodoRecord(todo), TodoRecord)
        delay(timeMillis = client.timeout)
        val todos2 = client.publicDB.query(TodoRecord).map { it.toDomain() }
        assertEquals(0, todos2.size)
        client.publicDB.delete(list, TodoListRecord)
    }

    @Test
    fun uploadAndDownloadAsset() = runTest(clients) { client ->
        val assetContent = "Hello Asset".encodeToByteArray()
        val asset = client.publicDB.upload(assetContent, TodoRecord, TodoRecord.Fields::asset, recordName = "TestAsset")

        val list = client.publicDB.create(TodoListRecord("MainList"), TodoListRecord)

        client.publicDB.create(
            TodoRecord(
                UUID().toString(),
                TodoRecord.Fields(
                    subtitle = Value.String("Hello World"),
                    list = Value.Reference(Value.Reference.Ref("MainList"))
                )
            ), TodoRecord
        )
        delay(timeMillis = client.timeout)
        val todo = client.publicDB.query(TodoRecord).first()
        val todoWithAsset = todo.copy(fields = todo.fields.copy(asset = Value.Asset(asset)))
        val updatedTodo = client.publicDB.update(todoWithAsset, TodoRecord)
        val assetToDownload = assertNotNull(updatedTodo.fields.asset).value
        val downloadedContent = client.download(assetToDownload)
        assertTrue(assetContent.contentEquals(downloadedContent))
        val todoDeletedAsset =
            client.publicDB.update(updatedTodo.copy(fields = todo.fields.copy(asset = null)), TodoRecord)
        assertNull(todoDeletedAsset.fields.asset)
        client.publicDB.delete(todoDeletedAsset, TodoRecord)
        client.publicDB.delete(list, TodoListRecord)
    }

    @Test
    fun notFound() = runTest(clients) { client ->
        assertNull(client.publicDB.read("TestingNotFound", TodoRecord))
    }

    private val Client.timeout get() = if (this is TestClient) 0L else 2500L
}
