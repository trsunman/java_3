import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TravelMap {

    public Map<Integer, Location> locationMap = new HashMap<>();

    public List<Location> locations = new ArrayList<>();

    public List<Trail> trails = new ArrayList<>();

    public void initializeMap(String filename) throws  IOException, SAXException {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        try{
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename));
            NodeList locationNodes = doc.getElementsByTagName("Location");
            for (int i = 0; i < locationNodes.getLength(); i++) {
                Element locationNode = (Element) locationNodes.item(i);
                String name = locationNode.getElementsByTagName("Name").item(0).getTextContent();
                int id = Integer.parseInt(locationNode.getElementsByTagName("Id").item(0).getTextContent());
                Location location = new Location(name, id);
                locations.add(location);
                locationMap.put(id, location);
            }

            List<Trail> trailList = new ArrayList<>();
            NodeList trailNodes = doc.getElementsByTagName("Trail");
            for (int i = 0; i < trailNodes.getLength(); i++) {
                Element trailNode = (Element) trailNodes.item(i);
                int sourceId = Integer.parseInt(trailNode.getElementsByTagName("Source").item(0).getTextContent());
                int destId = Integer.parseInt(trailNode.getElementsByTagName("Destination").item(0).getTextContent());
                int danger = Integer.parseInt(trailNode.getElementsByTagName("Danger").item(0).getTextContent());
                Location source = locationMap.get(sourceId);
                Location dest = locationMap.get(destId);
                Trail trail = new Trail(source, dest, danger);
                trails.add(trail);
            }
        }
        catch (ParserConfigurationException p){

        }

    }

    private int findParent(int locationId, Map<Integer, Integer> parentMap) {
        int parent = parentMap.get(locationId);
        while (parent != parentMap.get(parent)) {
            parent = parentMap.get(parent);
        }
        return parent;
    }
    public List<Trail> getSafestTrails() {
        List<Trail> allTrails = trails;
        Collections.sort(allTrails, Comparator.comparingInt(t -> t.danger));

        Map<Integer, Integer> parentMap = new HashMap<>();
        for (int i = 0; i < locations.size(); i++) {
            parentMap.put(i, i);
        }
        List<Trail> safestTrails = new ArrayList<>();
        for (Trail trail : allTrails) {
            int sourceParent = findParent(trail.source.id, parentMap);
            int destinationParent = findParent(trail.destination.id, parentMap);
            if (sourceParent != destinationParent) {
                safestTrails.add(trail);
                parentMap.put(sourceParent, destinationParent);
            }
        }
        return safestTrails;

    }

    public void printSafestTrails(List<Trail> safestTrails) {
        int totalDanger = safestTrails.stream().mapToInt(trail -> trail.danger).sum();
        System.out.println("Safest trails are:");
        for (Trail trail : safestTrails) {
            System.out.printf("The trail from %s to %s with danger %d%n",
                    trail.source.name, trail.destination.name, trail.danger);
        }
        System.out.printf("Total danger: %d%n", totalDanger);
    }
}
