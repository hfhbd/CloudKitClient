rootProject.name = "cloudkitclient"

include(":cloudkitclient-core")
include(":cloudkitclient-testing")

val keyID: String? = System.getProperty("keyID")
if (keyID != null) {
    include(":integrationTest")
}
