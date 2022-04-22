plugins {
    java
    application
}

dependencies {
    implementation(project(":shared"))
}

application {
    // Define the main class for the gradle `run`-Task
    mainClass.set("dhbw.ase.App")
}
