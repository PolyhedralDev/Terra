package structure;

import com.dfsek.terra.addons.structure.structures.tokenizer.Lookahead;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class LookaheadTest {
    @Test
    public void lookahead() {
        Lookahead lookahead = new Lookahead(new StringReader("Test string..."));

        for(int i = 0; lookahead.next(i) != null; i++) {
            System.out.print(lookahead.next(i).getCharacter());
        }
        while(lookahead.next(0) != null) {
            System.out.print(lookahead.consume().getCharacter());
        }
    }
}
