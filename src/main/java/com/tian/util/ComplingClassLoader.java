package com.tian.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by tom on 2018/9/28.
 */
public class ComplingClassLoader extends ClassLoader {
    private byte[] getBytes(String filename)
            throws IOException
    {
        File file = new File(filename);
        long len = file.length();

        byte[] raw = new byte[(int)len];

        FileInputStream fin = new FileInputStream(file);

        int r = fin.read(raw);
        if (r != len) {
            throw new IOException("Can't read all, " + r + " != " + len);
        }
        fin.close();

        return raw;
    }

    public Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        Class clas = null;

        clas = findLoadedClass(name);
        if (clas != null) {
            return clas;
        }
        String fileStub = name.replace('.', '/');
        String classFilename = getClass().getResource("/").getPath() + fileStub + ".class";
        try
        {
            byte[] raw = getBytes(classFilename);

            clas = defineClass(name, raw, 0, raw.length);
        }
        catch (IOException localIOException)
        {
        }

        if (clas == null) {
            clas = findSystemClass(name);
        }

        if ((resolve) && (clas != null)) {
            resolveClass(clas);
        }
        if (clas == null) {
            throw new ClassNotFoundException(name);
        }
        return clas;
    }
}
