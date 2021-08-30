package com.dfsek.terra.bukkit.population;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Field;


public final class SerializationUtil {
    public static Object fromFile(File f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new MovedObjectInputStream(new FileInputStream(f), "com.dfsek.terra.api.world.generation.population",
                                                           "com.dfsek.terra.bukkit.population"); // Backwards compat with old Gaea location
        Object o = ois.readObject();
        ois.close();
        return o;
    }
    
    public static void toFile(Serializable o, File f) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }
    
    public static class MovedObjectInputStream extends ObjectInputStream {
        private final String oldNameSpace;
        private final String newNameSpace;
        
        public MovedObjectInputStream(InputStream in, String oldNameSpace, String newNameSpace) throws IOException {
            super(in);
            this.oldNameSpace = oldNameSpace;
            this.newNameSpace = newNameSpace;
        }
        
        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            if(desc.getName().contains(oldNameSpace)) {
                String newClassName = desc.getName().replace(oldNameSpace, newNameSpace);
                return Class.forName(newClassName);
            }
            return super.resolveClass(desc);
        }
        
        @Override
        protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
            ObjectStreamClass result = super.readClassDescriptor();
            try {
                if(result.getName().contains(oldNameSpace)) {
                    String newClassName = result.getName().replace(oldNameSpace, newNameSpace);
                    Class<?> localClass = Class.forName(newClassName);
                    
                    Field nameField = ObjectStreamClass.class.getDeclaredField("name");
                    nameField.setAccessible(true);
                    nameField.set(result, newClassName);
                    
                    ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
                    Field suidField = ObjectStreamClass.class.getDeclaredField("suid");
                    suidField.setAccessible(true);
                    suidField.set(result, localClassDescriptor.getSerialVersionUID());
                }
            } catch(Exception e) {
                throw new IOException("Exception when trying to replace namespace", e);
            }
            return result;
        }
    }
}
