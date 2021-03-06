import com.dfsek.terra.getGitHash

val versionObj = Version("5", "0", "0", true)
version = versionObj

allprojects {
    version = versionObj
    group = "com.dfsek.terra"
}
/**
 * Version class that does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(val major: String, val minor: String, val revision: String, val preRelease: Boolean = false) {

    override fun toString(): String {
        return if (!preRelease)
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-BETA+${getGitHash()}"
    }
}