import com.dfsek.terra.configureCommon
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.expr.StringLiteralExpr
import com.github.javaparser.StaticJavaParser

plugins {
    `java-library`
    `maven-publish`
}

buildscript {
    dependencies {
        classpath("com.github.javaparser:javaparser-symbol-solver-core:3.20.2")
    }
}

configureCommon()

group = "com.dfsek.terra.common"

dependencies {
    "shadedApi"("org.apache.commons:commons-rng-core:1.3")
    "shadedApi"("commons-io:commons-io:2.4")

    "shadedApi"("com.dfsek:Paralithic:0.3.2")
    "shadedApi"("com.dfsek:Tectonic:1.2.3")
    "shadedApi"("net.jafama:jafama:2.3.2")
    "shadedApi"("org.yaml:snakeyaml:1.27")
    "shadedApi"("org.ow2.asm:asm:9.0")
    "shadedApi"("commons-io:commons-io:2.6")

    "shadedApi"("com.googlecode.json-simple:json-simple:1.1.1")
    "shadedApi"("org.yaml:snakeyaml:1.27")

    "compileOnly"("com.google.guava:guava:30.0-jre")

    "testImplementation"("com.google.guava:guava:30.0-jre")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["jar"])
        }
    }

    repositories {
        val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"
        val mavenSnapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"

        maven(mavenUrl) {
            val mavenUsername: String? by project
            val mavenPassword: String? by project
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}

tasks.create<SourceTask>("tectonicDocs") {
    group = "terra"
    println("Scanning sources...")

    val docs = HashMap<String, String>()

    sourceSets.forEach { sourceSet ->
        sourceSet.java.forEach { file ->
            val doc = StringBuilder()
            val name = file.name.substring(0, file.name.length - 5)
            val unit = StaticJavaParser.parse(file)

            doc.append("# $name\n")

            unit.getClassByName(name).ifPresent { declaration ->
                declaration.javadoc.ifPresent {
                    doc.append("${sanitizeJavadoc(it.toText())}    \n")
                }
                declaration.extendedTypes.forEach {
                    if(!it.name.asString().equals("AbstractableTemplate")) {
                        doc.append("Inherits from [${it.name}](./${it.name})\n")
                    }
                }
                doc.append("\n")
            }

            var applicable = false
            unit.findAll(FieldDeclaration::class.java).filter { it.isAnnotationPresent("Value") }.forEach { fieldDeclaration ->
                doc.append("## ${(fieldDeclaration.getAnnotationByName("Value").get().childNodes[1] as StringLiteralExpr).asString()}\n")

                if(fieldDeclaration.isAnnotationPresent("Default")) {
                    doc.append("* Default value: ${fieldDeclaration.variables[0]}    \n")
                }
                val type =fieldDeclaration.commonType.asString()
                doc.append("* Type: [$type](./$type)    \n")
                doc.append("\n")

                fieldDeclaration.javadoc.ifPresent {
                    doc.append(sanitizeJavadoc(it.toText()))
                }
                doc.append("\n\n")
                applicable = true
            }
            val s = doc.toString()
            if (s.isNotEmpty() && applicable) docs[name] = s
        }
    }
    println("Done. Generated ${docs.size} files")

    val docsDir = File(buildDir, "tectonic")
    docsDir.mkdirs()
    docs.forEach {
        val save = File(docsDir, "${it.key}.md")
        if (save.exists()) save.delete()
        save.createNewFile()
        save.writeText(it.value)
    }
}

fun sanitizeJavadoc(doc:String):String {
    return doc.replace("<p>", "").replace("<", "\\<").replace(">", "\\>")
}