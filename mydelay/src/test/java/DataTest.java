import java.io.Serializable;

/**
 * @author 刀锋
 * @date : Create in 9:34 2018/4/19
 * @description:
 */
public class DataTest implements Serializable{
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
