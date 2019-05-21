import java.util.*;

public class Parser {

    // part1.读入文法并进行处理、扩展
    private static HashMap<String, ArrayList<Integer>> index = new HashMap<>();
    private static Grammar[] grammars = new Grammar[1024];
    private static HashSet<Character> terminator = new HashSet<>();
    private static HashSet<Character> non_terminator = new HashSet<>();

    private static void input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入文法的数目：");
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 1; i <= n; i++)
        {
            String line = scanner.nextLine();
            grammars[i] = new Grammar(line);
            for (char c : (grammars[i].getFirst() + grammars[i].getSecond()).toCharArray())
            {
                if(Character.isUpperCase(c))
                    non_terminator.add(c);
                else
                    terminator.add(c);
            }
        }
        terminator.add('#');
        String first = grammars[1].getFirst();
        grammars[0] = new Grammar(String.format("%s'->%s", first, first)); // 扩展文法
        for (int i = 0; i <= n; i++)
        {
            if(!index.containsKey(grammars[i].getFirst()))
                index.put(grammars[i].getFirst(), new ArrayList<>());
            index.get(grammars[i].getFirst()).add(i); // 合并文法左部相同的产生式id
        }
    }

    // part2.求项目集规范族
    // 求闭包
    private static ArrayList<Item> getClosure(Item item) {
        ArrayList<Item> items = new ArrayList<>();
        Queue<Item> queue = new LinkedList<>();
        queue.offer(item);
        while(!queue.isEmpty()) { // bfs
            Item now = queue.poll();
            items.add(now);
            int dot = now.getDot();
            if(dot >= now.getSecond().length())
                continue;
            char c = now.getSecond().charAt(dot);
            if(Character.isUpperCase(c)) { // 非终结符，继续搜索
                String first = String.valueOf(c);
                for (int i: index.get(first)) {
                    Item next = new Item(grammars[i].toString(), 0, i);
                    if(next.hasNextDot())
                        queue.offer(next);
                }
            }
        }
        return items;
    }

    public static void main(String[] args) {
        input();
        Item item0 = new Item(grammars[0].toString(), 0, 0);
        ArrayList<ArrayList<Item>> itemsGroup = new ArrayList<>(); // 项目集规范族
        itemsGroup.add(getClosure(item0));

        // part3.构造DFA
        ArrayList<Edge> DFA = new ArrayList<>(); // 初始化DFA

        for (int i = 0; i < itemsGroup.size(); i++) { // from
            ArrayList<Item> items = itemsGroup.get(i); // 项目集
            for (Item now : items) { // 项目
                if (now.hasNextDot()) {
                    char path = now.getSecond().charAt(now.getDot()); // path
                    ArrayList<Item> nextItems = getClosure(now.nextDot());
                    int index = itemsGroup.indexOf(nextItems); // to
                    if (index == -1)
                    {
                        index = itemsGroup.size();
                        itemsGroup.add(nextItems);
                    }
                    DFA.add(new Edge(i, index, path));
                }
            }
        }
        System.out.println(String.format("\n项目集规范族 %d个：", itemsGroup.size()) + itemsGroup);
        System.out.println(String.format("DFA %d条路径：", DFA.size()) + DFA);

        // part4.构造分析表
        int n = itemsGroup.size();
        HashMap<Character, String>[] actionTable = new HashMap[n];
        HashMap<Character, Integer>[] gotoTable = new HashMap[n];

        for(int i = 0; i < n; i++) {
            actionTable[i] = new HashMap<>();
            gotoTable[i] = new HashMap<>();
        }
        actionTable[1].put('#', "acc");

        for (Edge edge : DFA) { // DFA中的每一条路径，为非终结符填Goto表，终结符填Action表
            char path = edge.getPath();
            int from = edge.getFrom();
            int to = edge.getTo();
            if(Character.isUpperCase(path))
                gotoTable[from].put(path, to);
            else
                actionTable[from].put(path, String.format("S%d", to));
        }

        for (int i = 0; i < n; i++)
            for (Item item : itemsGroup.get(i))
                if(!item.hasNextDot() && i != 1)
                    for (char c : terminator)
                        actionTable[i].put(c, String.format("r%d", item.getBelong()));

        int t_size = terminator.size();
        int nt_size = non_terminator.size();
        System.out.println("\nLR(0)分析表：");
        System.out.print("\taction\t");
        for (int i = 1; i < t_size; i++)
            System.out.print("\t\t");
        System.out.print("goto");
        for (int i = 0; i < nt_size; i++)
            System.out.print("\t");
        System.out.print("\n\t");
        for (char c : terminator)
            System.out.print(String.format("%-4c\t", c));
        for (char c : non_terminator)
            System.out.print(String.format("%-4c\t", c));
        for (int i = 0; i < n; i++) {
            System.out.print(String.format("\n%d\t", i));
            for (char c : terminator)
                System.out.print(String.format("%-4s\t", actionTable[i].get(c)));
            for (char c : non_terminator)
                System.out.print(String.format("%-4s\t", gotoTable[i].get(c)));
        }

        // part5. 分析输入串
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n请输入要分析的输入串：");
        String str = scanner.nextLine();

        Stack<Integer> statusStack = new Stack<>();
        statusStack.push(0);
        Stack<Character> charStack = new Stack<>();
        charStack.push('#');
        int status = statusStack.peek();
        int pos = 0;
        int cnt = 1;
        System.out.println("步骤\t\t状态栈\t\t\t\t符号栈\t\t\t\t\t输入串\t\taction\t\tgoto");
        String action = actionTable[status].get(str.charAt(pos));
        while (!action.equals("acc")) {
            System.out.print(String.format("%d\t\t%-16s\t%-16s\t%8s\t%8s\t\t", cnt++, statusStack, charStack, str.substring(pos), action));
            char a = action.charAt(0);
            if(a == 'S') {
                status = Integer.parseInt(action.substring(1));
                statusStack.push(status);
                charStack.push(str.charAt(pos++));
            }
            else {
                int index = Integer.parseInt(action.substring(1));
                Grammar grammar = grammars[index];
                int len = grammar.getSecond().length();
                while(len-- > 0) {
                    statusStack.pop();
                    charStack.pop();
                }
                char c = grammar.getFirst().charAt(0);
                charStack.push(c);
                status = statusStack.peek();
                status = gotoTable[status].get(c);
                statusStack.push(status);
                System.out.print(status);
            }
            action = actionTable[status].get(str.charAt(pos));
            System.out.println();
            if (action == null) {
                System.out.print(String.format("%d\t\t%-16s\t%-16s\t%8s\t%8s\t\t", cnt, statusStack, charStack, str.substring(pos), "err"));
                return;
            }
        }
        System.out.print(String.format("%d\t\t%-16s\t%-16s\t%8s\t%8s\t\t", cnt, statusStack, charStack, str.substring(pos), action));
    }
}
