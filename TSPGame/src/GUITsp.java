import listeners.FileReadListener;
import models.City;
import models.Edge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * Created by PC on 2018-04-05.
 */
public class GUITsp implements ActionListener, FileReadListener {

    private static final String ERROR_MESSAGE_TITLE = "Error";
    private static final String ID = "ID";
    private static final String FIRST_CITY = "1st city";
    private static final String SECOND_CITY = "2nd city";
    private static final String VALUE = "Value";

    private JFrame frame;
    private JButton readButton;
    private JTextField fileNameTextField;
    private JTextField numberOfCitiesTextField;
    private JButton generateButton;
    private JTextArea matrixTextArea;
    private JTextField stepsAmountTextField;
    private JButton startButton;
    private JTextArea resultTextArea;
    private JPanel panel;
    private JTextField newFileNameTextField;

    private DataManager dataManager = new DataManager();
    private Edge edgeMap[];
    private City cityMap[];
    private City opponentMap[];
    private Move moveManager;
    private int amountSteps;


    public GUITsp() {
        init();
    }

    private void init() {
        frame = new JFrame("TSP Game, Klaudia Rzońca, Michał Rokita");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);

        dataManager.setPrinterListener(this);
        readButton.addActionListener(this);
        generateButton.addActionListener(this);
        startButton.setEnabled(false);
        startButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == readButton) {
            dataManager.readData(fileNameTextField.getText());

        } else if (source == generateButton) {
            if (!numberOfCitiesTextField.getText().isEmpty() && !newFileNameTextField.getText().isEmpty()) {
                dataManager.generateData(Integer.valueOf(numberOfCitiesTextField.getText()), newFileNameTextField.getText());
            } else {
                showErrorMessage(ERROR_MESSAGE_TITLE, "Prosze wypełnić pola");
            }
        } else if (source == startButton) {
            if (!stepsAmountTextField.getText().isEmpty()) {
                try {
                    amountSteps = Integer.valueOf(stepsAmountTextField.getText());
                    start();
                } catch (NumberFormatException exception) {
                    showErrorMessage(ERROR_MESSAGE_TITLE, "Niepoprawne dane");
                }
            } else {
                showErrorMessage(ERROR_MESSAGE_TITLE, "Prosze podać ilość kroków");
            }

        }

    }

    private void start() {

        resultTextArea.removeAll();
        resultTextArea.append("RUCH 1\n");

        moveManager = new Move(dataManager.getCityNumber());
        cityMap = new City[dataManager.getCityNumber()];
        dataManager.createMap(cityMap);
        moveManager.Random(cityMap);
        opponentMap = new City[dataManager.getCityNumber()];
        dataManager.createMap(opponentMap);
        moveManager.Random(opponentMap);

        printMap(cityMap, 1);
        printMap(opponentMap, 2);

        double playerCost = moveManager.Cost(cityMap, opponentMap, edgeMap);
        double opponentCost = moveManager.Cost(opponentMap, cityMap, edgeMap);

        printCost(playerCost, 1);
        printCost(playerCost, 2);

        for (int i = 1; i < amountSteps; i++) {
            resultTextArea.append(String.format(Locale.ENGLISH, "%s%o\n", "RUCH ", i + 1));
            moveManager.NextMove(cityMap, opponentMap, playerCost, opponentCost, edgeMap);
            printMap(cityMap, 1);
            printMap(opponentMap, 2);
            printCost(moveManager.getCurrentPlayerCost(), 1);
            printCost(moveManager.getCurrentOpponentCost(), 2);
        }
    }

    private void printCost(double cost, int player) {
        resultTextArea.append(String.format(Locale.ENGLISH, "%s%o\n", "koszt gracza nr ", player));
        resultTextArea.append(cost + "\n\n");
    }

    public void printMap(City[] map, int player) {
        resultTextArea.append(String.format(Locale.ENGLISH, "%s%o\n", "trasa gracza nr ", player));
        for (City aMap : map) {
            resultTextArea.append("->" + aMap.getId());
        }
        resultTextArea.append("\n\n");
    }

    @Override
    public void onSuccess() {
        edgeMap = dataManager.getEdges();
        startButton.setEnabled(true);
        showEdgeMap();
    }

    private void showEdgeMap() {
        matrixTextArea.removeAll();
        matrixTextArea.append(String.format(Locale.ENGLISH, "%s\t%s\t%s\t%s\n", ID, FIRST_CITY, SECOND_CITY, VALUE));
        for (Edge edge : edgeMap) {
            matrixTextArea.append(edge.toString());
        }
    }

    @Override
    public void onFailed(String message) {
        showErrorMessage(ERROR_MESSAGE_TITLE, message);
        startButton.setEnabled(false);
    }

    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(frame,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
}
