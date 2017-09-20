package com.whj.api;

import com.lmax.disruptor.EventFactory;

/**
 * Created by wuhaijun on 2017/9/20.
 * 事件生产工厂
 */
public class TradeEventFactory implements EventFactory<TradeEvent> {

    @Override
    public TradeEvent newInstance() {
        return new TradeEvent();
    }
}
