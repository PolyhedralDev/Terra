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
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;

import static org.junit.jupiter.api.Assertions.*;


public class RegistryTest {
    @Test
    public void openRegistry() {
        OpenRegistry<String> test = new OpenRegistryImpl<>();
        
        test.register("test", "bazinga");
        
        assertEquals(test.get("test").orElseThrow(), "bazinga");
    }
    
    @Test
    public void openRegistryChecked() {
        OpenRegistry<String> test = new OpenRegistryImpl<>();
        
        test.registerChecked("test", "bazinga");
        
        try {
            test.registerChecked("test", "bazinga2");
            fail("Shouldn't be able to re-register with #registerChecked!");
        } catch(DuplicateEntryException ignore) {
        
        }
    }
    
    @Test
    public void checkedRegistry() {
        CheckedRegistry<String> test = new CheckedRegistryImpl<>(new OpenRegistryImpl<>());
        
        test.register("test", "bazinga");
        
        assertEquals(test.get("test").orElseThrow(), "bazinga");
        
        try {
            test.register("test", "bazinga2");
            fail("Shouldn't be able to re-register in CheckedRegistry!");
        } catch(DuplicateEntryException ignore) {
        
        }
    }
}
