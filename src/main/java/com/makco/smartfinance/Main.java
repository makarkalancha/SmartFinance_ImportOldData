package com.makco.smartfinance;

import com.makco.smartfinance.user_interface.ScreensController;
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

    public static final String mainID = "main";
    public static final String mainFile = "main.fxml";
    public static final String screen1ID = "scene1";
    public static final String screen1File = "scene1.fxml";
    public static final String screen2ID = "scene2";
    public static final String screen2File = "scene2.fxml";

//    private Button btnScene1;
//    private Button btnScene2;
//    private Label lblScene1;
//    private Label lblScene2;
//    private FlowPane pane1;
//    private FlowPane pane2;
//    private Scene scene1;
//    private Scene scene2;

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

//        btnScene1 = new Button("Click to go to Other Scene");
//        btnScene1.setOnAction(e->ButtonClicked(e));
//
//        btnScene2 = new Button("Click to go back to First Scene");
//        btnScene2.setOnAction(e->ButtonClicked(e));
//
//        lblScene1 = new Label("Scene 1");
//        lblScene2 = new Label("Scene 2");
//
//        pane1 = new FlowPane();
//        pane1.setVgap(10);
//        pane1.setStyle("-fx-background-color: tan; -fx-padding: 10px;");
//        pane1.getChildren().addAll(lblScene1, btnScene1);
//
//        pane2 = new FlowPane();
//        pane2.setVgap(10);
//        pane2.setStyle("-fx-background-color: red; -fx-padding: 10px;");
//        pane2.getChildren().addAll(lblScene2, btnScene2);
//
//        scene1 = new Scene(pane1, 200, 100);
//        scene2 = new Scene(pane2, 200, 100);

//        primaryStage.setScene(scene1);

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.mainID, Main.mainFile);
        mainContainer.loadScreen(Main.screen1ID, Main.screen1File);
        mainContainer.loadScreen(Main.screen2ID, Main.screen2File);

        mainContainer.setScreen(Main.mainID);


        this.primaryStage.setTitle("Hello");
//        //http://stackoverflow.com/questions/19602727/how-to-reference-javafx-fxml-files-in-resource-folder
//        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        LOG.debug("hello: start");
        primaryStage.show();
    }

//    public void ButtonClicked(ActionEvent event){
//        if(event.getSource() == btnScene1){
//            primaryStage.setScene(scene2);
//        } else if(event.getSource() == btnScene2){
//            primaryStage.setScene(scene1);
//        }
//    }
}
