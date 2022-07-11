package cn.ishangchi.mydelay.topic;


import cn.ishangchi.mydelay.exception.TopicNullException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Shirman
 * @date : Create in 17:09 2018/4/4
 * @description: 话题工厂类
 */
public class TopicFactory {
    private static Set<Topic> topics = new HashSet<>();

    public synchronized static Topic createTopic(String name){
        if (name == null || "".equals(name)){
            throw new TopicNullException("话题内容不能为空");
        }
        Topic topic = new Topic(name);
        topics.add(topic);
        return topic;
    }
}
