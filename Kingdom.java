import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Kingdom {

    private int[] parent;
    private int[] rank;

    public void initializeKingdom(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            int size = lines.size();
            parent = new int[size];
            rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }

            for (int i = 0; i < size; i++) {
                String[] elements = lines.get(i).split(" ");
                for (int j = 0; j < size; j++) {
                    if (elements[j].equals("1")) {
                        union(i, j);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    public List<Colony> getColonies() {
        Map<Integer, Colony> colonyMap = new HashMap<>();
        for (int i = 0; i < parent.length; i++) {
            int root = find(i);
            if (!colonyMap.containsKey(root)) {
                colonyMap.put(root, new Colony());
            }
            colonyMap.get(root).cities.add(i + 1);
        }
        return new ArrayList<>(colonyMap.values());
    }

    public void printColonies(List<Colony> discoveredColonies) {
        System.out.println("####################################");
        System.out.println("Discovered colonies are:");
        for (int i = 0; i < discoveredColonies.size(); i++) {
            System.out.printf("Colony %d: %s%n", i + 1, discoveredColonies.get(i).cities);
        }
    }
}
