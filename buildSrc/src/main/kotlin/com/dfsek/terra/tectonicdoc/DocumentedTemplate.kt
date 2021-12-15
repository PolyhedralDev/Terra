package com.dfsek.terra.tectonicdoc

class DocumentedTemplate(private val name: String) {
    private val template = HashMap<String, String>()
    
    fun add(name: String, content: String) {
        template[name] = content
    }
    
    fun format(): String {
        val builder = StringBuilder("# ").append(name).append("\n\n")
        template.forEach { name, content ->
            builder
                .append("### $name\n\n")
                .append(content)
                .append("\n\n")
        }
        return builder.toString()
    }
}