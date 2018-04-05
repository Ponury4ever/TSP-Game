import listeners.FileReadListener;
import models.City;
import models.Edge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataManager {

    private static int MAX_WEIGHT = 100;
    private Integer size;
    private Integer cityNumber;
    private Edge edges[];
    private Random random;
    private FileReadListener listener;


    public void readData(String name) {

        File file = new File(name + ".txt");
        Scanner scanner;
        int edgesIndex = 0;
        int splitIndex;

        try {
            scanner = new Scanner(file);
            size = Integer.parseInt(scanner.next());
            edges = new Edge[size];

            cityNumber = Integer.parseInt(scanner.next()) + 1;

            String splitedArray[] = new String[4];

            for (int index = 0; index < size; ++index) {
                for (splitIndex = 0; splitIndex < 4; splitIndex++) {
                    splitedArray[splitIndex] = scanner.next();
                }
                edges[edgesIndex] = new Edge(Integer.parseInt(splitedArray[0]), Double.parseDouble(splitedArray[1]),
                        Double.parseDouble(splitedArray[2]), Double.parseDouble(splitedArray[3]));
                edgesIndex++;
            }
            scanner.close();
            listener.onSuccess();

        } catch (FileNotFoundException e) {
            listener.onFailed(e.getMessage());
        }
    }

    public void printMap(City[] map) {
        int i;
        for (i = 0; i < map.length; i++) {
            System.out.print(map[i].getId() + " ");
        }

    }

    public void createMap(City[] map) {
        int i;
        for (i = 0; i < map.length - 1; i++) {
            map[i] = new City(i + 1);
        }
        map[i] = map[0];
    }

    public void generateData(Integer numberOfCity, String fileName) {
        random = new Random();
        cityNumber = numberOfCity;
        int edgeNumber = (numberOfCity * (numberOfCity - 1)) / 2;
        int index = 0;
        System.out.println(edgeNumber);
        edges = new Edge[edgeNumber];

        for (int j = 1; j <= numberOfCity; j++) {
            for (int k = j + 1; k <= numberOfCity; k++) {
                edges[index] = new Edge(index, j, k, random.nextInt(MAX_WEIGHT) + 1);
                index++;
            }
        }
        saveToFile(edges, fileName, numberOfCity, edgeNumber);
    }

    private void saveToFile(Edge[] edges, String fileName, Integer numberOfCity, int edgeNumber) {

        List<String> lines = new ArrayList<>();

        lines.add(String.valueOf(edgeNumber));
        lines.add(String.valueOf(numberOfCity));

        for (Edge edge : edges) {
            String line = edge.getId() + " " + (int) edge.getX() + " " + (int) edge.getY() + " " + (int) edge.getW();
            lines.add(line);
        }

        Path file = Paths.get(fileName + ".txt");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
            listener.onSuccess();
        } catch (IOException e) {
            listener.onFailed(e.getMessage());
        }
    }

    public Integer getCityNumber() {
        return cityNumber;
    }

    public void setPrinterListener(FileReadListener listener) {
        this.listener = listener;
    }


    public void setCityNumber(Integer cityNumber) {
        this.cityNumber = cityNumber;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }
}
