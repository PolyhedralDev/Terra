package lexer;

import com.dfsek.terra.addons.terrascript.lexer.Lexer;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.lexer.Token;

import com.dfsek.terra.addons.terrascript.lexer.Token.TokenType;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    
    private static void tokenTypeTest(String input, TokenType type) {
        Lexer lexer = new Lexer(input);
        assertEquals(new Token(input, type, new SourcePosition(1, 1)), lexer.current());
    }
    
    @Test
    public void typeTest() {
        tokenTypeTest("identifier", TokenType.IDENTIFIER);
        tokenTypeTest("(", TokenType.OPEN_PAREN);
        tokenTypeTest(")", TokenType.CLOSE_PAREN);
    }
    
    @Test
    public void multipleTokensTest() {
        Lexer lexer = new Lexer("(3 + 2)");
        lexer.analyze().forEach(System.out::println);
    }
}
