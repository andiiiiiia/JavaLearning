package designpattern.behavioralpattern.observerpattern.bosscomming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public abstract class Observer {
    private String name;
    protected abstract void update();
}
