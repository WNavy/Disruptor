package com.whj.api;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wuhaijun on 2017/9/20.
 */
public class TradeMain {

    public static void main(String[] args){

        //定义RingBuffer的大小，推荐为2的N次方(这样便于计算，速度更快)
        int ringBufferSize = 1024 * 1024;

        //创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        //创建事件工厂
        TradeEventFactory factory = new TradeEventFactory();

        //创建disruptor
        /**
         * 参数解释：
         * ProducerType:表示生产者参与类型，ProducerType.SINGLE 单一生产者，ProducerType.MULTI 多个生产者
         * WaitStrategy:表示等待策略
         */
        Disruptor disruptor = new Disruptor(factory,ringBufferSize,executor,ProducerType.SINGLE,new YieldingWaitStrategy());

        //注册消费事件(如果有多个消费者，可以指定执行顺序)
        disruptor.handleEventsWith(new TradeHandler());

        /*
        //该种模式是两个消费者同时消费同一条数据
        disruptor.handleEventsWith(new TradeHandler(),new TradeHandlerStepTwo());
        */

        /*
        //该种方式有先后顺序，第一个消费者先执行，执行完成之后再执行第二个
        disruptor.handleEventsWith(new TradeHandler()).handleEventsWith(new TradeHandlerStepTwo());
        disruptor.handleEventsWith(new TradeHandler()).then(new TradeHandlerStepTwo());*/

        //启动disruptor
        disruptor.start();

        //获取RingBuffer
        RingBuffer ringBuffer = disruptor.getRingBuffer();

        //创建生产者
        TradeProcuder procuder = new TradeProcuder(ringBuffer);

        //生产数据
        for(int i = 0; i < 100; i++){
            TradeEvent tradeEvent = factory.newInstance();
            tradeEvent.setId("Nun" + i);
            tradeEvent.setName("商品" + i + "号");
            tradeEvent.setPrice(i * 0.5);
            procuder.product(tradeEvent);
        }

       /* disruptor.halt();
        executor.shutdown();*/
    }
}
