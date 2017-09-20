package com.whj.api;

import com.lmax.disruptor.RingBuffer;

/**
 * Created by wuhaijun on 2017/9/20.
 * 生产者
 */
public class TradeProcuder {

    private RingBuffer<TradeEvent> ringBuffer;

    public TradeProcuder(RingBuffer<TradeEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void product(TradeEvent tradeEvent){

        //获得下一个事件槽的索引
        long sequence = ringBuffer.next();
        try{
            TradeEvent event = ringBuffer.get(sequence);
            event.setId(tradeEvent.getId());
            event.setName(tradeEvent.getName());
            event.setPrice(tradeEvent.getPrice());
        }finally{
            //写在finally中，是为了保证不管任何情况都能发布
            ringBuffer.publish(sequence);
        }
    }
}
