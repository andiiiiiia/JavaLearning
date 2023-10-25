package designpattern.behavioralpattern.observerpattern.bosscomming;

public class Client {
    public static void main(String[] args) {
        Observer colleagueA = new NbaObserver("zhangsan");
        Observer colleagueB = new NbaObserver("lisi");
        Observer colleagueC = new NbaObserver("wangwu");

        Subject boss = new BossSubject();
        boss.addObserver(colleagueA);
        boss.addObserver(colleagueB);
        boss.addObserver(colleagueC);

        boss.delObserver(colleagueB);

        boss.advice();
    }
}
