import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.the

internal
val Project.versionCatalogs: VersionCatalogsExtension
    get() = the<VersionCatalogsExtension>()

internal
val VersionCatalogsExtension.libs: VersionCatalog
    get() = named("libs")