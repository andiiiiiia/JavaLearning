package andrew.learning.reflectLearning;

import java.util.Date;

public class Cat {
    public String type;

    String name;

    Date birth;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void attack() {
        System.out.println("cat:"+ name+" is attack");
    }

    public void run() {
        System.out.println("cat:"+name+" is running!");
    }

    @Override
    public String toString() {
        return "Cat{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                '}';
    }
}
