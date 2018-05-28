# mydelay简介
基于redis实现延时任务。未来计划基于mq实现延时任务

# 适用场景
延时任务如24小时自动关闭订单、7天自动收货这一类延时任务。

# 原理
## redis 版本
  基于redis过期事件通知，为每一个延时任务的键值设定对应过期时间，并在任务过期时接收到redis过期事件通知，根据用户设定的回调函数处理延时消息。
  多机情况下，支持消息共享或消息独占模式。

# 使用简单
* MyBaseDelay delay = new MyBaseDelay("hello");
* //设置延时任务所需的数据
* DataTest data = new DataTest();
* data.setAge(10);
* data.setName("baby");
* delay.setCreateTime(new Date());
* //设置回调函数，注意不要使用匿名内部类
* delay.setInvoker(new InvokerTest());
* delay.setSerializableObject(data);
* //放入延时任务队列
* delayQueue.put(delay, 10, TimeUnit.SECONDS);

详见src/main/test/java
