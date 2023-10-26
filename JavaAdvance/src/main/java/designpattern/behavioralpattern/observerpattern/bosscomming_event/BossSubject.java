package designpattern.behavioralpattern.observerpattern.bosscomming_event;

import java.lang.reflect.InvocationTargetException;

public class BossSubject extends Subject {

    public void advice() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("boss is advice seriously!");
        this.getMyEventHandler().update();
    }

    void addListener(Object object, String method, Object... args) {
        System.out.println("someone entrust boss!");
        this.getMyEventHandler().addEvent(object, method, args);
    }

    void removeListener(Object object, String method, Object... args) {
        System.out.println("someone don‘t entrust boss anymore!");
        this.getMyEventHandler().removeEvent(object, method, args);
    }
}
