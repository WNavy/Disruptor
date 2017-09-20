package com.whj.workpool;

import com.lmax.disruptor.WorkHandler;

/**
 * Created by wuhaijun on 2017/9/20.
 * 消费者
 */
public class TradeHandler implements WorkHandler<TradeEvent> {

    @Override
    public void onEvent(TradeEvent tradeEvent) throws Exception {
        System.out.println("消费者1开始消费，消费的消息为：" + tradeEvent);
    }
}
