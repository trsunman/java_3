import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {
        List<List<Integer>> traps = new ArrayList<>();

        System.out.println("revealTraps trigerred");

        for (Colony colony : colonies) {
            for (int i:colony.cities){
                System.out.println("City : " + i+  " Roads to : " +colony.roadNetwork.get(i));

            }
            }
            /*
            HashSet<Integer> visited = new HashSet<>();
            List<Integer> cycle = new ArrayList<>();
            boolean hasCycle = false;

            for (int city : colony.cities) {
                if (!visited.contains(city)) {
                    hasCycle = dfs(city, -1, colony, visited, cycle);
                    if (hasCycle) {
                        break;
                    }
                }
            }

            if (hasCycle) {
                traps.add(cycle);
            } else {
                traps.add(new ArrayList<>());
            }*/


        return traps;
    }

    private boolean dfs(int currentCity, int parent, Colony colony, HashSet<Integer> visited, List<Integer> cycle) {
        visited.add(currentCity);

        List<Integer> Cities = colony.roadNetwork.get(currentCity);
        if ((Cities != null)) {
            System.out.println("city1: " + colony.roadNetwork.get(currentCity) + " city2 : " + colony.roadNetwork.get(parent));
            for (int adjacentCity : Cities) {
                if (adjacentCity == parent) {
                    continue;
                }

                if (visited.contains(adjacentCity)) {
                    cycle.add(currentCity);
                    cycle.add(adjacentCity);
                    return true;
                }

                if (dfs(adjacentCity, currentCity, colony, visited, cycle)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void printTraps(List<List<Integer>> traps) {
        System.out.println("Time traps are:");

        for (int i = 0; i < traps.size(); i++) {
            if (traps.get(i).isEmpty()) {
                System.out.printf("Colony %d: Safe%n", i + 1);
            } else {
                System.out.printf("Colony %d: %s%n", i + 1, traps.get(i));
            }
        }
    }
}
