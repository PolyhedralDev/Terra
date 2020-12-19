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

        for(int i = 0; i < 100; i++) {
            Token t = tokenizer.fetch();
            if(t == null) break;
            System.out.println(t);

        }
    }
}
