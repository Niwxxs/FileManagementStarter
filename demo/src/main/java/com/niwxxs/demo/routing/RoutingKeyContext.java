package com.niwxxs.demo.routing;

public final class RoutingKeyContext {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<String>();

    private RoutingKeyContext() {
    }

    public static void setKey(String key) {
        CONTEXT.set(key);
    }

    public static String getKey() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
