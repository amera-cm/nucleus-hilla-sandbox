import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    jacoco
    checkstyle
    id("cmtbuild.commons")
    id("com.github.spotbugs")
    id("io.spring.dependency-management")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

val cmtMvnName: String by extra
val cmtMvnUrl: String by extra
val cmtMvnUsername: String by extra
val cmtMvnPassword: String by extra

repositories {
    maven {
        name = cmtMvnName
        url = uri(cmtMvnUrl)
        credentials {
            username = cmtMvnUsername
            password = cmtMvnPassword
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(1, "minutes")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(platform(libs.nucleus.bom))

    implementation(libs.spotbugs.annotations)
    implementation(libs.log4j.api)

    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)

    testImplementation(libs.hamcrestLibrary)
    testImplementation(libs.junitJupiter.api)
    testImplementation(libs.lombok)
    testImplementation(libs.mockito.core)

    testRuntimeOnly(libs.junitJupiter.engine)

    testAnnotationProcessor(libs.lombok)

    checkstyle(libs.checkstyle)
    checkstyle(platform(libs.nucleus.bom))
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Checkstyle>().configureEach {
    val cfgDir = "${rootDir}/build-tools/checkstyle"
    val cfgFile = "${cfgDir}/checkstyle.xml"
    configDirectory.set(file(cfgDir))
    configFile = file(cfgFile)
    maxWarnings = 0
}

tasks.withType<SpotBugsTask>().configureEach {
    reports {
        create("html") {
            enabled = true
        }
        create("xml") {
            enabled = true
        }
    }
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}

tasks.withType<Javadoc> {
    isFailOnError = false
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

spotbugs {
    excludeFilter.set(file("${rootDir}/build-tools/spotbugs/spotbugs-exclude.xml"))
}
