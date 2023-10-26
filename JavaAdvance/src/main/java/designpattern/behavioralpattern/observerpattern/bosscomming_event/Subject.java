package designpattern.behavioralpattern.observerpattern.bosscomming_event;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;

@Setter
@Getter
public abstract class Subject {

    private MyEventHandler myEventHandler = new MyEventHandler();

    abstract void advice() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    abstract void addListener(Object object, String method, Object... args);

    abstract void removeListener(Object object, String method, Object... args);
}
