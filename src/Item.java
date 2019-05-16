import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Item extends Grammar implements Cloneable {
    private int dot;

    private Item(String line, int dot) {
        super(line);
        this.dot = dot;
    }

    private Item(Grammar grammar, int dot) {
        super(grammar.toString());
        this.dot = dot;
    }

    Item(String first, String second, int dot) {
        super(first, second);
        this.dot = dot;
    }

    private boolean nextDot() {
        if(dot == getSecond().length()+1)
            return false;
        dot++;
        return true;
    }

    private ArrayList<Item> getClosure(){
        ArrayList<Item> items = new ArrayList<>();
        Queue<Item> queue = new LinkedList<>();
        queue.add(this);
        while(!queue.isEmpty())
        {
            Item now = queue.poll();
            items.add(now);
            if(now.dot == now.getSecond().length())
                continue;
            char c = now.getSecond().charAt(now.dot);
            if(Character.isUpperCase(c))
            {
                Item next = null;
                try {
                    next = (Item) now.clone();
                    if(next.nextDot())
                        queue.offer(next);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        return items;
    }

    @Override
    public String toString() {
        String second = super.getSecond();
        StringBuilder stringBuilder = new StringBuilder(second);
        stringBuilder.insert(dot, ".");
        second = stringBuilder.toString();
        return super.getFirst() + "->" + second;
    }

    public static void main(String[] args) {
        Item item = new Item("S->BB", 0);
        System.out.println(item.getClosure());
//        Grammar grammar = new Grammar("S->BB");
//        for(int i = 0; i < grammar.getSecond().length()+1; i++)
//            System.out.println(new Item(grammar, i));
    }
}
