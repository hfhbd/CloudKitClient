module app.softwork.cloudkitclient {
    exports app.softwork.cloudkitclient;
    exports app.softwork.cloudkitclient.serializer;
    exports app.softwork.cloudkitclient.types;
    exports app.softwork.cloudkitclient.values;

    requires kotlin.stdlib;
    requires transitive io.ktor.client.java;
    requires transitive kotlinx.serialization.json;
}
