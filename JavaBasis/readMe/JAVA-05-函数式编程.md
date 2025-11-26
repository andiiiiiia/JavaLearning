# 匿名内部类
函数式编程的lambda表达式就是对特殊匿名内部类（只有一个抽象方法的接口，又称为函数式接口）的简化  
## 什么是匿名内部类
1）当实现接口或者抽象类或父类时，没有显式指定类名的实现类（编译时JAVA默认会生成一个）     
2）定义语法：  
```java
new XXXInterface/XXXAbstractClass/XXXParentClass(){
  // 实现抽象方法或者重写父类方法
}
```
3）在定义匿名内部类时就会实例化一个对象，适用于只需使用一次的类。     
## 举例
1、实现接口  
```java
public interface Swim {
    void swimming();

    String getName();
}

public class SwimTest {
    public static void main(String[] args) {
        start(new Swim() {
            @Override
            public void swimming() {
                System.out.println("start swim");
            }

            @Override
            public String getName() {
                return "stu";
            }
        });

        start(new Swim() {
            @Override
            public void swimming() {
                System.out.println("start swim");
            }

            @Override
            public String getName() {
                return "teacher";
            }
        });
    }

    public static void start(Swim s) {
        System.out.println(s.getName()+":");
        s.swimming();
    }
}
```
编译后的字节码文件目录：    
**图片**  
  
反编译：  
**图片**  
注意： 是final修饰的不可变类    
  
2、继承父类
```java
public  class Animal {
     void eat(){
         System.out.println("动物吃东西");
     }
}
```
```java
public class AnimalTest {
    public static void main(String[] args) {
        Animal a = new Animal() {
            public void eat() {
                System.out.println("猫吃东西");
            }
        };
        a.eat();
    }
}
```
# 函数式接口
## 是什么？
只有一个抽象方法的接口，可以使用@FunctionalInterface注解标注。eg：Runable    

## 使用lambda表达式简化函数式接口的匿名内部类的定义
1、简化规则：  
匿名内部类写法：new XXXInterface() { @Override 方法定义+方法的实现代码};    
lambda简化写法：    
抽象方法：无参，一行实现代码。简化为：()-> 方法实现代码;    
抽象方法：无参，多行实现代码。简化为：()->{方法的实现代码};      
抽象方法：一个参，一行实现代码。简化为：参-> 方法实现代码;    
抽象方法：一个参，多行实现代码。简化为：参->{方法实现代码};    
抽象方法：多个参，一行实现代码。简化为：(参数列表)-> 方法实现代码;  
抽象方法：多个参，多行实现代码。简化为：(参数列表)-> {方法实现代码};    

PS: 在方法实现代码只有一行时，若有return语句，则return关键字可以省略。  
  
eg:  Rubable，run方法无参，且我的实现只有一行代码。  
```java
public class RunnableTest {
    public static void main(String[] args) {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() { // 方法无参
                System.out.println("thread1"); // 只有一行实现代码
            }
        };

        // 简化定义：
        Runnable runnable2 = () -> {
            System.out.println("thread2");
        };

        // 最终简化定义为：
        Runnable runnable3 = () -> System.out.println("thread3");

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);
    }
}
```

## lambda简化后的表达式，和匿名内部类一样，可以作为参数传递给另一个方法或者赋值给另一个变量。
eg:lambda表达式作为参数传递。接上一个例子，最后的thread的构造可以写为：  
```java
Thread thread4 = new Thread(()-> System.out.println("thread4"));
```
eg:赋值给另一个变量。参考上述例子  
```java
Runnable runnable3 = () -> System.out.println("thread3");
```

## 内置的函数式接口
### Function
源码：
```java
public interface Function<T, R> {
  
    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
  
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
```
eg:
```java
public class FunctionTest {
    public static void main(String[] args) {
        Function<Integer, Integer> fun1 = x -> x + 1;
        Function<Integer, Integer> fun2 = x -> x * x;
        Function<Integer, Integer> fun3 = fun1.andThen(fun2); // 先执行fun1，再执行fun2
        Function<Integer, Integer> fun4 = fun1.compose(fun2); // 先执行fun2，再执行fun1

        System.out.println(fun3.apply(3));
        // 输出：16

        System.out.println(fun4.apply(3));
        // 输出：10

        System.out.println(Function.identity().apply(3));
        // 输出：3
    }
}
```
### Customer
源码：  
```java
@FunctionalInterface
public interface Consumer<T> {
  
    void accept(T t);

    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```
eg:
```java
public class ConsumerTest {
    public static void main(String[] args) {
        Consumer<Integer> consumer1 = (Integer x) -> System.out.println(x + " + 1 = " + (x + 1));
        Consumer<Integer> consumer2 = x -> {
            int y = x * x;
            System.out.println(x + " * " + x + " = " + y);
        };
        Consumer<Integer> consumer3 = consumer1.andThen(consumer2);
        consumer3.accept(5);
        // 输出：
        // 5 + 1 = 6
        // 5 * 5 = 25
    }
}
```

# 方法引用
某些场景下，对Lambda 表达式的继续简化  
## 静态方法引用，简化为：ClassName::staticMethod

条件：实现方法里只能有一行，类.静态方法  

**正例：**
```java
public class StaticMethodTest {
    public static void main(String[] args) {
        // 匿名内部类
        Function<Integer, String> function1 = new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return String.valueOf(integer);  // 实现方法
            }
        };
        String apply1 = function1.apply(111);
        System.out.println(apply1);

        // lambda表达式简化
        Function<Integer, String> function2 = integer -> String.valueOf(integer);
        String apply2 = function2.apply(222);
        System.out.println(apply2);

        // 方法引用简化
        Function<Integer, String> function3 = String::valueOf;
        String apply3 = function2.apply(333);
        System.out.println(apply3);
    }
}
```

## 特定对象的实例方法引用，简化为：object::instanceMethod
条件：  
**正例**
```java
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void sayHello(String otherName) {
        System.out.println(name + "在向" + otherName + "问好");
    }
}
```

```java
public class PersonTest {
    public static void main(String[] args) {
        // 创建一个 Person 对象
        Person person = new Person("张三");

        // 使用匿名内部类
        Consumer<String> greet1 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                person.sayHello(s);
            }
        };
        greet1.accept("李四");
        // 输出：张三在向李四问好

        // 使用 Lambda 表达式
        Consumer<String> greet2 = name -> person.sayHello(name);
        greet2.accept("李四");
        // 输出：张三在向李四问好

        // 使用方法引用（引用特定对象的实例方法）
        Consumer<String> greet3 = person::sayHello;
        greet3.accept("李四");
        // 输出：张三在向李四问好
    }
}
```

## 任意对象的实例方法引用，简化为：className::instanceMethod

条件：实例方法，参数类型必须匹配。实现方法只有一行，且是对象.实例方法  

**正例：**
```java
public class AnyInstanceMethodTest {
    public static void main(String[] args) {
        // 匿名内部类
        Function<String,Integer> function1 = new Function<String, Integer>() {
            @Override
            public Integer apply(String string) {
                return string.length(); // 实现方法
            }
        };
        System.out.println(function1.apply("hello"));

        // lambda表达式简化
        Function<String,Integer> function2 = string -> string.length();
        System.out.println(function2.apply("hello"));

        // 方法引用简化
        Function<String,Integer> function3 = String::length;
        System.out.println(function3.apply("hello"));
    }
}
```

## 构造方法引用，简化为：ClassName::new

条件：构造方法必须存在，且参数类型必须匹配  

**正例：**
```java
public class ConstructorMethodTest {
    public static void main(String[] args) {
        // 匿名内部类
        Supplier<String> supplier1 = new Supplier<String>() {
            @Override
            public String get() {
                return new String();
            }
        };
        System.out.println(supplier1.get());

        // lambda表达式
        Supplier<String> supplier2 = () -> new String();
        System.out.println(supplier2.get());

        // 方法引用简化
        Supplier<String> supplier3 = String::new;
        System.out.println(supplier3.get());
    }
}
```
