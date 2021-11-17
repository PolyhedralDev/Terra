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
        
        assertEquals(test.get("test"), "bazinga");
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
        
        assertEquals(test.get("test"), "bazinga");
        
        try {
            test.register("test", "bazinga2");
            fail("Shouldn't be able to re-register in CheckedRegistry!");
        } catch(DuplicateEntryException ignore) {
        
        }
    }
}
