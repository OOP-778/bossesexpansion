group = "me.brian"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        maven(url = "https://repo.codemc.org/repository/nms/")
        maven(url = "https://repo.codemc.org/repository/maven-public/")
    }
}
