plugins {
    java
    application
}

dependencies {
    project(":shared")
}

application {
    // Define the main class for the gradle `run`-Task
    mainClass.set("dhbw.ase.App")
}

testing