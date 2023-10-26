package designpattern.behavioralpattern.observerpattern.bosscomming_event;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Subject boss = new BossSubject();
        NbaObserver nbaObserver = new NbaObserver("张三");
        StockObserver stockObserver = new StockObserver("李四");

        boss.addListener(nbaObserver, "closeNba", new Date());
        boss.addListener(stockObserver, "closeStock", new Date());

        boss.advice();
    }
}
