package structure;

import com.dfsek.terra.api.structures.tokenizer.Token;
import com.dfsek.terra.api.structures.tokenizer.Tokenizer;
import com.dfsek.terra.api.structures.tokenizer.exceptions.TokenizerException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TokenizerTest {
    @Test
    public void tokens() throws IOException, TokenizerException {
        Tokenizer tokenizer = new Tokenizer(IOUtils.toString(getClass().getResourceAsStream("/test.tesf")));
        // Actual run
        long l = System.nanoTime();

        Token t = tokenizer.fetch();
        while(t != null) {
            System.out.println(t);
            t = tokenizer.fetch();
        }

        System.out.println((double) (System.nanoTime() - l) / 1000000);
    }
}
