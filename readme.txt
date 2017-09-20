Disruptor框架的学习

    1、Disruptor就是一个线程间通信的框架，在多线程共享数据
    2、在Disruptor中，保存数据的是一个换状缓冲区RingBuffer,生产者将生产的数据放在RingBuffer中，
       消费者从RingBuffer中读取数据
    3、RingBuffer是一个环状的区域，其中的每个元素都有一个序列号(sequence)来索引，RingBuffer维护当前最新
       放置的元素的序列号，这个序列号一直递增(通过取余获得元素在RingBuffer下的数组的下标)
    4、Disruption是一个无锁变成，通过单一线程的方式实现，一块数据永远只有一个线程写入。



api包下的demo:

    功能：使用原生的api,实现单一生产者，单一消费者或多个消费者

    步骤：

        第一步：创建事件的原型Model
        第二步：创建事件工厂，专门用来生产事件,实现Disruptor框架提供的EventFactory<>接口
        第三步：创建生产者
               1、要持有对RingBuffer的引用
               2、通过RingBuffer实例的next()方法，获得带插入事件的事件槽的index
               3、将传入的事件的数据放入事件槽
               4、发布事件

        第四步：创建消费者，消费者要实现Disruptor框架提供的EventHandler<>接口


创建RingBuffer，除了可以通过Disruption获得，还可以直接通过RingBuffer提供的静态方法获得

除了使用Disrpgot来启动，还可以使用workPool或者BatchEventProcessor，代码分别见workpool包和processor包

当使用WorkPool的时候，消费者要实现WorkHandler
当使用workPool或者BatchEventProcessor时，消费者要实现EvetHandler


