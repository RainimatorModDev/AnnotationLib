package com.iafenvoy.annotationlib.util;

import java.lang.reflect.Method;

public class MethodHelper {
    public static boolean check(Method method, Class<?> returnType, Class<?>... paramsType) {
        if (method == null || returnType == null) return false;
        if (method.getReturnType() != returnType) return false;
        if (method.getParameterCount() != paramsType.length) return false;
        Class<?>[] methodParams = method.getParameterTypes();
        for (int i = 0; i < paramsType.length; i++)
            if (methodParams[i] != paramsType[i])
                return false;
        return true;
    }

    public static boolean match(Method method, Class<?> lambdaClass) {
        if (lambdaClass.getMethods().length != 1)
            throw new IllegalArgumentException("lambdaClass must can be implement as a lambda.");
        Method lambda = lambdaClass.getMethods()[0];
        return check(method, lambda.getReturnType(), lambda.getParameterTypes());
    }
}
