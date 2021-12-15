package com.dfsek.terra.tectonicdoc

import com.dfsek.tectonic.api.config.template.annotations.Description
import com.dfsek.tectonic.api.config.template.annotations.Final
import com.dfsek.tectonic.api.config.template.annotations.Value
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode


abstract class GenerateDocsTask : DefaultTask() {
    @TaskAction
    fun generateDocs() {
        project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.forEach { sources ->
            val classes = HashMap<String, ClassNode>()
            sources.java.classesDirectory.get().asFileTree.forEach {
                if (it.name.endsWith(".class")) {
                    val node = createClassNode(FileInputStream(it))
                    if (node.fields.stream().anyMatch {
                            it.visibleAnnotations?.stream()?.anyMatch {
                                it.desc.equals(descriptor(Value::class.java.canonicalName))
                            } == true
                        }) {
                        classes[sources
                            .java
                            .classesDirectory
                            .get()
                            .asFile
                            .toPath()
                            .relativize(it.toPath())
                            .toString()
                            .substringBeforeLast('.')] = node
                    }
                }
            }
            
            val docsDir = File(project.buildDir, "tectonic")
            docsDir.mkdirs()
            
            classes.forEach { name, clazz ->
                val template = DocumentedTemplate(name.substringAfterLast('/'))
                clazz.fields
                    .stream()
                    .filter {
                        it.visibleAnnotations?.stream()?.anyMatch {
                            it.desc.equals(descriptor(Value::class.java.canonicalName))
                        } == true
                    }.forEach {
                        val annotations = it.visibleAnnotations
                        
                        val description = StringBuilder()
                        
                        annotations.stream().filter {
                            it.desc.equals(descriptor(Description::class.java.canonicalName))
                        }.forEach {
                            description.append(it.values[1])
                        }
                        
                        val name = StringBuilder()
                        
                        if (annotations.stream().anyMatch { it.desc.equals(descriptor(Final::class.java.canonicalName)) }) {
                            name.append("final ")
                        }
                        
                        name.append(descriptorToHumanReadable(it.desc))
                            .append(" ")
                            .append(it.name)
                        
                        template.add(name.toString(), description.toString().ifBlank {
                            println("No description provided for field " + it.name)
                            "*No description provided.*"
                        })
                    }
                
                
                val save = File(docsDir, "$name.md")
                if (save.exists()) save.delete()
                save.parentFile.mkdirs()
                save.createNewFile()
                save.writeText(template.format())
            }
        }
    }
    
    private fun createClassNode(input: InputStream): ClassNode {
        val reader = ClassReader(input)
        val node = ClassNode()
        try {
            reader.accept(node, ClassReader.EXPAND_FRAMES)
        } catch (e: Exception) {
            reader.accept(node, ClassReader.SKIP_FRAMES or ClassReader.SKIP_DEBUG)
        }
        return node
    }
    
    private fun descriptorToHumanReadable(descriptor: String): String {
        if(descriptor.startsWith('L')) {
            return descriptor.substring(1).substringBeforeLast(';').replace('/', '.')
        }
        if(descriptor.startsWith("[")) {
            return "${descriptorToHumanReadable(descriptor.substring(1))}[]"
        }
        return when(descriptor) {
            "B" -> "byte"
            "C" -> "char"
            "I" -> "int"
            "D" -> "double"
            "F" -> "float"
            "J" -> "long"
            "S" -> "short"
            "Z" -> "boolean"
            else -> throw IllegalArgumentException("Invalid descriptor: $descriptor")
        }
    }
    
    private fun descriptor(name: String): String = "L${name.replace('.', '/')};"
}