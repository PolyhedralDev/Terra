package com.dfsek.terra.api.gaea.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

public class MovedObjectInputStream extends ObjectInputStream {
    private final String oldNameSpace;
    private final String newNameSpace;

    public MovedObjectInputStream(InputStream in, String oldNameSpace, String newNameSpace) throws IOException {
        super(in);
        this.oldNameSpace = oldNameSpace;
        this.newNameSpace = newNameSpace;
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass result = super.readClassDescriptor();
        try {
            if (result.getName().contains(oldNameSpace)) {
                String newClassName = result.getName().replace(oldNameSpace, newNameSpace);
                Class localClass = Class.forName(newClassName);

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

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        if (desc.getName().contains(oldNameSpace)) {
            String newClassName = desc.getName().replace(oldNameSpace, newNameSpace);
            return Class.forName(newClassName);
        }
        return super.resolveClass(desc);
    }
}
