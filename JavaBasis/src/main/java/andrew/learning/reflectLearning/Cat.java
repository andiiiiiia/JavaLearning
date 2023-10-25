package andrew.learning.reflectLearning;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Cat extends Animal {
    public String catType;

    private String nickName;

    Date birth;

    public Cat() {
        System.out.println("cat 无参构造方法");
    }

    private Cat(String catType) {
        super();
        this.catType = catType;
    }

    public Cat(String name, Integer age, BigDecimal weight, String animalType, String catType, String nickName, Date birth) {
        super(name, age, weight, animalType);
        this.catType = catType;
        this.nickName = nickName;
        this.birth = birth;
    }

    public void attack() {
        System.out.println("cat:" + nickName + " is attack");
    }

    void eat() {
        System.out.println("cat:" + nickName + " is eating!");
    }

    private void clamp() {
        System.out.println("cat:" + nickName + "is clamping!");
    }

    public void run() {
        System.out.println("cat:" + nickName + " is running!");
    }

    @Override
    public String toString() {
        return "Cat{" +
                "type='" + catType + '\'' +
                ", name='" + nickName + '\'' +
                ", birth=" + birth +
                '}';
    }
}
