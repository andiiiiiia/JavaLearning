package designpattern.behavioralpattern.observerpattern.bosscomming_event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MyEventHandler {
    List<Event> eventList = new ArrayList<Event>();

    public void update() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Event event : eventList) {
            event.invoke();
        }
    }

    public void addEvent(Object object, String method, Object... args) {
        eventList.add(new Event(object, method, args));
    }

    public void removeEvent(Object object, String method, Object... args) {

        eventList.remove(new Event(object, method, args));
    }
}
