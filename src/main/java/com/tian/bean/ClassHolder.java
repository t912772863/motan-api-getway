package com.tian.bean;

/**
 * Created by tom on 2018/9/28.
 */
public class ClassHolder {
    private String classPath;
    private String methodName;

    public String getClassPath()
    {
        return this.classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String toString()
    {
        return "ClassHolder{classPath='" + this.classPath + '\'' + ", methodName='" + this.methodName + '\'' + '}';
    }
}
