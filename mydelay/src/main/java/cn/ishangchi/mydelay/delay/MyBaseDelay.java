package cn.ishangchi.mydelay.delay;

import cn.ishangchi.mydelay.queue.ErrorProcessHandle;
import cn.ishangchi.mydelay.queue.MyDelayInvoker;
import cn.ishangchi.mydelay.queue.MyDelayQueue;
import cn.ishangchi.mydelay.topic.Topic;
import cn.ishangchi.mydelay.topic.TopicFactory;
import cn.ishangchi.mydelay.util.Jackson2JsonRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Shirman
 * @date : Create in 14:46 2018/4/4
 * @description:
 */
public class MyBaseDelay implements Serializable{
    private static Logger logger = LoggerFactory.getLogger(MyBaseDelay.class);
    private String id;
    private Date createTime;
    private Date triggerTime;
    private Topic topic;
    private MyDelayInvoker invoker;
    private String groupId;
    private Object serializableObject;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public Object getSerializableObject() {
        return serializableObject;
    }

    public <T> T getSerializableObject(Class<T> clazz) {
        try {
            return objectMapper.readValue(Jackson2JsonRedisSerializer.serialize(this.serializableObject), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSerializableObject(Object serializableObject) {
        this.serializableObject = serializableObject;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 重复次数
     */
    private AtomicInteger retryNum = new AtomicInteger();

    public int getRetryNum() {
        return retryNum.get();
    }

    public void addRetryNum(){
        retryNum.incrementAndGet();
    }

    /**
     * 处理delay元素的业务
     * @param delayQueue
     */
    public void invoke(MyDelayQueue delayQueue){
        if (invoker != null){
            try {
                invoker.invoke(this);
                delayQueue.remove(this);
            } catch (Exception e) {
                ErrorProcessHandle errorProcessHandle = delayQueue.getConfig().getErrorProcessHandle();
                if (errorProcessHandle != null) {
                    errorProcessHandle.handle(delayQueue,this);
                }
                try {
                    invoker.onError(this, e);
                }catch (Exception e1){
                    logger.error("invoker throw exception", e);
                }
            }
        }
    }

    public void setInvoker(MyDelayInvoker invoker) {
        this.invoker = invoker;
    }

    public MyDelayInvoker getInvoker() {
        return invoker;
    }

    public long remainingTime(){
        long inMsExpired = triggerTime.getTime() - System.currentTimeMillis();
        return inMsExpired;
    }

    public MyBaseDelay() {
        this.id = UUID.randomUUID().toString();
    }

    public MyBaseDelay(String topic) {
        this.id = UUID.randomUUID().toString();
        this.topic = TopicFactory.createTopic(topic);
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public MyBaseDelay(String id, String topic) {
        this.id = id;
        this.topic = TopicFactory.createTopic(topic);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }
}
