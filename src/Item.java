import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Item extends Grammar implements Cloneable {
    private int dot;

    Item(String line, int dot) {
        super(line);
        this.dot = dot;
    }

    Item(Grammar grammar, int dot) {
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

    int getDot() {
        return dot;
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
        Grammar grammar = new Grammar("S->BB");
        for(int i = 0; i < grammar.getSecond().length()+1; i++)
            System.out.println(new Item(grammar, i));
    }
}
