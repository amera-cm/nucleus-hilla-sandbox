plugins {
    `java-library`
    `maven-publish`
    id("cmtbuild.java-conventions")
}

val cmtMvnName: String by extra
val cmtMvnUrl: String by extra
val cmtMvnUsername: String by extra
val cmtMvnPassword: String by extra

group = "io.cmt.nucleus"

publishing {
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
    publications {
        create<MavenPublication>(cmtMvnName) {
            from(components["java"])
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
