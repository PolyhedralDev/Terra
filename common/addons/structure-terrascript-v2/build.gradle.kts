import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.0.0")

dependencies {
    api("commons-io:commons-io:2.7")
    api("org.ow2.asm:asm:9.5")
    api("org.ow2.asm:asm-commons:9.5")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.commons", "com.dfsek.terra.addons.terrascript.v2.lib.commons")
    relocate("net.jafama", "com.dfsek.terra.addons.terrascript.v2.lib.jafama")
}

val astSourceSet = buildDir.resolve("generated/ast")
val astPackage = astSourceSet.resolve("com/dfsek/terra/addons/terrascript/v2/ast")

data class ASTClass(
        val name: String,
        val imports: List<String>,
        val nodes: List<ASTNode>,
        val constructorFields: List<Pair<String, String>> = emptyList(),
)

data class ASTNode(
        val name: String,
        val constructorFields: List<Pair<String, String>>,
        val mutableFields: List<Pair<String, String>> = emptyList() // TODO - Remove mutability from AST nodes

)

// Auto generate AST classes rather than writing them by hand
tasks.register("genTerrascriptAstClasses") {


    val packageName = astPackage.toRelativeString(astSourceSet).replace('/', '.')
    fun generateClass(clazz: ASTClass) {
        val src = StringBuilder()
        src.appendLine("package $packageName;\n");
        for (imprt in clazz.imports) src.appendLine("import $imprt;")
        src.appendLine("""
        
        /**
         * Auto-generated class via genTerrascriptAstClasses gradle task
         */
        public abstract class ${clazz.name} {
        
        """.trimIndent())

        for (field in clazz.constructorFields) {
            src.appendLine("    public final ${field.second} ${field.first};")
        }

        src.appendLine("""
        | 
        |    public ${clazz.name}(${clazz.constructorFields.joinToString { "${it.second} ${it.first}" }}) {
        """.trimMargin())

        for (field in clazz.constructorFields) {
            src.appendLine("        this.${field.first} = ${field.first};")
        }

        src.appendLine("""
        |    }
        |
        |    public interface Visitor<R> {
        |        
        """.trimMargin())
        for (node in clazz.nodes) {
            src.appendLine("        R visit${node.name}${clazz.name}(${node.name} ${clazz.name.toLowerCase()});")
        }

        src.appendLine("""
            |
            |    }
            |    
            |    public abstract <R> R accept(Visitor<R> visitor);
        """.trimMargin())

        for (node in clazz.nodes) {
            src.appendLine()
            // Inner class declaration
            src.appendLine("    public static class ${node.name} extends ${clazz.name} {\n")

            // Add fields
            for (field in node.constructorFields) {
                src.appendLine("        public final ${field.second} ${field.first};")
            }
            for (field in node.mutableFields) {
                src.appendLine("        private ${field.second} ${field.first};")
            }
            src.appendLine()

            // Add constructor
            src.appendLine("""
            |        public ${node.name}(${node.constructorFields.plus(clazz.constructorFields).joinToString { "${it.second} ${it.first}" }}) {
            |            super(${clazz.constructorFields.joinToString { it.first }});
            """.trimMargin())

            for (field in node.constructorFields) {
                src.appendLine("            this.${field.first} = ${field.first};")
            }
            src.appendLine("        }")

            // Add getters and setters for mutable fields
            for (field in node.mutableFields) {
                src.appendLine("""
                    |        
                    |        public void set${field.first.capitalize()}(${field.second} value) {
                    |            this.${field.first} = value;
                    |        }
                    |        
                    |        public ${field.second} get${field.first.capitalize()}() {
                    |            if (this.${field.first} == null) throw new RuntimeException("Compilation bug! Field ${field.first} has not been set yet");
                    |            return this.${field.first};
                    |        }
                """.trimMargin())
            }

            src.appendLine("""
                |        
                |        @Override
                |        public <R> R accept(Visitor<R> visitor) {
                |            return visitor.visit${node.name}${clazz.name}(this);
                |        }
                |    }
            """.trimMargin())
        }
        src.appendLine("}")
        val outputFile = astPackage.resolve("${clazz.name}.java")
        outputFile.writeText(src.toString())
    }

    doLast {
        astSourceSet.deleteRecursively()
        astPackage.mkdirs()

        listOf(
            ASTClass(
                "Expr",
                listOf(
                    "com.dfsek.terra.addons.terrascript.v2.Type",
                    "com.dfsek.terra.addons.terrascript.v2.parser.UnaryOperator",
                    "com.dfsek.terra.addons.terrascript.v2.parser.BinaryOperator",
                    "com.dfsek.terra.addons.terrascript.v2.Environment",
                    "com.dfsek.terra.addons.terrascript.v2.Environment.Symbol",
                    "com.dfsek.terra.addons.terrascript.v2.lexer.SourcePosition",
                    "java.util.List",
                ),
                listOf(
                    ASTNode("Binary", listOf("left" to "Expr", "operator" to "BinaryOperator", "right" to "Expr")),
                    ASTNode("Grouping", listOf("expression" to "Expr")),
                    ASTNode("Literal", listOf("value" to "Object", "type" to "Type")),
                    ASTNode("Unary", listOf("operator" to "UnaryOperator", "operand" to "Expr")),
                    ASTNode("Call", listOf("identifier" to "String", "arguments" to "List<Expr>"), listOf("environment" to "Environment", "symbol" to "Symbol.Function")),
                    ASTNode("Variable", listOf("identifier" to "String"), listOf("symbol" to "Symbol.Variable")),
                    ASTNode("Assignment", listOf("lValue" to "Variable", "rValue" to "Expr")),
                    ASTNode("Void", listOf()),
                ),
                listOf("position" to "SourcePosition")
            ),
            ASTClass(
                "Stmt",
                listOf(
                    "com.dfsek.terra.addons.terrascript.v2.Type",
                    "com.dfsek.terra.api.util.generic.pair.Pair",
                    "com.dfsek.terra.addons.terrascript.v2.Environment.Symbol",
                    "com.dfsek.terra.addons.terrascript.v2.lexer.SourcePosition",
                    "java.util.List",
                    "java.util.Optional",
                ),
                listOf(
                    ASTNode("Expression", listOf("expression" to "Expr")),
                    ASTNode("Block", listOf("statements" to "List<Stmt>")),
                    ASTNode("FunctionDeclaration", listOf("identifier" to "String", "parameters" to "List<Pair<String, Type>>", "returnType" to "Type", "body" to "Block"), listOf("symbol" to "Symbol.Function")),
                    ASTNode("VariableDeclaration", listOf("type" to "Type", "identifier" to "String", "value" to "Expr")),
                    ASTNode("Return", listOf("value" to "Expr"), listOf("type" to "Type")),
                    ASTNode("If", listOf("condition" to "Expr", "trueBody" to "Block", "elseIfClauses" to "List<Pair<Expr, Block>>", "elseBody" to "Optional<Block>")),
                    ASTNode("For", listOf("initializer" to "Stmt", "condition" to "Expr", "incrementer" to "Expr", "body" to "Block")),
                    ASTNode("While", listOf("condition" to "Expr", "body" to "Block")),
                    ASTNode("NoOp", listOf()),
                    ASTNode("Break", listOf()),
                    ASTNode("Continue", listOf()),
                ),
                listOf("position" to "SourcePosition")
            ),
            ASTClass(
                "TypedExpr",
                listOf(
                    "com.dfsek.terra.addons.terrascript.v2.Type",
                    "com.dfsek.terra.addons.terrascript.v2.parser.UnaryOperator",
                    "com.dfsek.terra.addons.terrascript.v2.parser.BinaryOperator",
                    "java.util.List",
                ),
                listOf(
                    ASTNode("Binary", listOf("left" to "TypedExpr", "operator" to "BinaryOperator", "right" to "TypedExpr")),
                    ASTNode("Grouping", listOf("expression" to "TypedExpr")),
                    ASTNode("Literal", listOf("value" to "Object")),
                    ASTNode("Unary", listOf("operator" to "UnaryOperator", "operand" to "TypedExpr")),
                    ASTNode("Call", listOf("identifier" to "String", "arguments" to "List<TypedExpr>", "scopedIdentifier" to "String")),
                    ASTNode("Variable", listOf("identifier" to "String")),
                    ASTNode("Assignment", listOf("lValue" to "Variable", "rValue" to "TypedExpr")),
                    ASTNode("Void", listOf()),
                ),
                listOf("type" to "Type")
            ),
            ASTClass(
                "TypedStmt",
                listOf(
                    "com.dfsek.terra.addons.terrascript.v2.Type",
                    "com.dfsek.terra.api.util.generic.pair.Pair",
                    "java.util.List",
                    "java.util.Optional",
                ),
                listOf(
                    ASTNode("Expression", listOf("expression" to "TypedExpr")),
                    ASTNode("Block", listOf("statements" to "List<TypedStmt>")),
                    ASTNode("FunctionDeclaration", listOf("identifier" to "String", "parameters" to "List<Pair<String, Type>>", "returnType" to "Type", "body" to "Block", "scopedIdentifier" to "String")),
                    ASTNode("VariableDeclaration", listOf("type" to "Type", "identifier" to "String", "value" to "TypedExpr")),
                    ASTNode("Return", listOf("value" to "TypedExpr")),
                    ASTNode("If", listOf("condition" to "TypedExpr", "trueBody" to "Block", "elseIfClauses" to "List<Pair<TypedExpr, Block>>", "elseBody" to "Optional<Block>")),
                    ASTNode("For", listOf("initializer" to "TypedStmt", "condition" to "TypedExpr", "incrementer" to "TypedExpr", "body" to "Block")),
                    ASTNode("While", listOf("condition" to "TypedExpr", "body" to "Block")),
                    ASTNode("NoOp", listOf()),
                    ASTNode("Break", listOf()),
                    ASTNode("Continue", listOf()),
                ),
            ),
        ).forEach(::generateClass)
    }
}

tasks.getByName("compileJava") {
    dependsOn("genTerrascriptAstClasses")
}

sourceSets.getByName("main") {
    java {
        srcDirs(astSourceSet)
    }
}
