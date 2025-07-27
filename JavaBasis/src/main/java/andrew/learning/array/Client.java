package andrew.learning.array;

import java.util.Iterator;

public class Client {
    public static void main(String[] args) {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(3);
        myArrayList.forEach2(element -> System.out.println(element));

        myArrayList.add(4);
        Iterator<Integer> iterator = myArrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        myArrayList.add(5);
        myArrayList.stream().forEach(element -> System.out.println(element));
    }
}
