package com.makco.smartfinance;

import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.Screens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by mcalancea on 2016-03-28.
 */
public class Main extends Application{
    private final static Logger LOG = LogManager.getLogger(Main.class);

    private Stage primaryStage;

    //https://www.youtube.com/watch?v=5GsdaZWDcdY
    //https://github.com/acaicedo/JFX-MultiScreen/tree/master/ScreensFramework/src/screensframework
    public static void main(String[] args) {
        Application.launch(args);
    }
    //http://stackoverflow.com/questions/24055897/same-stage-different-fxml-javafx
    //http://www.devx.com/Java/Article/48193/0/page/2
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;


        ScreensController mainContainer = new ScreensController();
        for(Screens scr : Screens.values()) {
            mainContainer.loadScreen(scr);
        }
        mainContainer.setScreen(Screens.MAIN);


        this.primaryStage.setTitle("Hello");
        ////http://stackoverflow.com/questions/19602727/how-to-reference-javafx-fxml-files-in-resource-folder
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        LOG.debug("hello: start");
        primaryStage.show();
    }
}
