plugins {
    id(libs.plugins.cmtbuild.springBootConventions.get().pluginId)
    id(libs.plugins.vaadin.get().pluginId)
}

dependencies {
    implementation(libs.log4j.api)
    implementation(libs.springBoot.starter.actuator)
    implementation(libs.springBoot.starter.security)
    implementation(libs.springBoot.starter.oauth2.resourceServer)
    implementation(libs.spring.security.oauth2Jose)
    implementation(libs.springBoot.starter.web)
    implementation(libs.vaadin.core)
    implementation(libs.vaadin.springBoot.starter)
    implementation(platform(libs.vaadin.bom))

    developmentOnly(libs.springBoot.devtools)

    testImplementation(libs.springBoot.starter.test)
}
hilla {
    exposedPackagesToParser = listOf("io.cmt.nucleus.hilla_sandbox.ui.endpoints")
}

application {
    mainClass.set("io.cmt.nucleus.hilla_sandbox.ui.boot.HillaUiBootApp")
}
