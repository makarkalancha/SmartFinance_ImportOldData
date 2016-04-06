package com.makco.smartfinance;

import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.utils.Logs;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
    public void start(Stage primaryStage){
        System.setErr(Logs.createLoggingProxy(System.err));
        try {
//            H2DbUtils.checkIfSchemaExists(ApplicationConstants.DB_SCHEMA_NAME);
//http://www.hascode.com/2013/04/easy-database-migrations-using-flyway-java-ee-6-and-glassfish/
//            Flyway flyway = new Flyway();
//            flyway.setDataSource(DBConnectionResource.getDbConnectionUrl(),TestContext.INSTANCE.DB_USER(),TestContext.INSTANCE.DB_PASSWORD());
//            flyway.migrate();
            if(H2DbUtils.checkIfSchemaExists(DataBaseConstants.SCHEMA)){
                LOG.debug("db exists");
            } else {
                LOG.debug("db DOESN'T exist");
                H2DbUtils.migrate(DataBaseConstants.SCHEMA);
            }

            this.primaryStage = primaryStage;

            ScreensController mainContainer = new ScreensController();
            for (Screens scr : Screens.values()) {
                mainContainer.loadScreen(scr);
            }
//            mainContainer.setScreen(Screens.MAIN);
            mainContainer.setScreen(Screens.FAMILY_MEMBER);

            this.primaryStage.setTitle("Hello");
            ////http://stackoverflow.com/questions/19602727/how-to-reference-javafx-fxml-files-in-resource-folder
            Group root = new Group();
            root.getChildren().addAll(mainContainer);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            LOG.debug("hello: start");
            primaryStage.show();
        }catch (Exception e){
            LOG.error(e, e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Look, an Exception Dialog");
            alert.setContentText(e.getMessage());

//            Exception ex = new FileNotFoundException("Could not find file blabla.txt");

// Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }

    }
}
