package com.whj.processor;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wuhaijun on 2017/9/20.
 */
public class Main {

    public static void main(String[] args){
        int ringBufferSize = 1024 * 1024;
        int poolSize = 4;

        //事件工厂
        EventFactory<TradeEvent> factory = new EventFactory<TradeEvent>() {
            @Override
            public TradeEvent newInstance() {
                return new TradeEvent();
            }
        };

        //获取RingBuffer
        RingBuffer<TradeEvent> ringBuffer = RingBuffer.createSingleProducer(factory,ringBufferSize);

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //线程池
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        BatchEventProcessor<TradeEvent> processor =
                new BatchEventProcessor<>(ringBuffer,sequenceBarrier,new TradeHandler());

        //将消息处理器提交到线程池
        executor.submit(processor);

        //创建生产者
        TradeEventProcuder procuder = new TradeEventProcuder(ringBuffer);

        for(int k = 0;k<10;k++){
            long seq = ringBuffer.next();
            TradeEvent tradeEvent = ringBuffer.get(seq);
            tradeEvent.setId("Nun" + k);
            tradeEvent.setName("商品" + k + "号");
            tradeEvent.setPrice(k * 0.5);
            ringBuffer.publish(seq);
        }
    }
}
