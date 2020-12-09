package ch.bbw.pr.helloworldfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

class Cell extends Button {
    final String[] color = {"#e6e6e6","#eaff00","#ffe100", "#ffa200","#ff7700","#ff5100","#ff2f00","#ff1900","#ff0000"};
    private int age;
    private int nextAge;
    private int maxAge = color.length-1;

    public int getAge() {
        return age;
    }

    Cell(int age) {

        this.age = age;
    }
    public String getNewColor(boolean colorStatus) {
        String newColor = null;
        if(colorStatus) {
            //Man beachte die fancy concat expression, um weniger Zeilen zu brauchen
            newColor = "-fx-background-color: " + color[(age < maxAge ? age : maxAge)] + "; ";
        }
        else {
            newColor = "-fx-background-color: " + color[(age < 1 ? age : 1)] + "; ";
        }
        return newColor;
    }
    public void toggleColor(ActionEvent f) {
        System.out.println("lol nani!?");
    }
    public void updateAge(int neighbors) {
        if(age==0&&neighbors==3)
        {
            nextAge = 1;
        }
        else if(age>0&&neighbors>=2&&neighbors<=3) {
            nextAge = age+1;
        }
        else {
            nextAge = 0;
        }
    }
    public void setNewAge()
    {
        age = nextAge;
    }
    public void setAge(int age) {
        this.age = age;
    }
}