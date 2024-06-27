plugins {
    `kotlin-dsl`
}

apply(from = "sharedExtraProps.gradle.kts")

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

dependencies {
    implementation(platform(libs.nucleus.bom))

    implementation(libs.dependencyManagement.plugin)
    implementation(libs.sonarqube.plugin)
    implementation(libs.spotbugs.plugin)
    implementation(libs.springBoot.plugin)
    implementation(libs.vaadin.plugin)
    implementation(libs.hilla.plugin)

    // https://stackoverflow.com/questions/67795324/gradle7-version-catalog-how-to-use-it-with-buildsrc
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
