plugins {
    java
    application
}

dependencies {
    implementation(project(":app02"))
    implementation(project(":shared"))
}

application {
    // Define the main class for the gradle `run`-Task
    mainClass.set("dhbw.ase.app3.App")
}
