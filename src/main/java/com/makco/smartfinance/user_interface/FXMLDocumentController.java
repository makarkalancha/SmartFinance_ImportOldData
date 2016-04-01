package com.makco.smartfinance.user_interface;/**
 * Created by mcalancea on 2016-04-01.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

//http://www.javafxtutorials.com/tutorials/switching-to-different-screens-in-javafx-and-fxml/
public class FXMLDocumentController implements Initializable {
    @FXML
    private Button btnScene1;
    @FXML
    private Button btnScene2;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        if (event.getSource() == btnScene1) {
            stage = (Stage) btnScene1.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("scene2.fxml"));
        } else if (event.getSource() == btnScene2) {
            stage = (Stage) btnScene2.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("scene1.fxml"));
        } else {
            stage = (Stage) btnScene2.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("scene1.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
