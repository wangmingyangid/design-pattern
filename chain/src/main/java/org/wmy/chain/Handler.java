package org.wmy.chain;

/**
 * @author wmy
 * @date 2021-06-29 15:15
 * Handler 基于链表实现一个责任链模式
 */
public abstract class Handler {
    private Handler successor = null;


    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    /**
     * 模板方法，定义规则，不允许子类重写
     */
    public final void handler(){

        boolean handled  = doHandler();
        // 有下一个处理器，且当前处理器不能处理
        if (successor!=null && !handled){
            successor.handler();
        }
    }

    protected abstract boolean doHandler();

}
