package andrew.learning.reflectLearning;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class Animal {
    private String name;

    private Integer age;

    private BigDecimal weight;

    private String animalType;

    public Animal() {
    }

    public Animal(String name, Integer age, BigDecimal weight, String animalType) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.animalType = animalType;
    }

    abstract void eat();

    abstract void run();

    abstract void attack();

    public void sleep() {
        System.out.println("animal is sleeping!");
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", animalType='" + animalType + '\'' +
                '}';
    }
}
