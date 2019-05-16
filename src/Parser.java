import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

    public static void main(String[] args) {
        input();
//        output();

    }
}
