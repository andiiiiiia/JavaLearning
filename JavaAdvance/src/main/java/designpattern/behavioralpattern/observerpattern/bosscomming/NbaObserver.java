package designpattern.behavioralpattern.observerpattern.bosscomming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class NbaObserver extends Observer {
    public NbaObserver(String name) {
        super(name);
    }

    protected void update() {
        System.out.println("停止看NBA，继续工作！");
    }
}
