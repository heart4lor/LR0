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
        this.first = line.split("->")[0];
        if(line.length() == 3)
            this.second = "";
        else
            this.second = line.split("->")[1];
    }

    Grammar(String first, String second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return this.first + "->" + this.second;
    }
}
