import cn.ishangchi.mydelay.delay.MyBaseDelay;
import cn.ishangchi.mydelay.queue.MyDelayInvoker;

/**
 * @author 刀锋
 * @date : Create in 9:27 2018/4/19
 * @description:
 */
public class InvokerTest implements MyDelayInvoker {
    @Override
    public void invoke(MyBaseDelay delay) {
        DataTest object = (DataTest) delay.getSerializableObject();
        System.out.println("hello " + object.getName());
    }

    @Override
    public void onError(MyBaseDelay delay, Exception e) {
        System.out.println("error");
    }
}
