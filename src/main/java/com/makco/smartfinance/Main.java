package com.makco.smartfinance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by mcalancea on 2016-03-28.
 */
public class Main extends Application{

    private Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(args);
    }
//http://stackoverflow.com/questions/24055897/same-stage-different-fxml-javafx
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        this.primaryStage.setTitle("Hello");
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
