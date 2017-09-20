package com.whj.workpool;

import com.lmax.disruptor.*;
import com.whj.api.TradeProcuder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuhaijun on 2017/9/20.
 */
public class TradeMain {

    public static void main(String[] args) throws Exception{

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

        //创建WorkerPool
        WorkerPool<TradeEvent> workerPool = new WorkerPool<TradeEvent>(ringBuffer,sequenceBarrier,
                new IgnoreExceptionHandler(),new TradeHandler());

        workerPool.start(executor);

        //生产者
        for(int i = 0; i < 100; i++){
            long sequence = ringBuffer.next();
            TradeEvent tradeEvent = ringBuffer.get(sequence);
            tradeEvent.setId("Nun" + i);
            tradeEvent.setName("商品" + i + "号");
            tradeEvent.setPrice(i * 0.5);
            ringBuffer.publish(sequence);
        }
    }
}
