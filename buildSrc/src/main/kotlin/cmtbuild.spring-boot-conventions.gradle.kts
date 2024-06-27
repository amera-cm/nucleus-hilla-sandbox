import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    application
    id("cmtbuild.java-conventions")
    id("org.springframework.boot")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.springBoot.starter.log4j2)
}

configurations {
    all {
        exclude(
            group = "org.springframework.boot",
            module = "spring-boot-starter-logging"
        )
        exclude(
            group = "commons-logging",
            module = "commons-logging"
        )
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
    layered {
        isEnabled = true
    }
}

tasks.build {
    mustRunAfter("clean")
    val destinationDirectory: File = tasks.named<Jar>("jar").get().destinationDirectory.asFile.get()
    doLast {
        io.cmt.gradle.CmtBuildUtils.generateVersionInfoFiles(project, destinationDirectory)
    }
}
