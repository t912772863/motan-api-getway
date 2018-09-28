package com.tian.handler;

import com.tian.bean.ClassHolder;
import com.tian.util.ComplingClassLoader;
import javassist.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tom on 2018/9/28.
 */
public class CtClassHandler<T> {
    private ClassPool classPool = ClassPool.getDefault();
    private Class<T> interfaceClass;
    private ClassHolder classHolder;

    public Class<T> makeInterface()
    {
        if (this.classHolder == null) {
            throw new RuntimeException("ClassHolder is not set");
        }

        CtClass cstring = null;
        CtClass result = null;
        try {
            cstring = this.classPool.get("java.lang.String");
            result = this.classPool.get("java.lang.Object");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        CtClass clzz = null;
        try {
            clzz = this.classPool.get(this.classHolder.getClassPath());
        }
        catch (NotFoundException localNotFoundException1) {
        }
        if (clzz == null) {
            clzz = this.classPool.makeInterface(this.classHolder.getClassPath());
        }

        for (CtMethod method : clzz.getDeclaredMethods()) {
            try {
                clzz.removeMethod(method);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        CtMethod method = null;
        try {
            method = CtNewMethod.abstractMethod(result, this.classHolder.getMethodName(), new CtClass[] { cstring }, null, clzz);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            clzz.addMethod(method);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        try
        {
            this.interfaceClass = clzz.toClass(new ComplingClassLoader(), null);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        if (clzz.isFrozen()) {
            clzz.defrost();
        }

        return this.interfaceClass;
    }

    public Object invoke(Object referer, String param)
    {
        if (this.classHolder == null) {
            throw new RuntimeException("ClassHolder is not set");
        }

        if (this.interfaceClass == null)
            throw new RuntimeException("InterfaceClass is not set");
        try
        {
            Method method = this.interfaceClass.getDeclaredMethod(this.classHolder.getMethodName(), new Class[] { String.class });
            return method.invoke(referer, new Object[] { param });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClassHolder getClassHolder() {
        return this.classHolder;
    }

    public void setClassHolder(ClassHolder classHolder) {
        this.classHolder = classHolder;
    }
}
