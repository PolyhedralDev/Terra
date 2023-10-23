/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package structure;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import com.dfsek.terra.addons.terrascript.tokenizer.Lookahead;


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
