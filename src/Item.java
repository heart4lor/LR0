public class Item extends Grammar implements Cloneable {
    private int dot;
    private int belong;

    Item(String line, int dot, int belong) {
        super(line);
        this.dot = dot;
        this.belong = belong; // 所属grammar
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

    int getBelong() {
        return belong;
    }

}
