package org.wmy.chain;

/**
 * @author wmy
 * @date 2021-06-29 15:27
 */
public class HandlerChain {

    private Handler head;
    private Handler tail;

    /**
     * 给执行链中放置处理器
     */
    public void addHandler(Handler handler){
        if (head == null){
            head = handler;
            tail = handler;
            return;
        }
        tail.setSuccessor(handler);
        tail = handler;
    }
    public void handle(){
        if (head != null) {
            head.handler();
        }
    }
}
