package designpattern.behavioralpattern.observerpattern.bosscomming;

public interface Subject {
    void addObserver(Observer observer);
    void delObserver(Observer observer);
    void advice();
}
