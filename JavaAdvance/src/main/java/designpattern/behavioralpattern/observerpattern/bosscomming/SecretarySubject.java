package designpattern.behavioralpattern.observerpattern.bosscomming;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SecretarySubject implements Subject {

    private List<Observer> observerList = new ArrayList<Observer>();

    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    public void delObserver(Observer observer) {
        observerList.remove(observer);
    }

    public void advice() {
        for (Observer observer : observerList) {
            observer.update();
        }
    }
}
