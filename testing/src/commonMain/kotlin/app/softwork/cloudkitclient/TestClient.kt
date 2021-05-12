package app.softwork.cloudkitclient

public open class TestClient : Client {
    override val publicDB: TestDatabase = TestDatabase("public")
    override val privateDB: TestDatabase = TestDatabase("private")
    override val sharedDB: TestDatabase = TestDatabase("shared")
}
