import io.cmt.gradle.CmtBuildUtils

apply(from = "${rootDir}/buildSrc/sharedExtraProps.gradle.kts")

extra["sharedProjectVersion"] = CmtBuildUtils.semver(major = 1, minor = 0, patch = 0, pre = CmtBuildUtils.PRE_SNAPSHOT)
