package com.makco.smartfinance.user_interface.constants.forms;

import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * User: Makar Kalancha
 * Date: 10/04/2016
 * Time: 09:43
 */
public class ProgressBarForm {
    private final Stage dialogStage;
    private final ProgressBar pb;
    private final Label messageLbl;
//    @FXML
//    private ProgressBar pb;
//    @FXML
//    private Label messageLbl;

    public ProgressBarForm() {
        dialogStage = new Stage();
        ////Defines a Stage style with a solid white background and minimal platform decorations used for a utility window.
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        //Defines a modal window that blocks events from being delivered to any other application window.
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.getIcons().add(new Image(ApplicationConstants.PROGRESS_BAR_FORM_WINDOW_ICO));

        messageLbl = new Label();
        messageLbl.setPrefHeight(18);
        messageLbl.setPrefWidth(250);
        pb = new ProgressBar();
        pb.setPrefHeight(18);
        pb.setPrefWidth(250);
        pb.setProgress(-1F);
        pb.setStyle("-fx-accent: #11fe65;");

        final VBox vb = new VBox();
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(messageLbl, pb);

        Scene scene = new Scene(vb, 300, 100);
        dialogStage.setScene(scene);


//        //https://assylias.wordpress.com/2013/12/08/383/
//        //this is where the transparency is achieved:
//        //the three layers must be made transparent
//        //(i)  make the VBox transparent (the 4th parameter is the alpha)
//        hb.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//        //(ii) set the scene fill to transparent
//        scene.setFill(null);
//        //(iii) set the stage background to transparent
//        //Defines a Stage style with a transparent background and no decorations.
//        dialogStage.initStyle(StageStyle.TRANSPARENT);
    }

    public void activateProgressBar(final Worker<?> task)  {
        pb.progressProperty().bind(task.progressProperty());
        dialogStage.titleProperty().bind(task.titleProperty());
        messageLbl.textProperty().bind(task.messageProperty());
        task.messageProperty();
//        pin.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    private Stage getDialogStage() {
        return dialogStage;
    }

    public void show(){
        getDialogStage().setAlwaysOnTop(true);
        getDialogStage().show();
    }

    public void close(){
        getDialogStage().close();
    }
}
