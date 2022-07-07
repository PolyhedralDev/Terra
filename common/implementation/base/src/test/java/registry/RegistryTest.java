/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package registry;

import org.junit.jupiter.api.Test;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;

import static org.junit.jupiter.api.Assertions.*;


public class RegistryTest {
    @Test
    public void openRegistry() {
        OpenRegistry<String> test = new OpenRegistryImpl<>(TypeKey.of(String.class));
        
        test.register(RegistryKey.parse("test:test"), "bazinga");
        
        assertEquals(test.get(RegistryKey.parse("test:test")).orElseThrow(), "bazinga");
    }
    
    @Test
    public void openRegistryChecked() {
        OpenRegistry<String> test = new OpenRegistryImpl<>(TypeKey.of(String.class));
        
        test.registerChecked(RegistryKey.parse("test:test"), "bazinga");
        
        try {
            test.registerChecked(RegistryKey.parse("test:test"), "bazinga2");
            fail("Shouldn't be able to re-register with #registerChecked!");
        } catch(DuplicateEntryException ignore) {

        }
    }
    
    @Test
    public void checkedRegistry() {
        CheckedRegistry<String> test = new CheckedRegistryImpl<>(new OpenRegistryImpl<>(TypeKey.of(String.class)));
        
        test.register(RegistryKey.parse("test:test"), "bazinga");
        
        assertEquals(test.get(RegistryKey.parse("test:test")).orElseThrow(), "bazinga");
        
        try {
            test.register(RegistryKey.parse("test:test"), "bazinga2");
            fail("Shouldn't be able to re-register in CheckedRegistry!");
        } catch(DuplicateEntryException ignore) {

        }
    }
    
    @Test
    public void getID() {
        OpenRegistry<String> test = new OpenRegistryImpl<>(TypeKey.of(String.class));
        
        test.register(RegistryKey.parse("test:test"), "bazinga");
        
        assertEquals(test.getByID("test").orElseThrow(), "bazinga");
    }
    
    @Test
    public void getIDAmbiguous() {
        OpenRegistry<String> test = new OpenRegistryImpl<>(TypeKey.of(String.class));
        
        test.registerChecked(RegistryKey.parse("test:test"), "bazinga");
        test.registerChecked(RegistryKey.parse("test2:test"), "bazinga");
        
        try {
            test.getByID("test");
            fail("Shouldn't be able to get with ambiguous ID!");
        } catch(IllegalArgumentException ignore) {

        }
    }
}
