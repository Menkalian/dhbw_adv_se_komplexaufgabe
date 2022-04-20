plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
}

application {
    // Define the main class for the gradle `run`-Task
    mainClass.set("dhbw.inf.ase.komplexaufgabe.App")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.8.1")
        }
    }
}
