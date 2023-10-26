package designpattern.behavioralpattern.observerpattern.bosscomming_event;

import java.util.Date;

public class NbaObserver {
    private String name;

    public NbaObserver(String name) {
        this.name = name;
    }

    protected void closeNba(Date time) {
        System.out.println(this.name + "北京时间:" + time + ", 停止看NBA，继续工作！");
    }
}
