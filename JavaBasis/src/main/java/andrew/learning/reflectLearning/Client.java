package andrew.learning.reflectLearning;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, ParseException, NoSuchFieldException {
        // 1. 获取Class对象的方式
        // 方式1
        Class clazz1 = Cat.class;
        // 方式2
        Class<?> clazz2 = Class.forName("andrew.learning.reflectLearning.Cat");
        // 方式3
        Cat cat3 = new Cat();
        cat3.setType("英短-金渐层");
        cat3.setName("九筒");
        cat3.setBirth(new SimpleDateFormat("yyyy-mm-dd").parse("2023-03-01"));
        Class<? extends Cat> clazz3 = cat3.getClass();

        // 2. 通过Class对象创建类对象
        // 方式1 通过class的Constructor.newInstance()
        Constructor constructor = clazz1.getDeclaredConstructor();
        Object cat1 = constructor.newInstance();
        // 方式2 通过class.newInstance()
        Object cat2 = clazz2.newInstance();

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

        // 4. 通过反射来操作方法
        // 获取方法定义
        Method run = clazz3.getMethod("run");
        Method attack = clazz3.getDeclaredMethod("attack");
        // 调用方法
        run.invoke(cat1);
        run.invoke(cat2);
        run.invoke(cat3);
        attack.invoke(cat1);
        attack.invoke(cat2);
        attack.invoke(cat3);

    }
}
