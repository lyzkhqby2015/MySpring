package com.zk.library.aop;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

public class ProxyManager {

    public static Object createProxy(Object aspect, Object target,
                                     Map<Method, List<String>> matchBeforeMethodMap,
                                     Map<Method, List<String>> matchAfterMethodMap) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        aspectMethod(aspect, matchBeforeMethodMap, method, args);
                        Object object = method.invoke(target, args);
                        aspectMethod(aspect, matchAfterMethodMap, method, args);
                        return object;
                    }
                });
    }

    private static void aspectMethod(Object aspect, Map<Method, List<String>> matchMethodMap, Method method, Object[] args) throws Throwable{
        for (Map.Entry<Method, List<String>> entry : matchMethodMap.entrySet()) {
            Method aspectMethod = entry.getKey();
            List<String> matchBeforeList = entry.getValue();
            StringBuffer sb = new StringBuffer();
            sb.append(method.getDeclaringClass().getName())
              .append(".")
              .append(method.getName());
            if (matchBeforeList.contains(sb.toString())) {
                aspectMethod.invoke(aspect, args);
            }
        }
    }

}
