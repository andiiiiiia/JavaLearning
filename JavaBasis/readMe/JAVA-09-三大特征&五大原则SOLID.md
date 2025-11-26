# 1. 封装
## 1.1 是什么
将对象的属性和行为（方法）包装在一起，并对外隐藏实现的细节，只暴露必要的接口供外部访问。  
## 1.2 为什么需要封装？有什么作用
1、隐藏实现细节：外部无法直接访问对象的内部数据，只能通过定义好的方法来操作。  
2、提高安全性：防止外部对对象的非法操作或数据的随意修改。  
3、增强代码的可维护性：如果内部实现发生变化，只要接口不变，外部调用不受影响。  
4、提高代码的复用性：封装好的类可以被多个程序或模块重复使用。  
## 1.3 使用场景/方式
### 1.3.1 实现手段
1、访问修饰符  
private：本类  
default（可省略）：本类、同一个包内的类  
protected：本类、同一个包内的类、子类  
public：任意类  
2、提供公共的 getter 和 setter 方法，用于访问和修改私有字段。   
### 1.3.2 举例：
实体类  
```java
public class Person {
    // 将字段设为 private，实现封装
    private String name;
    private int age;

    // 提供 public 的 getter 方法
    public String getName() {
        return name;
    }

    // 提供 public 的 setter 方法
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        } else {
            System.out.println("年龄不能为负数");
        }
    }
}
```
# 2. 继承
## 2.1 是什么
继承是指一个类（子类）可以继承另一个类（父类）的属性和方法。  
## 2.2 为什么引入继承？有什么用
1、代码复用：子类可以直接使用父类中定义的属性和方法，避免重复编写代码。  
2、建立类之间的关系：通过继承可以表达“is-a”关系，例如“狗是动物”。  
3、提高代码的可维护性和扩展性：父类的修改可以影响所有子类，便于统一管理。  
## 2.3 使用方式/场景  
### 2.3.1 使用方式  
entends 关键字    
```java
class X2 extends X1{
}
```
### 2.3.2 使用注意事项：    
**1、Java 不支持多继承**    
**2、构造方法不能被继承**    
使用子类构造方法时，默认会自动调用父类的无参构造方法（也可以用super显示指定父类构造方法）    
```java
public class Parent{

    static {
        System.out.println("parent static code area");
    }

    public Parent() {
        System.out.println("parent constructor");
    }

    public Parent(String string) {
        System.out.println("parent param constructor");
    }
}
```
```java
public class Son extends Parent {
    public Son() { 
        super("str"); // 显示指定父类构造方法
        System.out.println("son constructor");
    }

    public Son(String string) { // 默认会调用父类的无参构造方法
        System.out.println("son param constructor");
    }
}
```
```java
public class Client {
    public static void main(String[] args) {
        Son son = new Son();
        // 输出：
        // parent static code area
        // parent param constructor
        // son constructor
        
        Son son = new Son("str");
        // 输出：
        // parent constructor
        // son param constructor
    }
}
```
**3、private成员和方法不能被继承**  
```java
public class Parent{
    private String prvStr1;

    private final String prvStr2 = "prv_str2";

    private static String prvStr3 = "prv_str3";

    private static final String prvStr4 = "prv_str4";

    public String pubStr1;

    public final String pubStr2 = "pub_str2";

    public static String pubStr3 = "pub_str3";

    public static final String pubStr4 = "pub_str4";

    protected String protectedStr1;

    protected final String protectedStr2 = "protected_str2";

    protected static String protectedStr3 = "protected_str3";

    protected static final String protectedStr4 = "protected_str4";

    String defaultStr1;

    final String defaultStr2 = "default_str2";

    static String defaultStr3 = "default_str3";

    static final String defaultStr4 = "default_str4";

    public Parent() {
    }

    public Parent(String prvStr1) {
        this.prvStr1 = prvStr1;
    }
}
```
```java
public class Son extends Parent {
}
```
```java
public class Client {
    public static void main(String[] args) {
        Son son = new Son();

        // System.out.println(son.prvStr1); // 编译报错
        // System.out.println(son.prvStr2); // 编译报错
        // System.out.println(son.prvStr3); // 编译报错
        // System.out.println(son.prvStr4); // 编译报错

        System.out.println(son.pubStr1);
        System.out.println(son.pubStr2);
        System.out.println(son.pubStr3);
        System.out.println(son.pubStr4);

        System.out.println(son.protectedStr1);
        System.out.println(son.protectedStr2);
        System.out.println(son.protectedStr3);
        System.out.println(son.protectedStr4);

        System.out.println(son.defaultStr1);
        System.out.println(son.defaultStr2);
        System.out.println(son.defaultStr3);
        System.out.println(son.defaultStr4);
    }
}
```
**子类可以重写父类的方法**  
**final类不能被继承**  
# 3. 多态
## 3.1 是什么
多态是指同一个方法调用，根据对象的不同，表现出不同的行为。  

## 3.2 为什么引入多态？有什么用
提高代码的可扩展性：可以轻松添加新的子类，而不需要修改已有的代码。  
提高代码的复用性：通过统一的接口处理不同的对象。  
简化程序设计：可以使用统一的接口处理多种对象，使代码更简洁。  

## 3.3 使用方式/场景/注意事项
### 3.3.1 如何实现
方法重写（Override）：子类重写父类的方法。  
父类引用指向子类对象：这是实现多态的关键。  
### 3.3.2 注意事项
**父类引用只能调用父类中定义的方法和属性，不能调用子类的专属方法和属性**  
如果子类有额外的方法，父类引用是不能直接调用的。    

**多态只适用于引用类型，不适用于基本数据类型**  
多态是面向对象的特性，只适用于类和接口。  

**static 静态方法不能实现多态**  
静态方法在编译时就已经确定，不会根据对象的实际类型动态绑定。  

**final 方法不能被重写，因此不能参与多态**  
如果一个方法被声明为 final，则不能被子类重写，也就不能实现多态。  

**constructor 构造方法不能被重写，因此不能参与多态**  
构造方法是类的特殊方法，不能被继承或重写。  

**接口也可以实现多态**  
Java 中的接口也可以作为多态的实现方式，通过接口引用指向实现类的对象。  

### 3.3.3 示例
```java
interface Animal {
    void makeSound();
}

class Dog implements Animal {
    public void makeSound() {
        System.out.println("汪汪汪");
    }
}

class Cat implements Animal {
    public void makeSound() {
        System.out.println("喵喵喵");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal a1 = new Dog();
        Animal a2 = new Cat();

        a1.makeSound();  // 汪汪汪
        a2.makeSound();  // 喵喵喵
    }
}
```
# 4. SRP
单一职责原则（Single Responsibility Principle，SRP）    
## 4.1 作用
降低耦合：职责分离后，类之间的依赖更少。   
提高可维护性：修改一个功能时，不会影响到其他功能。   
提高可测试性：每个类只做一件事，测试起来更简单。   
## 4.2 注意
如果你在开发中发现一个类做了太多事情，那它很可能违反了单一职责原则，应该考虑重构。   
# 5. OCP
开闭原则（Open-Closed Principle，OCP）   
## 5.1 是什么
软件实体（类、模块、函数等）应该对扩展开放，对修改关闭。   
对扩展开放：允许通过添加新代码来扩展功能。   
对修改关闭：不修改已有代码，避免引入新的错误或破坏已有功能。   
## 5.2 实现方式
多态   
使用**接口或抽象类**，定义统一的行为规范。   
通过继承或实现接口，扩展具体行为。   
使用策略模式、工厂模式、模板方法模式等**设计模式**。   
## 5.3 作用
系统可拓展性   
代码复用性   
降低代码维护成本，提高稳定性   
## 5.4 反例
```java
public class AreaCalculator {
    public double calculateArea(Shape shape) {
        if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            return rectangle.getWidth() * rectangle.getHeight();
        } else if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return Math.PI * circle.getRadius() * circle.getRadius();
        }
        throw new IllegalArgumentException("不支持的形状");
    }
}
```
## 5.5 正例
```java
// 定义一个形状接口
public interface Shape {
    double getArea();
}
```
```java
// 矩形类
public class Rectangle implements Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }
}
```
```java
// 圆形类
public class Circle implements Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}
```
```java
// 新增三角形类，无需修改 AreaCalculator
public class Triangle implements Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double getArea() {
        return 0.5 * base * height;
    }
}
```
```java
// 面积计算器类，无需修改
public class AreaCalculator {
    public double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream()
                     .mapToDouble(Shape::getArea)
                     .sum();
    }
}
```
# 6. LSP
李氏替换原则（Liskov Substitution Principle，LSP）    
## 6.1 是什么  
子类应该可以替换其父类，并且替换后的行为不会破坏程序的正确性。   
  
## 6.2 注意   
子类必须完全兼容父类的行为。  
不能破坏父类的契约（contract），即不能改变父类定义的行为逻辑。子类可以扩展功能，但**不能改变父类已有的行为语义**。    
行为不一致时，考虑使用接口或组合代替继承。  

## 6.3 实现方式  
子类不要重写父类的方法，除非你**完全理解其行为语义。**    
**子类重写方法时，不能改变方法的前置条件（precondition）和后置条件（postcondition）**。   
子类不能抛出父类方法中没有声明的异常。   
子类不能返回比父类更具体的类型（除非是协变返回）。   
子类不能破坏父类的不变性（invariant）。   
## 6.4 反例  
```java
// 父类：Bird
public class Bird {
    public void fly() {
        System.out.println("鸟在飞翔");
    }
}
```
```java
// 子类：Penguin（企鹅）
public class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("企鹅不会飞");
    }
}
```
```java
public class BirdFlock {
    public void makeAllBirdsFly(List<Bird> birds) {
        for (Bird bird : birds) {
            bird.fly();
        }
    }
}
```
说明，fly()方法不是所有鸟类的共性，不应该放在父类中，可以作为接口  
## 6.5 正例  
```java
// 定义一个通用的动物接口
public interface Animal {
    void move();
}
```
```java
// 飞行行为接口
public interface Flyable {
    void fly();
}
```
```java
// 鸟类实现 Animal 接口，并实现 Flyable 接口
public class Bird implements Animal, Flyable {
    @Override
    public void move() {
        fly();
    }

    @Override
    public void fly() {
        System.out.println("鸟在飞翔");
    }
}
```
```java
// 企鹅实现 Animal 接口，但不实现 Flyable 接口
public class Penguin implements Animal {
    @Override
    public void move() {
        System.out.println("企鹅在游泳");
    }
}
```
```java
public class AnimalGroup {
    public void makeAllMove(List<Animal> animals) {
        for (Animal animal : animals) {
            animal.move();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Bird());
        animals.add(new Penguin());

        AnimalGroup group = new AnimalGroup();
        group.makeAllMove(animals);
    }
}
```
# 7. ISP
接口隔离原则（Interface Segregation Principle，ISP）   
## 7.1 是什么  
客户端不应该依赖它不需要的接口。  
## 7.2 实现方式
将大接口拆分成多个小接口，每个接口只定义一组相关的方法。  
使用组合代替继承，通过组合多个接口来实现复杂行为。  
根据客户端的需求定义接口，而不是为所有客户端设计一个通用接口。  
## 7.3 作用
提高代码的可维护性：接口职责清晰，修改一个接口不会影响其他客户端  
提高代码的可扩展性：可以灵活组合多个接口，适应不同需求  
降低类之间的耦合：客户端只依赖它需要的接口，不依赖不需要的方法  
提高代码的可测试性：接口职责单一，更容易进行单元测试  
## 7.4 注意事项  
**接口应该小而专一，每个接口只定义一组相关的操作。但是，注意也不要过度拆分接口，导致接口数量过多，增加复杂度。**    
客户端只依赖它需要的接口，而不是一个大而全的接口。接口应该根据职责划分，而不是根据客户端数量。  
避免“胖接口”，即一个接口中包含太多不相关的方法。  
## 7.5 反例  
```java
// 一个大而全的接口
public interface Machine {
    void print(String content);
    void scan(String content);
    void fax(String content);
}

// 打印机类，实现了所有方法
public class Printer implements Machine {
    @Override
    public void print(String content) {
        System.out.println("打印内容: " + content);
    }

    @Override
    public void scan(String content) {
        System.out.println("扫描内容: " + content);
    }

    @Override
    public void fax(String content) {
        System.out.println("传真内容: " + content);
    }
}

// 旧式打印机，只能打印，但必须实现所有方法
public class OldPrinter implements Machine {
    @Override
    public void print(String content) {
        System.out.println("打印内容: " + content);
    }

    @Override
    public void scan(String content) {
        throw new UnsupportedOperationException("不支持扫描");
    }

    @Override
    public void fax(String content) {
        throw new UnsupportedOperationException("不支持传真");
    }
}
```
老款打印机无法实现一些新兴的打印机功能   
## 7.6 正例
```java
// 打印接口
public interface Printer {
    void print(String content);
}

// 扫描接口
public interface Scanner {
    void scan(String content);
}

// 传真接口
public interface FaxMachine {
    void fax(String content);
}

// 多功能打印机，实现多个接口
public class MultiFunctionPrinter implements Printer, Scanner, FaxMachine {
    @Override
    public void print(String content) {
        System.out.println("打印内容: " + content);
    }

    @Override
    public void scan(String content) {
        System.out.println("扫描内容: " + content);
    }

    @Override
    public void fax(String content) {
        System.out.println("传真内容: " + content);
    }
}

// 旧式打印机，只实现打印接口
public class OldPrinter implements Printer {
    @Override
    public void print(String content) {
        System.out.println("打印内容: " + content);
    }
}
```
# 8. DIP
依赖倒置原则（Dependency Inversion Principle，DIP）  
## 8.1 是什么
**高层模块不应该依赖低层模块，二者都应该依赖于抽象。抽象不应该依赖细节，细节应该依赖抽象。**  
## 8.2 实现方式  
使用接口或抽象类，定义抽象行为。  
高层模块依赖接口，而不是具体实现。  
通过依赖注入（DI），将具体实现注入到高层模块中。  
使用工厂模式、策略模式、IoC 容器等设计模式或框架来管理依赖。  
## 8.3 作用  
降低模块之间的耦合	高层模块不依赖具体实现，提高可维护性   
提高代码的可扩展性	可以轻松替换具体实现，不影响高层逻辑   
支持单元测试	可以通过注入 mock 对象进行测试   
提高代码的复用性	抽象接口可以被多个具体实现复用   
## 8.4 注意  
不要滥用接口，接口应该有明确的职责。   
依赖注入要合理使用，避免过度设计。  
接口和实现要分离，但不要过度抽象，增加复杂度。  
依赖倒置不是万能的，要根据项目规模和需求合理使用。  
## 8.5 反例  
```java
// 低层模块：数据库操作
public class Database {
    public void save(String data) {
        System.out.println("保存数据到数据库: " + data);
    }
}

// 高层模块：业务逻辑
public class UserService {
    private Database database = new Database();

    public void registerUser(String username) {
        System.out.println("注册用户: " + username);
        database.save(username);
    }
}
```
如果不是存储database，而是存储到file中，则需要修改userService     
## 8.6 正例  
```java
// 定义抽象接口
public interface DataStorage {
    void save(String data);
}

// 具体实现：数据库存储
public class Database implements DataStorage {
    @Override
    public void save(String data) {
        System.out.println("保存数据到数据库: " + data);
    }
}

// 具体实现：文件存储
public class FileStorage implements DataStorage {
    @Override
    public void save(String data) {
        System.out.println("保存数据到文件: " + data);
    }
}
```
```java
// 高层模块：依赖接口，不依赖具体实现
public class UserService {
    private DataStorage storage;

    // 通过构造函数注入依赖
    public UserService(DataStorage storage) {
        this.storage = storage;
    }

    public void registerUser(String username) {
        System.out.println("注册用户: " + username);
        storage.save(username);
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        // 使用数据库存储
        DataStorage dbStorage = new Database();
        UserService userService1 = new UserService(dbStorage);
        userService1.registerUser("Alice");

        // 使用文件存储
        DataStorage fileStorage = new FileStorage();
        UserService userService2 = new UserService(fileStorage);
        userService2.registerUser("Bob");
    }
}
```
