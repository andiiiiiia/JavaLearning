# 1. 泛型  
## 1.1 what is it?  
泛型的本质是：将类型作为参数传递，让类、接口或方法可以适用于多种类型，而不需要为每种类型都写一份代码。    
通过泛型，我们可以在编译时就检查类型的安全性，避免运行时的类型转换错误，同时提高代码的可重用性和可读性。    
## 1.2 为什么要引入泛型（why use it）?  
### 1.2.1 java5之前不支持泛型导致的问题    
集合类（如 ArrayList）只能存储 Object 类型的对象。    
```java  
List list = new ArrayList();  
list.add("Hello");  
list.add(123); // 也可以添加整数  
String str = (String) list.get(0); // 需要强制类型转换  
```  
类型不安全：可以向集合中添加任意类型的对象。    
需要强制类型转换：从集合中取出元素时，必须进行类型转换，容易出错。    
编译器无法检查类型错误：很多错误只能在运行时发现。    
  
### 1.2.2 引入泛型之后：  
类型安全：在编译时就能发现类型错误。    
无需强制类型转换：提高代码的可读性和安全性。    
代码复用：一个泛型类或方法可以适用于多种类型。    
更好的文档性：通过泛型参数，可以更清晰地表达代码意图。    
  
## 1.3 使用场景/方式(how to use it)?  
### 1.3.1 泛型类  
示例：  
```java  
public class Box<T> {  
    private T item;  
  
    public void setItem(T item) {  
        this.item = item;  
    }  
  
    public T getItem() {  
        return item;  
    }  
}  
```  
```java  
public static void main(String[] args){  
  Box<String> stringBox = new Box<>();  
  stringBox.setItem("Hello");  
  String str = stringBox.getItem(); // 不需要强制转换  
}  
```  
### 1.3.2 泛型接口  
示例：  
```java  
public interface List<T> {  
    void add(T item);  
    T get(int index);  
}  
```  
### 1.3.3 泛型方法  
示例：  
```java  
public <T> void printItem(T item) {  
    System.out.println(item);  
}  
```  
```java  
printItem("Hello");  
printItem(123);  
```  
### 1.3.4 使用限制:  
不能使用基本类型:     
List<int> list = new ArrayList<>(); // 错误  
  
不能创建泛型类型的数组:    
T[] array = new T[10]; // 错误  
  
不能使用 instanceof 检查泛型类型  
if (item instanceof T) { ... } // 错误 泛型擦除，Object  
```java
List<String> sList = new ArrayList<>();
if (sList instanceof List<Integer>) { ... } // 错误 泛型擦除，Object  
```
  

# 2. 泛型擦除  
## 2.1 what is it?  
Java 的泛型是伪泛型，它在编译时会进行类型擦除，即泛型信息在运行时是不可见的。    
  
**1、示例1：**    
编译时擦除泛型信息。    
从泛型集合取出元素时，需要进行强制类型转换      
```java  
public class Client {  
  
    public static void main(String[] args) {  
        List<String> list1 = new ArrayList<>();  
        list1.add("1");  
  
        // 取出元素  
        List<Integer> list2 = new ArrayList<>();  
        list2.add(123);  
        Integer i = list2.get(0);  
  
        // class是否相等  
        System.out.println(list1.getClass() == list2.getClass()); // true  
    }  
}  
```  
执行结果：    
```text  
true  
```  
javac编译后的文件Client.class文件：    
注意：    
< T >泛型信息全部被擦除了。     
var2.get(0)进行了强制类型转换    
```text  
import java.util.ArrayList;  
  
public class Client {  
    public Client() {  
    }  
  
    public static void main(String[] var0) {  
        ArrayList var1 = new ArrayList();  
        var1.add("1");  
        ArrayList var2 = new ArrayList();  
        var2.add(123);  
        Integer var3 = (Integer)var2.get(0);  
        System.out.println(var1.getClass() == var2.getClass());  
    }  
}  
```  
  
**示例2：泛型类**    
自动类型转换    
```java  
public class Box<T> {  
    private T value;  
  
    public Box(T value) {  
        this.value = value;  
    }  
  
    public void set(T value) {  
        this.value = value;  
    }  
  
    public T get() {  
        return value;  
    }  
}  
```  
编译后不会保留T，使用Object类替代,编译后等效于如下Box.class  
```text  
public class Box {  
    private Object value;  
  
    public Box(Object value) {  
        this.value = value;  
    }  
  
    public void set(Object value) {  
        this.value = value;  
    }  
  
    public Object get() {  
        return value;  
    }  
}  
```  
  
  
## 2.2 为什么要有泛型擦除  
1、兼容性    
如果 Java 不采用泛型擦除，而是将泛型信息保留在运行时，那么新版本的 Java 就无法与旧版本的类库和代码兼容。    
    
2、性能优化    
如果不擦除泛型，JVM 会认为 List<String> 和 List<Integer> 是两个不同的类，这会导致：    
类数量爆炸式增长、内存占用增加、类加载变慢    
而泛型擦除后，它们在运行时都变成 List，JVM 不需要为每个泛型类型生成不同的类，从而节省内存和提高性能。    
    
3、简化JVM实现    
JVM 本身并不支持泛型，泛型是** Java 编译器**的特性。    
如果泛型信息在运行时保留，JVM 就需要支持泛型相关的机制，比如泛型类型检查、泛型方法调用等，这会大大增加 JVM 的复杂度。    
通过泛型擦除，Java 编译器在编译阶段就完成了类型检查和转换，JVM 只需要处理普通的类和方法，简化了 JVM 的实现。    
  
4、类型安全可以在编译时保证，而运行时的类型信息并不是必须的。  
## 2.3 使用场景/方式
### 2.3.1 使用限制
**equals方法继承自Object，不允许泛型方法定义**
```java
public class Pair<T> {
    public boolean equals(T t) {
        return this == t; // 编译报错
    }
}
```
  
# 3. 通配符?  
## 3.1 what is it?  
无界通配符：  
< ? >表示可以接受任何类型  
  
上界：  
< ? extends T >表示类型是 T 或其子类  
  
下界：  
< ? super T > 表示类型是 T 或其父类  
## 3.2 为什么要有通配符？  
1、在Java泛型中，类型是不可协变的。    
List< String > 与 List< Object > 之间没有继承关系    
这会导致一些问题，比如：   
```java  
public class Client {  
  public static void main(String[] args) {  
    List<String> stringList = new ArrayList<>();  
    stringList.add("abc");  
    fun(stringList); // 编译报错  
  
    List<Integer> integerList = new ArrayList<>();  
    integerList.add(1);  
    fun(integerList); // 编译报错  
  }  
  
  public static void fun(List<Object> list) {  
    System.out.println(list.size());  
  }  
}  
```   
为了解决这种类型不兼容的问题，Java引入了通配符。    
eg:  
```java  
public class Client {  
  public static void main(String[] args) {  
    List<String> stringList = new ArrayList<>();  
    stringList.add("abc");  
    fun(stringList); // 编译通过  
  
    List<Integer> integerList = new ArrayList<>();  
    integerList.add(1);  
    fun(integerList); // 编译通过  
  }  
  
  public static void fun(List<?> list) {  
    System.out.println(list.size());  
  }  
}  
```  
## 3.3 常见使用场景  
1、作为方法参数，提高泛型方法的通用性    
```java  
public void printList(List<?> list) {  
  for (Object item : list) {  
      System.out.println(item);  
  }  
}  
```  
    
2、 使用上界通配符 ? extends T：读取数据更安全    
```java  
public void processNumbers(List<? extends Number> numbers) {  
  for (Number num : numbers) {  
      System.out.println(num.doubleValue());  
  }  
}  
```  
    
3、 使用下界通配符 ? super T：写入数据更灵活  
```java  
public void addNumbers(List<? super Integer> list) {  
  list.add(10);  
  list.add(20);  
}  
```  
  
# 4. PECS原则(通配符的使用原则)：  
## 4.1 是什么（what is it?）  
Producer Extends  
Consumer Super  
是作用在通配符上的设计原则  
  
处理多个类型之间的兼容问题，在不牺牲类型安全的前提下，提高泛型集合的灵活性和复用性。  
  
## 4.2 为什么要引入这个原则  
在Java泛型中，类型是不可协变的，导致一些泛型方法在设计时，无法灵活地处理子类或父类的集合。  
  
## 4.3 使用场景/方式(how to use it?)  
### 4.3.1 使用前提  
1、使用泛型集合（如 List、Set 等），并且在集合中进行读取或写入操作。  
  
2、PE:     
适用前提：你只需要从集合中读取元素，不需要写入。  
作用：允许集合是 T 或其子类的集合。  
限制：不能向集合中添加元素（除了 null）。  
  
3、CS:  
适用前提：你只需要向集合中写入元素，不需要读取。    
作用：允许集合是 T 或其父类的集合。    
限制：**不能保证**读出的元素类型。注意：如果要读，只能用Object来接。    
  
### 4.3.2 Producer Extends示例：  
**示例1：基本类型**  
```java  
public class PETest {  
    public static void main(String[] args) {  
        List<Integer> integers = new ArrayList<>();  
        integers.add(1);  
        integers.add(3);  
        integers.add(7);  
        printNumbers(integers);  
  
        List<Long> longs = new ArrayList<>();  
        longs.add(137L);  
        longs.add(173L);  
        longs.add(371L);  
        longs.add(317L);  
        longs.add(731L);  
        longs.add(713L);  
        printNumbers(longs);  
    }  
  
    public static void printNumbers(List<? extends Number> list) {  
        // list.add(2L); // 编译报错  
        for (Number number : list) {  
            System.out.println(number);  
        }  
    }  
}  
```  
```text  
输出结果：  
1  
3  
7  
137  
173  
371  
317  
731  
713  
```  

**实例2：自定义类型**
```java  
public class Food {  
    private String name;  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public Food(String name) {  
        this.name = name;  
    }  
}  
```  
```java  
public class Meat extends Food{  
    public Meat(String name) {  
        super(name);  
    }  
}  
```  
```java  
public class Beef extends Meat{  
    public Beef(String name) {  
        super(name);  
    }  
}  
```  
```java  
public class Pork extends Meat{  
    public Pork(String name) {  
        super(name);  
    }  
}  
```  
```java  
public class PETest {  
    public static void main(String[] args) {  
  
        // List<Food> foodPlat = new ArrayList<>();  
        // printMeatPlat(foodPlat); // 报错  
  
        // List<Meat>  
        List<Meat> meatPlat = new ArrayList<>();  
        meatPlat.add(new Meat("meat1"));  
        meatPlat.add(new Beef("beef1"));  
        meatPlat.add(new Pork("pork1"));  
        printMeatPlat(meatPlat);  
  
        // List<Beef>  
        List<Beef> beefPlat = new ArrayList<>();  
        beefPlat.add(new Beef("beef2"));  
        beefPlat.add(new Beef("beef3"));  
        beefPlat.add(new Beef("beef4"));  
        printMeatPlat(beefPlat);  
    }  
  
    public static void printMeatPlat(List<? extends Meat> meatPlat) {  
        for (Meat meat : meatPlat) {  
            System.out.println(meat.getName());  
        }  
        // 可以add，只能add Null  
        meatPlat.add(null);  
    }  
}  
```
输出结果：
```text  
输出：  
meat1  
beef1  
pork1  
beef2  
beef3  
beef4  
```  
### 4.3.3 Consumer Super示例：  
**示例1：使用基本类型对应的泛型类**  
```java  
public class CSTest {  
    public static void main(String[] args) {  
        List<Long> longs = new ArrayList<>();  
        longs.add(137L);  
        addDefaultNumber(longs);  
  
  
        List<Number> numbers = new ArrayList<>();  
        numbers.add(Double.valueOf(1));  
        numbers.add(Float.valueOf(3));  
        numbers.add(Integer.valueOf(7));  
        addDefaultNumber(numbers);  
    }  
  
    public static void addDefaultNumber(List<? super Long> list) {  
        // consumer  
        list.add(731L); // 可以加Long及其子类型  
  
        // 只能用Object接收  
        for (Object object : list) {  
            System.out.println(object);  
            System.out.println(object.getClass());  
        }  
  
        // Long aLong = list.get(0); // 编译报错  
    }  
}  
```  
```text  
137  
class java.lang.Long  
731  
class java.lang.Long  
1.0  
class java.lang.Double  
3.0  
class java.lang.Float  
7  
class java.lang.Integer  
731  
class java.lang.Long  
```  
**示例2：使用自定义类**  
```java  
public class Food {  
    private String name;  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public Food(String name) {  
        this.name = name;  
    }  
}  
```  
```java  
public class Fruit extends Food{  
    public Fruit(String name) {  
        super(name);  
    }  
}  
```  
```java  
public class Apple extends Fruit{  
    public Apple(String name) {  
        super(name);  
    }  
}  
```  
```java  
public class Banana extends Fruit{  
    public Banana(String name) {  
        super(name);  
    }  
}  
```  
```java  
public class CSTest {  
    public static void main(String[] args) {  
        List<Food> foodList = new ArrayList<>();  
        putDefaultFruitPlat(foodList);  
        foodList.add(new Apple("apple2"));  
        foodList.add(new Banana("banana2"));  
        foodList.add(new Fruit("fruit2"));  
        foodList.add(new Food("food2"));  
        for (Food food : foodList) {  
            System.out.println(food.getName());  
        }  
  
        List<Fruit> fruitList = new ArrayList<>();  
        putDefaultFruitPlat(fruitList);  
        fruitList.add(new Apple("apple3"));  
        fruitList.add(new Banana("banana3"));  
        fruitList.add(new Fruit("fruit3"));  
        for (Fruit fruit : fruitList) {  
            System.out.println(fruit.getName());  
        }  
  
        // List<Apple> appleList = new ArrayList<>();  
        // putDefaultFruitPlat(appleList); // 报错  
    }  
  
    public static void putDefaultFruitPlat(List<? super Fruit> fruitList) {  
        // write  
        // fruitList.add(new Food()); // 报错  
        fruitList.add(new Fruit("fruit1"));  
        fruitList.add(new Apple("apple1"));  
        fruitList.add(new Banana("banana1"));  
  
        // read  
        for (Object object : fruitList) {  
            System.out.println(object.getClass());  
        }  
    }  
}  
```  
```text  
输出结果：  
class 泛型.extends和super.Fruit  
class 泛型.extends和super.Apple  
class 泛型.extends和super.Banana  
fruit1  
apple1  
banana1  
apple2  
banana2  
fruit2  
food2  
class 泛型.extends和super.Fruit  
class 泛型.extends和super.Apple  
class 泛型.extends和super.Banana  
fruit1  
apple1  
banana1  
apple3  
banana3  
fruit3  
```  
