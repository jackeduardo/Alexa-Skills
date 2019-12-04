package alexa.skill;

import java.util.ArrayList;
import java.util.List;

public class Shopping_list {
    public List<String> shoppinglist = new ArrayList<>();

    public void Add(String item) {
        shoppinglist.add(item);
    }

    public void Remove(String item) {

        shoppinglist.remove(item);


    }

    public void print_list() {
        if (!shoppinglist.isEmpty()) {
            System.out.print("Current shopping list is: ");
            for (String string : shoppinglist) {
                System.out.print(string + " || ");
            }
        } else {
            System.out.print("Current shopping list is null. ");
        }
        System.out.println();
    }
}
