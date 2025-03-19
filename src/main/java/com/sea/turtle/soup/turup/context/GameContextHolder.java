package com.sea.turtle.soup.turup.context;

public class GameContextHolder {
    private static final ThreadLocal<GameContext> contextHolder = new ThreadLocal<>();



    /**
     * 设置当前线程的上下文
     */
    public static void setContext(GameContext context) {
        contextHolder.set(context);
    }

    /**
     * 获取当前线程的上下文
     */
    public static GameContext getContext() {
        return contextHolder.get();
    }

    /**
     * 清除当前线程的上下文
     */
    public static void clearContext() {
        contextHolder.remove();
    }

}
