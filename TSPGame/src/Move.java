import models.City;
import models.Edge;

import java.util.Random;

public class Move {

    private double start_temp;
    private double stop_temp;
    private double max_iteration;
    private double max_same_iteration;
    private double alpha;
    private double punish;
    private double currentPlayerCost;
    private double currentOpponentCost;

    public Move(int number) {

        //set parameters
        start_temp = Math.sqrt(number);
        stop_temp = 0.00001;
        max_iteration = number * 3;
        max_same_iteration = 3;
        alpha = 0.995;
        punish = 2;


    }

    public void NextMove(City[] playerMap, City[] opponentMap, double playerCost, double opponentCost, Edge[] edges) {

        //TODO think about optimalizations

        double currentPlayerCost = playerCost;
        double optimalPlayerCost = playerCost;

        double currentOpponentCost = opponentCost;
        double optimalOpponentCost = opponentCost;

        City[] currentPlayerMap = playerMap;
        City[] optimalPlayerMap = opponentMap;

        Integer[] swap; //edges to change

        Random random = new Random();


        double currentTemp = start_temp;

        double newCost;
        double newOpponentCost;
        int k = 0;
        int l = 0;

        while (k <= max_iteration) {
            currentTemp = ChangeTemp(currentTemp, alpha, stop_temp);
            while (l <= max_same_iteration) {
                swap = GetSwap(playerMap.length);
                if (swap[0] != 0 && swap[1] != 0) {

                    //count new cost
                    currentPlayerMap = ChangePlayerMap(swap, currentPlayerMap);
                    newCost = 0.0;
                    newOpponentCost = 0.0;
                    newCost = Cost(currentPlayerMap, opponentMap, edges);
                    newOpponentCost = Cost(opponentMap, currentPlayerMap, edges);


                    if (newCost - newOpponentCost <= optimalPlayerCost - optimalOpponentCost
                            || random.nextDouble() < Math.exp(((optimalPlayerCost - optimalOpponentCost) - (newCost - newOpponentCost)) / currentTemp)) {
                        optimalPlayerCost = newCost;
                        optimalOpponentCost = newOpponentCost;
                    }
                }
                if ((optimalPlayerCost - optimalOpponentCost) <= (currentPlayerCost - currentOpponentCost)) {
                    currentPlayerCost = optimalPlayerCost;
                    currentOpponentCost = optimalOpponentCost;
                    for (int i = 0; i < currentPlayerMap.length; i++) {
                        optimalPlayerMap[i] = currentPlayerMap[i];
                    }
                }

                l++;
            }
            l = 0;

            k++;

        }

        this.currentPlayerCost = currentPlayerCost;
        this.currentOpponentCost = currentOpponentCost;

//        System.out.println("Result: " + currentPlayerCost);
//        System.out.println("Result: " + currentOpponentCost);

    }


    public void Random(City[] map) {
        Random random = new Random();

        int i;
        int j;
        City temp;
        //city first is state
        for (i = 1; i < map.length - 1; i++) {
            j = random.nextInt(map.length - 2) + 1;
            temp = map[i];
            map[i] = map[j];
            map[j] = temp;
        }

    }

    public double Cost(City[] playerMap, City[] opponentMap, Edge[] edgesMap) {
        double cost = 0;
        Edge[] opponentEdges;
        opponentEdges = PlayerEdges(opponentMap, edgesMap);

        Edge[] playerEdges;
        playerEdges = PlayerEdges(playerMap, edgesMap);


        for (int i = 0; i < playerEdges.length; i++) {
            int visited = 0;
            int j = 0;
            while (j < i && visited == 0) {
                if (playerEdges[i].getId() == opponentEdges[j].getId()) {
                    visited = 1;
                }
                j++;
            }

            if (visited == 1) {
                cost = cost + playerEdges[i].getW() * punish;
            } else {
                cost = cost + playerEdges[i].getW();

            }
        }

        return cost;
    }

    public Edge[] PlayerEdges(City[] opponentMap, Edge[] edgesMap) {
        Edge[] opponentEdges = new Edge[opponentMap.length - 1];

        for (int i = 0; i < opponentMap.length - 1; i++) {
            int found = 0;
            int j = 0;
            while (found == 0) {
                if (opponentMap[i].getId() == edgesMap[j].getX() && opponentMap[i + 1].getId() == edgesMap[j].getY()) {
                    opponentEdges[i] = edgesMap[j];
                    found = 1;
                } else if (opponentMap[i].getId() == edgesMap[j].getY() && opponentMap[i + 1].getId() == edgesMap[j].getX()) {
                    opponentEdges[i] = edgesMap[j];
                    found = 1;
                }
                j++;
            }

        }
        return opponentEdges;
    }

    public double ChangeTemp(double current_temp, double alpha, double stop_temp) {
        if (stop_temp >= current_temp) {
            return stop_temp;
        } else {
            return current_temp * alpha;
        }
    }

    //returns random swap par
    public Integer[] GetSwap(int length) {

        Random random = new Random();

        Integer[] swaps = new Integer[2];
        int j;
        int k;

        j = random.nextInt(length - 1); //index of edge
        k = random.nextInt(length - 1); //index of edge


        int i = 0;
        while (j == k || Math.abs(j - k) < 2) {
            //TODO 'zmienna' stala na wartosc zalezna od dlugosci listy
            if (i >= 4) {
                j = 0;
                k = 0;
                break;
            } else {
                j = random.nextInt(length - 1); //index of edge
                k = random.nextInt(length - 1); //index of edge
            }
            i++;
        }

        if (j > k) {
            swaps[0] = k;
            swaps[1] = j;
        } else {
            swaps[0] = j;
            swaps[1] = k;
        }

        return swaps;
    }

    public City[] ChangePlayerMap(Integer[] swaps, City[] playerMap) {
        City temp;
        temp = playerMap[swaps[0]];
        playerMap[swaps[0]] = playerMap[swaps[1]];
        playerMap[swaps[1]] = temp;
        return playerMap;
    }

    public double getCurrentPlayerCost() {
        return currentPlayerCost;
    }

    public void setCurrentPlayerCost(double currentPlayerCost) {
        this.currentPlayerCost = currentPlayerCost;
    }

    public double getCurrentOpponentCost() {
        return currentOpponentCost;
    }

    public void setCurrentOpponentCost(double currentOpponentCost) {
        this.currentOpponentCost = currentOpponentCost;
    }
}
