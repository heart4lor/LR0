public class Item extends Grammar implements Cloneable {
    private int dot;
    private int belong;

    Item(String line, int dot) {
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

    boolean hasNextDot() {
        return dot < getSecond().length();
    }

    Item nextDot() {
        Item next = null;
        try {
            next = (Item) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assert next != null;
        next.dot++;
        return next;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Item)
            return this.toString().equals(object.toString());
        return false;
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
