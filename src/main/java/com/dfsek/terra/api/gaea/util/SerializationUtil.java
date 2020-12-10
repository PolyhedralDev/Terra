package com.dfsek.terra.api.gaea.util;

import com.dfsek.terra.api.gaea.serial.MovedObjectInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationUtil {
    public static Object fromFile(File f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new MovedObjectInputStream(new FileInputStream(f), "org.polydev.gaea", "com.dfsek.terra.api.gaea"); // Backwards compat with old Gaea location
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public static void toFile(Serializable o, File f) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }
}
