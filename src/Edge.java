class Edge {

    private int from;
    private int to;
    private int path;

    Edge(int from, int to, int path) {
        this.from = from;
        this.to = to;
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("%d-%d:%c", from, to, path);
    }
}
