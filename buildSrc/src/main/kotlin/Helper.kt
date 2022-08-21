import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

internal
val Project.versionCatalogs: VersionCatalogsExtension
    get() = the<VersionCatalogsExtension>()

internal
val VersionCatalogsExtension.libs: VersionCatalog
    get() = named("libs")