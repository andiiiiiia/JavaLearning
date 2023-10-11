## 反射
https://zhuanlan.zhihu.com/p/405325823
### 原理
### 源码
### 使用
#### 获取Class对象的方式
```java
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
```
```java
// 1. 获取Class对象的方式
// 方式1
Class clazz1 = Cat.class;
// 方式2
Class<?> clazz2 = Class.forName("andrew.learning.reflectLearning.Cat");
// 方式3
Cat cat3 = new Cat();
Class<? extends Cat> clazz3 = cat3.getClass();
```
#### 通过Class对象创建类对象
```java
// 2. 通过Class对象创建类对象
// 方式1 通过class的Constructor.newInstance()
Constructor constructor = clazz1.getDeclaredConstructor();
Object cat1 = constructor.newInstance();
// 方式2 通过class.newInstance()
Object cat2 = clazz2.newInstance();
```
#### 通过反射来操作属性
```text
getField: 
    获取类的public属性，
    包括继承自父类的public属性
getDeclareField: 
    获取类的public、protected、default、private属性，
    不包括继承自父类的属性
```
```java
// 3. 通过反射来操作属性
// 获取属性定义
Field type = clazz3.getField("type");
Field name = clazz3.getDeclaredField("name");
Field birth = clazz3.getDeclaredField("birth");
// 获取属性
String cat3Type = (String)type.get(cat3);
String cat3Name = (String)name.get(cat3);
Date cat3Birth = (Date)birth.get(cat3);
System.out.println("cat3.type: "+cat3Type);
System.out.println("cat3.name: "+cat3Name);
System.out.println("cat3.birth: "+cat3Birth);
// 设置属性
type.set(cat1, "中华田园犬");
type.set(cat2, "英短-蓝白");
name.set(cat1, "九万");
name.set(cat2, "九饼");
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
birth.set(cat1, dateFormat.parse("2023-01-01"));
birth.set(cat2, dateFormat.parse("2023-02-01"));
System.out.println("cat1\n" + cat1);
System.out.println("cat2\n" + cat2);
```
注意：
```
    私有属性无法设值
```
![](.RDM_images/ea5440ec.png)
#### 通过反射来操作方法
```
getMethod: 
    获取类的public方法，
    包括继承自父类的public方法
getDeclareMethod: 
    获取类的public、protected、default、private方法，
    不包括继承自父类的方法
    包括继承自父类的public方法
```
```
// 4. 通过反射来操作方法
// 获取方法
Method run = clazz3.getDeclaredMethod("run");
Method attack = clazz3.getDeclaredMethod("attack");
// 调用方法
run.invoke(cat1);
run.invoke(cat2);
run.invoke(cat3);
attack.invoke(cat1);
attack.invoke(cat2);
attack.invoke(cat3);
```
注意
```
    无法invoke私有方法
```
![](.RDM_images/df1ecdd0.png)
