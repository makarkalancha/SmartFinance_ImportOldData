package com.makco.smartfinance.user_interface.constants;

import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * User: Makar Kalancha
 * Date: 10/04/2016
 * Time: 09:43
 */
public class ProgressForm {
    private final Stage dialogStage;
//    private final ProgressBar pb = new ProgressBar();
    private final ProgressIndicator pin = new ProgressIndicator();

    public ProgressForm() {
        dialogStage = new Stage();
//        dialogStage.initStyle(StageStyle.UTILITY);//Defines a Stage style with a solid white background and minimal platform decorations used for a utility window.
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        // PROGRESS BAR
//        final Label label = new Label();
//        label.setText("alerto");

//        pb.setProgress(-1F);
        pin.setProgress(-1F);

        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
//        hb.getChildren().addAll(pb, pin);
        hb.getChildren().addAll(pin);

        Scene scene = new Scene(hb);
        dialogStage.setScene(scene);


        //https://assylias.wordpress.com/2013/12/08/383/
        //this is where the transparency is achieved:
        //the three layers must be made transparent
        //(i)  make the VBox transparent (the 4th parameter is the alpha)
        hb.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        //(ii) set the scene fill to transparent
        scene.setFill(null);
        //(iii) set the stage background to transparent
        //Defines a Stage style with a transparent background and no decorations.
        dialogStage.initStyle(StageStyle.TRANSPARENT);
    }

    public void activateProgressBar(final Worker<?> task)  {
//        pb.progressProperty().bind(task.progressProperty());
        pin.progressProperty().bind(task.progressProperty());
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
