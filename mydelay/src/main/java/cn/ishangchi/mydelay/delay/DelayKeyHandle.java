package cn.ishangchi.mydelay.delay;

/**
 * @author Shirman
 * @date : Create in 10:57 2018/4/8
 * @description:
 */
public interface DelayKeyHandle {
    /**
     * 根据delay元素生成key
     * @param ob
     * @return
     */
    String generateKey(MyBaseDelay ob);
}
