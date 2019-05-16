public class Grammar {
    private String first;
    private String second;

    String getFirst() {
        return first;
    }

    String getSecond() {
        return second;
    }

    Grammar(String line) {
        String regex = line.contains("->") ? "->" : "→";
        this.first = line.split(regex)[0];
        this.second = line.split(regex)[1];
    }

    Grammar(String first, String second) {
        this.first = first;
        this.second = second;
    }

    private Item[] getItems() {
        int n = second.length() + 1;
        Item[] items = new Item[n];
        for(int i = 0; i < n; i++)
            items[i] = new Item(first, second, i);
        return items;
    }

    @Override
    public String toString() {
        return this.first + "->" + this.second;
    }

    public static void main(String[] args) {
        Grammar grammar = new Grammar("S->BB");
        Item[] items = grammar.getItems();
        for(Item item: items) {
            System.out.println(item);
        }
    }
}
