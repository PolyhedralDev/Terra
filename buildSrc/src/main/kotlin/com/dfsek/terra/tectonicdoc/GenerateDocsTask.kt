package com.dfsek.terra.tectonicdoc

import com.dfsek.tectonic.api.config.template.annotations.Description
import com.dfsek.tectonic.api.config.template.annotations.Final
import com.dfsek.tectonic.api.config.template.annotations.Value
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode


abstract class GenerateDocsTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val classDirectories: ListProperty<Directory>
    
    @get:OutputDirectory
    abstract val docsDirectory: DirectoryProperty
    
    @TaskAction
    fun generateDocs() {
        classDirectories.get().forEach { classesDirectory ->
            val classes = HashMap<String, ClassNode>()
            classesDirectory.asFileTree.forEach { file ->
                if (file.name.endsWith(".class")) {
                    val node = createClassNode(FileInputStream(file))
                    if (node.fields.stream().anyMatch { field ->
                            field.visibleAnnotations?.stream()?.anyMatch {
                                it.desc.equals(descriptor(Value::class.java.canonicalName))
                            } == true
                        }) {
                        var name = classesDirectory
                            .asFile
                            .toPath()
                            .relativize(file.toPath())
                            .toString()
                            .substringBeforeLast('.')
                        if (name.endsWith("Template")) {
                            name = name.substringBeforeLast("Template")
                        }
                        classes[name] = node
                    }
                }
            }
            
            val docsDir = docsDirectory.get().asFile
            docsDir.mkdirs()
            
            classes.forEach { (name, clazz) ->
                val template = DocumentedTemplate(name.substringAfterLast('/'))
                clazz.fields
                    .stream()
                    .filter { field ->
                        field.visibleAnnotations?.stream()?.anyMatch {
                            it.desc.equals(descriptor(Value::class.java.canonicalName))
                        } == true
                    }.forEach { field ->
                        val annotations = field.visibleAnnotations
                        
                        val description = StringBuilder()
                        
                        annotations.stream().filter {
                            it.desc.equals(descriptor(Description::class.java.canonicalName))
                        }.forEach {
                            description.append(it.values[1])
                        }
                        
                        val keyName = StringBuilder()
                        
                        if (annotations.stream().anyMatch { it.desc.equals(descriptor(Final::class.java.canonicalName)) }) {
                            keyName.append("final ")
                        }
                        
                        keyName.append(getType(field))
                            .append(" ")
                        
                        annotations.stream().filter {
                            it.desc.equals(descriptor(Value::class.java.canonicalName))
                        }.forEach {
                            keyName.append(it.values[1])
                        }
                        
                        template.add(keyName.toString(), description.toString().ifBlank {
                            logger.info("No description provided for field " + field.name + " in class " + name)
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
    
    private fun getType(node: FieldNode): String {
        if (node.signature != null) {
            return generic(node.signature)
        }
        return descriptorToHumanReadable(node.desc).substringAfterLast('.')
    }
    
    private fun generic(type: String): String {
        val clean = descriptorToHumanReadable(type)
        
        if (clean.contains('<')) {
            val typeIndex = clean.indexOf('<')
            return clean.substring(0, typeIndex + 1).substringAfterLast('.') + generic(clean.substring(typeIndex + 1)) + "\\>"
        }
        
        return clean.substringAfterLast('.')
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
        if (descriptor.startsWith('L')) {
            return descriptor.substring(1).substringBeforeLast(';').replace('/', '.')
        }
        if (descriptor.startsWith("[")) {
            return "${descriptorToHumanReadable(descriptor.substring(1))}[]"
        }
        return when (descriptor) {
            "B"  -> "byte"
            "C"  -> "char"
            "I"  -> "int"
            "D"  -> "double"
            "F"  -> "float"
            "J"  -> "long"
            "S"  -> "short"
            "Z"  -> "boolean"
            else -> descriptor
        }
    }
    
    private fun descriptor(name: String): String = "L${name.replace('.', '/')};"
}