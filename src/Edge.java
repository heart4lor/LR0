class Edge {

    private int from;
    private int to;
    private char path;

    Edge(int from, int to, char path) {
        this.from = from;
        this.to = to;
        this.path = path;
    }

    int getFrom() {
        return from;
    }

    int getTo() {
        return to;
    }

    char getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format("I%d--%c--I%d", from, path, to);
    }
}
