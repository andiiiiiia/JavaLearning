package designpattern.behavioralpattern.observerpattern.bosscomming_event;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Setter
@Getter
public class Event {
    private Object obj;
    private String methodName;
    private Object[] params;
    private Class[] paramsClass;

    public Event(Object object, String method, Object... args) {
        this.obj = object;
        this.methodName = method;
        this.params = args;
        iniParamClass();
    }

    private void iniParamClass() {
        this.paramsClass = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            Class paramClass = params[i].getClass();
            paramsClass[i] = paramClass;
        }
    }

    public void invoke() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = obj.getClass();
        Method declaredMethod = clazz.getDeclaredMethod(this.methodName, this.paramsClass);
        declaredMethod.setAccessible(true);
        declaredMethod.invoke(this.obj, this.params);
    }
}
