package designpattern.behavioralpattern.observerpattern.bosscomming_event;


import java.util.Date;

public class StockObserver {
    private String name;

    public StockObserver(String name) {
        this.name = name;
    }

    protected void closeStock(Date time) {
        System.out.println(this.name + "北京时间:" + time + ", 停止看彩票，继续工作！");
    }
}
