import models.City;
import models.Edge;

public class Main {

    public static void main(String[] args) {

        GUITsp tsp = new GUITsp();

        Edge edgeMap[];
        City cityMap[];
        City opponentMap[];

//        DataManager dataManager = new DataManager();
//
//
//        //read data
//        dataManager.readData("data2");
//        edgeMap = dataManager.edges;
//
//        //first random move
//        Move moveManager = new Move(dataManager.cityNumber);
//
//        //only for tests
//        cityMap = new model.City[dataManager.cityNumber];
//        dataManager.createMap(cityMap);
//        moveManager.Random(cityMap);
//        opponentMap = new model.City[dataManager.cityNumber];
//        dataManager.createMap(opponentMap);
//        moveManager.Random(opponentMap);
//        dataManager.printMap(opponentMap);
//        System.out.println(" ");
//        dataManager.printMap(cityMap);
//        double playerCost = moveManager.Cost(cityMap, opponentMap,edgeMap);
//        double opponentCost = moveManager.Cost(opponentMap,cityMap,edgeMap);
//        System.out.println(playerCost);
//        System.out.println(opponentCost);
//        moveManager.NextMove(cityMap,opponentMap,playerCost,opponentCost,edgeMap);

    }
}
