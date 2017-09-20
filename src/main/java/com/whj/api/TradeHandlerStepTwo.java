package com.whj.api;


import com.lmax.disruptor.EventHandler;

/**
 * Created by wuhaijun on 2017/9/20.
 */
public class TradeHandlerStepTwo implements EventHandler<TradeEvent>{

    @Override
    public void onEvent(TradeEvent tradeEvent, long l, boolean b) throws Exception {
        System.out.println("消费者2开始消费，消费的消息为：" + tradeEvent);
    }
}
