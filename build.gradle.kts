plugins {
    java
    id("io.freefair.lombok") version "8.11"
}

group = "tech.forethought"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.noear:solon-parent:3.0.4.1"))
    implementation("org.noear:solon-web")
    /* data */
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("org.noear:wood-solon-plugin")
    implementation("org.postgresql:postgresql:42.7.4")
    /* other */
    implementation("org.noear:nami-channel-http")
    implementation("org.noear:nami.coder.snack3")
    implementation("org.noear:solon.logging.logback")
    implementation("org.noear:solon.scheduling.quartz")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.jar {
    archiveBaseName = "stock"
    manifest.attributes["Main-Class"] = "tech.forethought.stock.Main"
    dependsOn(configurations.runtimeClasspath)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    from(sourceSets.main.get().output)
}