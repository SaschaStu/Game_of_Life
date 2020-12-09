package ch.bbw.pr.helloworldfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import java.io.File;
import java.util.Scanner;

public class Controller {
    private boolean isRunning = true;
    private int sleepMs = 1000;
    private int size = 500;
    private int w = 20;
    private int h = 20;
    //Wie viele Iterationen zurück geprüft wird, ob ein Muster vorhanden ist.
    private final int turnToLookBack = 2;
    private boolean colorStatus = true;
    //vergangene Felder werden als 2d Boolean-Array gespeichert. Die dritte Dimension dient dem Speichern mehrerer Felder.
    //es wird nur true und false, also alive und dead gespeichert, da das Alter der Zellen für Muster nicht relevant ist.
    private boolean[][][] pastGrids = new boolean[turnToLookBack+1][w][h];
    Model model;
    @FXML
    AnchorPane anchor;
    @FXML
    Pane cellField;
    GridPane grid = new GridPane();
    Cell[][] buttons = new Cell[w][h];
    @FXML
    Button runningButton;

    @FXML
    protected void initialize() {
        //man beachte die kreative Namensgebung
        File pp = new File("files/5x5.txt");
        Scanner ppReader;
        try {
            ppReader = new Scanner(pp);
            cellField.getChildren().add(grid);
            grid.setPrefSize(size, size);
            //grid.getChildren().add(new Button("Click me!"));
            for (int j = 0; j < h; j++) {
                for (int i = 0; i < w; i++) {
                    buttons[i][j] = new Cell(ppReader.nextInt());
                    buttons[i][j].setPrefHeight(size / h);
                    buttons[i][j].setPrefWidth(size / w);
                    buttons[i][j].setMaxHeight(size / h);
                    buttons[i][j].setMaxWidth(size / w);
                    int finalI = i;
                    int finalJ = j;
                    buttons[i][j].setOnAction((ActionEvent event) -> {
                        //penis();
                        buttons[finalI][finalJ].setAge(1);


                    });
                    grid.add(buttons[i][j], i, j, 1, 1);
                }

            }
        } catch (FileNotFoundException fileEx) {
            System.out.println("*Loud screaming* AAAAAAAAAAAAAAAAAAAAAAAAAAAAH! We are all gonna the the path is not a fileeeee\n" +
                    "WHAT DID YOU DO!\n" +
                    "Everyone, Evacuate! I repeat, Evacuate!!!!!!!\n");
        }
        //Boolean-Array für vergangenen Felder wird leer initialisiert. Wenn falls sich das erste Feld um ein leeres handelt, wird ein Muster erkennt,
        //da es dem Boolean-Array gleicht, obwohl noch keine Iteration durchgeführt wurde. Das macht aber nichts, da bei einem leeren Feld es sich immer um
        //Muster handelt
        for (int iteration = 0; iteration < turnToLookBack+1; iteration++) {
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    pastGrids[iteration][x][y] = false;
                }

            }
        }
        setCellColor();
    }
/*
    public void penis() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (buttons[i][j].isPressed()) {
                    buttons[i][j].setAge(1);
                    System.out.println("penis3333333333333333");
                }
            }
        }
        System.out.println("penis");
    }

 */

    //Farbumschaltung
    public void toggleColor(ActionEvent f) {
        colorStatus = !colorStatus;
    }

    public void toggleRunning(ActionEvent f) {
        if(isRunning) {
           // runningButton
        }
    }

    void loop() throws InterruptedException {
        //Der unendliche Loop wird in einen Hintergrund-Thread verschoben, damit die View aktualisiert werden kann
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    updateCells();
                });
            }
        }).start();
    }

    void updateCells() {
        nextRound();
        setCellColor();
        saveCurrentGrid();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                buttons[i][j].setText(""+buttons[i][j].getAge());
            }
        }


    }

    void nextRound() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                buttons[i][j].updateAge(countNeighbors(i, j));
                //buttons[i][j].setText("" + countNeighbors(i, j));
            }
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                buttons[i][j].setNewAge();
            }
        }
    }

    void setCellColor() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                buttons[i][j].setStyle(buttons[i][j].getNewColor(colorStatus));
            }
        }
    }

    void saveCurrentGrid() {
        for (int iteration = turnToLookBack; iteration >= 1; iteration--) {
            //Nach testen in Processing habe ich herausgefunden, dass in 3d Arrays nur 1 Dimension angeben kann, um so die 2D grid eine Position nach unten
            //zu verrutschen. In die erste Position kommt das aktuelle Feld.

            //Lösung zur clonung eines Arrays von https://stackoverflow.com/a/53397359
            pastGrids[iteration] = Arrays.stream(pastGrids[iteration-1]).map(boolean[]::clone).toArray(boolean[][]::new);
        }
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                pastGrids[0][i][j] = (buttons[i][j].getAge() == 0 ? false : true);
            }
        }
        printPastGrid(0);
        System.out.println("---------------------------------------------------------------------------");
        printPastGrid(1);
        System.out.println("---------------------------------------------------------------------------");
        printPastGrid(2);
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------");
        for (int iteration = 1; iteration < turnToLookBack+1; iteration++) {
            if (compare2DBooleanArray(pastGrids[0], pastGrids[iteration])) {
                System.out.println("BIIIIIIIIIIIIG PP!!! Erweiterung almost done!!! yay");
            }
        }
    }

    public int countNeighbors(int x, int y) {
        int neighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i + x >= 0 && i + x < w && j + y >= 0 && j + y < h) {
                    if (buttons[x + i][y + j].getAge() > 0 && (i != 0 || j != 0)) {
                        neighbors++;
                    }
                }
            }

        }
        return neighbors;
    }

    public void setModel(Model model) {
        this.model = model;
    }
    //Funktion um 2D-boolean-Array zu vergleichen, hier primär die pastGrids
    public boolean compare2DBooleanArray(boolean[][] array1, boolean[][] array2) {
        if(array1.length!=array2.length||array1[0].length!=array2[0].length) {
            System.out.println("Länge der Arrays stimmt nicht überrein!");
            return false;
        }
        else {
            for (int i = 0; i < array1.length; i++) {
                for (int j = 0; j < array1[0].length; j++) {
                    if(array1[i][j]!=array2[i][j]) {
                        System.out.println("Inhalt der Arrays ist nicht gleich!");
                        return false;
                    }
                }
            }
        }
        System.out.println("Die Arrays sind gleich!");
        return true;
    }
    //für debugging der Mustererkennung
    public void printPastGrid(int iteration) {
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                if(pastGrids[iteration][i][j]==true) {
                    System.out.print("\u001B[31m " + 1+ "\u001B[0m");
                }
                else {
                    System.out.print(" "+0 + "");
                }
            }
            System.out.println("\n");
        }
    }
}
