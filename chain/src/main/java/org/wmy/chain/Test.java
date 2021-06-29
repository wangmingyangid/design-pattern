package org.wmy.chain;

/**
 * @author wmy
 * @date 2021-06-29 15:34
 */
public class Test {
    public static void main(String[] args) {
        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(new AHandler());
        handlerChain.addHandler(new BHandler());
        handlerChain.handle();
    }
}
