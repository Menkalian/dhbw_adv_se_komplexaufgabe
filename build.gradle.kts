plugins {
    java
}

subprojects {
    group = "dhbw.ase"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    pluginManager.withPlugin("java") {
        testing {
            suites {
                val test by getting(JvmTestSuite::class) {
                    // Use JUnit Jupiter test framework
                    useJUnitJupiter("5.8.1")
                }
            }
        }
    }
}