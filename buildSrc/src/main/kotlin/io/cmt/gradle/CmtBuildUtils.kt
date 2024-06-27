package io.cmt.gradle

import org.gradle.api.Project
import java.io.File
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object CmtBuildUtils {

    const val PRE_SNAPSHOT = "SNAPSHOT"
    const val PRE_BUILD = "BUILD"
    val PRE_NONE = null

    fun semver(major: Int, minor: Int, patch: Int, pre: String?): String {
        val version = "$major.$minor.$patch"
        val suffix = when {
            pre == null -> ""
            pre.trim().uppercase() == "BUILD" -> "-" + ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))

            else -> "-" + pre.trim().uppercase()
        }
        return version + suffix
    }

    fun generateVersionInfoFiles(project: Project, destinationDir: java.io.File) {
        val version = project.version.toString()
        val versionFile = File(destinationDir, "VERSION")
        versionFile.writeText(version)

        val buildFile = File(destinationDir, "BUILD")
        buildFile.writeText(nowDateTime())

        val commitFile = File(destinationDir, "COMMIT")
        commitFile.writeText(commitHash())
    }

    private fun nowDateTime() =
        ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))!!

    private fun commitHash(): String {
        val cmd = "git --no-pager log -n1 --format=%h"
        val commit = try {
            val process = ProcessBuilder(cmd).start()
            process.inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            println("Error getting the commit hash: $e")
            ""
        }
        return commit.ifBlank { "commit-not-available" }
    }
}