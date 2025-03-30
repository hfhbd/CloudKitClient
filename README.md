# CloudKitClient

This Kotlin library allows you to access your Apple CloudKit public database from your server.
This library uses the serverKey authentication.

## Supported Server Platforms

- JVM

### Unsupported Server Platforms
- nodeJS
- Native

## Other Client Platforms

- iOS/macOS/watchOS/tvOS: use the native library from Apple
- Android: use the JS library from Apple
- JS: use the JS library from Apple

## Supported Databases

Currently, the public and the private databases are supported.

## Key Generation

CloudKit requires an ECDSA keypair with the prime parameter.
The public key is uploaded to the iCloud dashboard, which returns the `keyID`.
The private key is used to sign your requests.
To generate the private key using the JVM supported PKCS8 format, use the following commands:

```
openssl ecparam -name prime256v1 -out gen.pem -genkey -noout

openssl pkcs8 -topk8 -in gen.pem -nocrypt -out privateKey.pem
```

To retrieve the private key use `cat privateKey.pem`, for the public key use `openssl ec -in privateKey.pem -pubout`.

## Reference

https://developer.apple.com/library/archive/documentation/DataManagement/Conceptual/CloudKitWebServicesReference/index.html#
