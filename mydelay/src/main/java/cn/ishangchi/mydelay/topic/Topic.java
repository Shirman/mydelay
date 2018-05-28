package cn.ishangchi.mydelay.topic;

/**
 * @author Shirman
 * @date : Create in 17:04 2018/4/4
 * @description:
 */
public class Topic {
    private String name;
    public Topic() {

    }
    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this){
            return true;
        }
        if(obj instanceof Topic){
            Topic t1 = (Topic) obj;
            String t1Name = t1.getName();
            if (this.name != null & this.name.equals(t1Name)){
                return true;
            }else if(this.name == null && t1Name == null){
                return true;
            }
        }
        return false;
    }
}
