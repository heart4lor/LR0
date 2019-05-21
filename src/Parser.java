import java.util.*;

public class Parser {
    private static HashMap<String, ArrayList<Integer>> index = new HashMap<>();
    private static Grammar[] grammars = new Grammar[1024];

    private static void input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入文法的数目：");
        int n = scanner.nextInt();
        scanner.nextLine();
        for(int i = 1; i <= n; i++)
            grammars[i] = new Grammar(scanner.nextLine());
        grammars[0] = new Grammar("S'->S");
        for(int i = 0; i <= n; i++)
        {
            if(!index.containsKey(grammars[i].getFirst()))
                index.put(grammars[i].getFirst(), new ArrayList<>());
            index.get(grammars[i].getFirst()).add(i);
        }
    }

    private static void output() {
        for (String key : index.keySet()) {
            System.out.println(key + "=" + index.get(key));
        }
    }

    private static ArrayList<Item> getClosure(Item item) {
        ArrayList<Item> items = new ArrayList<>();
        Queue<Item> queue = new LinkedList<>();
        queue.offer(item);
        while(!queue.isEmpty()) {
            Item now = queue.poll();
            items.add(now);
            int dot = now.getDot();
            if(dot >= now.getSecond().length())
                continue;
            char c = now.getSecond().charAt(dot);
            if(Character.isUpperCase(c)) {
                String first = String.valueOf(c);
                for(int i: index.get(first)) {
                    String second = grammars[i].getSecond();
                    Item next = new Item(first, second, 0);
                    if(next.hasNextDot())
                        queue.offer(next);
                }
            }
        }
        return items;
    }

    public static void main(String[] args) {
        input();
        Item item0 = new Item("S'->S", 0);
        ArrayList<ArrayList<Item>> itemsGroup = new ArrayList<>(); // 项目集规范族
        itemsGroup.add(getClosure(item0));

        ArrayList<Edge> DFA = new ArrayList<>(); // 初始化DFA

        for(int i = 0; i < itemsGroup.size(); i++) {
            ArrayList<Item> items = itemsGroup.get(i); // 项目集
            for (Item now : items) { // 项目
                if (now.hasNextDot()) {
                    char path = now.getSecond().charAt(now.getDot());
                    ArrayList<Item> nextItems = getClosure(now.nextDot());
                    int index = itemsGroup.indexOf(nextItems);
                    if (index == -1)
                    {
                        index = itemsGroup.size();
                        itemsGroup.add(nextItems);
                    }
                    DFA.add(new Edge(i, index, path));
                }
            }
        }
        System.out.println("项目集规范族:" + itemsGroup);
        System.out.println("DFA:" + DFA);
//        System.out.println(items.get(0));
//        output();

    }
}
