import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = version("1.1.0")

dependencies {
    api("commons-io:commons-io:2.7")
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.commons", "com.dfsek.terra.addons.terrascript.lib.commons")
    relocate("net.jafama", "com.dfsek.terra.addons.terrascript.lib.jafama")
}

val astSourceSet = buildDir.resolve("generated/ast")
val astPackage = astSourceSet.resolve("com/dfsek/terra/addons/terrascript/ast")

// Auto generate AST classes rather than writing them by hand
tasks.register("genTerrascriptAstClasses") {
    val packageName = astPackage.toRelativeString(astSourceSet).replace('/', '.')
    fun generateClass(name: String, imports: List<String>, nodes: List<Pair<String, List<String>>>) {
        val src = StringBuilder()
        src.appendLine("package $packageName;\n");
        for (imprt in imports) src.appendLine("import $imprt;")
        src.appendLine("""
        import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
        import com.dfsek.terra.addons.terrascript.Environment;
        
        /**
         * Auto-generated class via genTerrascriptAstClasses gradle task
         */
        public abstract class $name {
        
            public final SourcePosition position;
            private Environment environment;
            
            public $name(SourcePosition position) {
                this.position = position;
            }
            
            public Environment getEnvironment() {
                if (this.environment == null) throw new RuntimeException("Compilation bug! environment has not been set yet for AST node");
                return environment;
            }
            
            public void setEnvironment(Environment environment) {
                if (this.environment != null) throw new RuntimeException("Compilation bug! environment has already been set for AST node and cannot be changed");
                this.environment = environment;
            }
        
            public interface Visitor<R> {
                
        """.trimIndent())
        for (node in nodes) {
            src.appendLine("        R visit${node.first}$name(${node.first} ${name.toLowerCase()});")
        }

        src.appendLine("""
            |
            |    }
            |    
            |    public abstract <R> R accept(Visitor<R> visitor);
        """.trimMargin())

        for (node in nodes) {
            src.appendLine()
            // Inner class declaration
            src.appendLine("    public static class ${node.first} extends $name {\n")

            // Add fields
            for (field in node.second) {
                src.appendLine("        public final $field;")
            }
            src.appendLine()

            // Add constructor
            src.append("        public ${node.first}(")
            for (field in node.second)
                src.append("$field, ")
            src.appendLine("SourcePosition position) {\n            super(position);")
            for (field in node.second) {
                val fieldName = field.split(' ').last()
                src.appendLine("            this.$fieldName = $fieldName;")
            }

            src.appendLine("""
                |        }
                |        
                |        @Override
                |        public <R> R accept(Visitor<R> visitor) {
                |            return visitor.visit${node.first}$name(this);
                |        }
                |    }
            """.trimMargin())
        }
        src.appendLine("}")
        val outputFile = astPackage.resolve("$name.java")
        outputFile.writeText(src.toString())
    }

    doLast {
        astSourceSet.deleteRecursively()
        astPackage.mkdirs()
        generateClass("Expr", listOf(
            "com.dfsek.terra.addons.terrascript.Type",
            "com.dfsek.terra.addons.terrascript.parser.UnaryOperator",
            "com.dfsek.terra.addons.terrascript.parser.BinaryOperator",
            "java.util.List",
        ),
        listOf(
            Pair("Binary", listOf("Expr left", "BinaryOperator operator", "Expr right",)),
            Pair("Grouping", listOf("Expr expression")),
            Pair("Literal", listOf("Object value", "Type type")),
            Pair("Unary", listOf("UnaryOperator operator", "Expr operand")),
            Pair("Call", listOf("String identifier", "List<Expr> arguments")),
            Pair("Variable", listOf("String identifier")),
            Pair("Assignment", listOf("Variable lValue", "Expr rValue")),
            Pair("Void", listOf()),
        ))
        generateClass("Stmt", listOf(
            "com.dfsek.terra.addons.terrascript.Type",
            "com.dfsek.terra.api.util.generic.pair.Pair",
            "java.util.List",
        ),
        listOf(
            Pair("Expression", listOf("Expr expression")),
            Pair("Block", listOf("List<Stmt> statements")),
            Pair("FunctionDeclaration", listOf("String identifier", "List<Pair<String, Type>> parameters", "Type type", "Block body")),
            Pair("VariableDeclaration", listOf("Type type", "String identifier", "Expr value")),
            Pair("Return", listOf("Expr value")),
            Pair("If", listOf("Expr condition", "Block trueBody", "List<Pair<Expr, Block>> elseIfClauses", "Block elseBody")),
            Pair("For", listOf("Stmt initializer", "Expr condition", "Expr incrementer", "Block body")),
            Pair("While", listOf("Expr condition", "Block body")),
            Pair("NoOp", listOf()),
            Pair("Break", listOf()),
            Pair("Continue", listOf()),
        ))
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
