# 反射
https://zhuanlan.zhihu.com/p/405325823
## 原理
## 源码
## 使用
### 基本用法
Animal
```java
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
```
Cat
```java
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
```
#### 获取Class对象的方式
```java
    /**
     * 获取Class对象的方式
     * 1. 类.class
     * 2. 对象.getClass()
     * 3. Class.forName(类路径)
     *
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void testGetClass() throws ClassNotFoundException, ParseException {
        // 方式1
        Class clazz1 = Cat.class;
        System.out.println(clazz1.getName());
        // 方式2
        Class<?> clazz2 = Class.forName("andrew.learning.reflectLearning.Cat");
        System.out.println(clazz2.getName());
        // 方式3
        Cat cat3 = new Cat();
        Class<? extends Cat> clazz3 = cat3.getClass();
        System.out.println(clazz3.getName());
    }
```
#### 通过Class对象创建类对象
```java
/**
 * 通过class创建对象的几种方式
 * 1.Constructor.newInstance(Object ... initargs)
 * getDeclaredConstructor:可以获取所有的构造方法
 * getConstructor: 只能获取public的构造方法
 * <p>
 * 2. clazz.newInstance()
 * 这个方法只能调用无参构造方法
 */
public static void testCreateObject() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class clazz = Cat.class;
// 方式1 通过class的Constructor.newInstance()
// constructor1
        Constructor constructor1 = clazz.getDeclaredConstructor();
                Cat cat1 = (Cat) constructor1.newInstance();
                System.out.println(cat1);
                Constructor constructor2 = clazz.getDeclaredConstructor(String.class);
        constructor2.setAccessible(true);
        Cat cat2 = (Cat) constructor2.newInstance("英短");
        System.out.println(cat2);
        // constructor2
        Constructor constructor3 = clazz.getConstructor();
        Cat cat3 = (Cat) constructor3.newInstance();
        System.out.println(cat3);
        /*Constructor constructor4 = clazz.getConstructor(String.class); // 报错
        Cat cat4 = (Cat) constructor4.newInstance("英短");
        System.out.println(cat4);*/

        // 方式2 通过class.newInstance()
        Object cat5 = clazz.newInstance();
        System.out.println(cat5);
        }
```
#### 通过反射来操作属性
```java
    /**
     * 通过反射来操作属性
     * getField():
     * 只能获取public属性，包括父类的public属性
     * getDeclaredField():
     * 可以获取所哟属性,不包括父类的
     * 调用私有方法时，记得加setAccessible(true)
     */
    public static void testOperateFields() throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, ParseException {
        Class clazz = Cat.class;
        Constructor constructor = clazz.getDeclaredConstructor(String.class, Integer.class, BigDecimal.class, String.class, String.class, String.class, Date.class);
        Cat cat = (Cat) constructor.newInstance("猫", 8, new BigDecimal(10.2), "哺乳类", "蓝白", "九饼", new Date());
        // 获取属性定义
        Field catType = clazz.getField("catType");
        Field nickName = clazz.getDeclaredField("nickName");
        nickName.setAccessible(true); // 因为 nickName是private属性，即使getDeclaredField可以访问，但是也获取不了
        Field birth = clazz.getDeclaredField("birth");
        // Field name = clazz.getField("name"); // 获取父类private属性报错
        // Field name = clazz.getDeclaredField("name"); // 获取父类private属性报错
        // 获取属性
        String catCatType = (String) catType.get(cat);
        String catNickName = (String) nickName.get(cat);
        Date catBirth = (Date) birth.get(cat);
        System.out.println("cat.catType: " + catCatType);
        System.out.println("cat.nickName: " + catNickName);
        System.out.println("cat.birth: " + catBirth);
        // 设置属性
        catType.set(cat, "狸花猫");
        nickName.set(cat, "九万");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        birth.set(cat, dateFormat.parse("2023-01-01"));
        System.out.println("cat.catType: " + cat.getCatType());
        System.out.println("cat.nickName: " + cat.getNickName());
        System.out.println("cat.birth: " + cat.getBirth());
    }
```
```text
getField: 
    获取类的public属性，
    包括继承自父类的public属性
getDeclareField: 
    获取类的public、protected、default、private属性，
    操作private属性时，加上setAccessible(true);
    不包括继承自父类的属性
```
#### 通过反射来操作方法
```java
    public static void testOperateMethods() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class clazz = Cat.class;
        Constructor constructor = clazz.getDeclaredConstructor(String.class, Integer.class, BigDecimal.class, String.class, String.class, String.class, Date.class);
        Cat cat = (Cat) constructor.newInstance("猫", 8, new BigDecimal(10.2), "哺乳类", "蓝白", "九饼", new Date());
        // 获取Method
        Method eat = clazz.getDeclaredMethod("eat");
        eat.invoke(cat);

        // Method sleep = clazz.getDeclaredMethod("sleep"); // 报错，获取不到操作父类的方法
        // sleep.invoke(cat);

         Method sleep = clazz.getMethod("sleep");
         sleep.invoke(cat);

        Method clamp = clazz.getDeclaredMethod("clamp");
        clamp.setAccessible(true);
        clamp.invoke(cat);
    }
```
```text
getMethod: 
    获取类的public方法，
    包括继承自父类的public方法
getDeclareMethod: 
    获取类的public、protected、default、private方法，
    调用private方法时，加上setAccessible(true);
    不包括继承自父类的方法
```
