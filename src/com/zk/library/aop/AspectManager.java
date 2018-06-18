package com.zk.library.aop;

import com.zk.library.aop.annotation.After;
import com.zk.library.aop.annotation.Before;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class AspectManager {

    private Map<String, Object> beanMap;
    private List<Method> beforeMethodList;
    private List<Method> afterMethodList;

    public AspectManager(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
        beforeMethodList = new ArrayList<>();
        afterMethodList = new ArrayList<>();
    }

    public void parse(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        try {
            Object aspect = clazz.newInstance();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethodList.add(method);
                }
                if (method.isAnnotationPresent(After.class)) {
                    afterMethodList.add(method);
                }
            }
            handle(aspect);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Map<Method, List<String>> getMatchBeforeMethodMap(Object target) {
        Map<Method, List<String>> result = new HashMap<>();
        for (Method method : beforeMethodList) {
            Before before = method.getAnnotation(Before.class);
            String execution = before.value();
            List<String> matchMethodList = getMatchMethod(execution, target);
            result.put(method, matchMethodList);
        }
        return result;
    }

    private Map<Method, List<String>> getMatchAfterMethodMap(Object target) {
        Map<Method, List<String>> result = new HashMap<>();
        for (Method method : afterMethodList) {
            After after = method.getAnnotation(After.class);
            String execution = after.value();
            List<String> matchMethodList = getMatchMethod(execution, target);
            result.put(method, matchMethodList);
        }
        return result;
    }


    private void handle(Object aspect) {
        for (Entry<String, Object> entry : beanMap.entrySet()) {
            String name = entry.getKey();
            Object target = entry.getValue();
            Map<Method, List<String>> matchBeforeMethodMap = getMatchBeforeMethodMap(target);
            Map<Method, List<String>> matchAfterMethodMap = getMatchAfterMethodMap(target);
            if (!matchBeforeMethodMap.isEmpty() || !matchAfterMethodMap.isEmpty()) {
                Object obj = ProxyManager.createProxy(aspect, target, matchBeforeMethodMap, matchAfterMethodMap);
                beanMap.put(name, obj);
            }
        }
    }


    private List<String> getMatchMethod(String execution, Object target) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(execution);
        Class<?>[] classes = target.getClass().getInterfaces();
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                StringBuffer methodName = new StringBuffer(clazz.getName());
                methodName.append(".").append(method.getName());
                if (pattern.matcher(methodName).matches()) {
                    result.add(methodName.toString());
                }
            }
        }
        return result;
    }

    public Map<String, Object> getBeanMap() {
        return beanMap;
    }

}
