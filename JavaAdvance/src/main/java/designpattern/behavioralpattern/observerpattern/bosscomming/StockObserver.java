package designpattern.behavioralpattern.observerpattern.bosscomming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StockObserver extends Observer {
    public StockObserver(String name) {
        super(name);
    }

    protected void update() {
        System.out.println("停止看彩票，继续工作！");
    }
}
