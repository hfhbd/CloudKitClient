# CloudKitClient

This Kotlin library allows you to access your Apple CloudKit public database from your server. This library uses the
serverKey authentication.

## Stability

This library uses experimental Kotlin libraries, so binary, source and semantic stability cannot be provided!

## Supported Server Platforms

- JVM
- nodeJS (currently not available)
- Native (currently not available)

## Other Client Platforms

- iOS/macOS/watchOS/tvOS: use the native library from Apple
- Android: use the JS library from Apple
- JS: use the JS library from Apple

## Supported Databases

Technically, all 3 (public, private, shared) databases are supported. But with the serverKey authentication only the
public database is useful, because CloudKit is designed with privacy in mind, and the private database is only
accessible by the user.

## Reference

https://developer.apple.com/library/archive/documentation/DataManagement/Conceptual/CloudKitWebServicesReference/index.html#
