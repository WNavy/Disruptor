package com.whj.processor;

import com.lmax.disruptor.RingBuffer;

/**
 * Created by wuhaijun on 2017/9/20.
 */
public class TradeEventProcuder {

    private RingBuffer<TradeEvent> ringBuffer;

    public TradeEventProcuder(RingBuffer<TradeEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void product(TradeEvent tradeEvent){
        long sequence = ringBuffer.next();
        TradeEvent event = ringBuffer.get(sequence);
        event.setId(tradeEvent.getId());
        event.setName(tradeEvent.getName());
        event.setPrice(tradeEvent.getPrice());
        ringBuffer.publish(sequence);
    }
}
