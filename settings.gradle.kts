rootProject.name = "cloudkitclient"

include(":cloudkitclient-core")
include(":cloudkitclient-testing")

System.getProperty("keyID")?.let {
    include(":integrationTest")
}
