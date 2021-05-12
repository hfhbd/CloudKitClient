rootProject.name = "cloudkit-client"

include(":core")
include(":testing")

val keyID: String? = System.getProperty("keyID")
if (keyID != null) {
    include(":integrationTest")
}
