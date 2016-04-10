package com.makco.smartfinance;

import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationUtililities;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.utils.Logs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        System.setOut(Logs.createLoggingProxy(System.out));
        try {
//            H2DbUtils.checkIfSchemaExists(ApplicationUtililities.DB_SCHEMA_NAME);
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

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ApplicationUtililities.quit(event);
                }
            });

            ScreensController mainContainer = new ScreensController();
            for (Screens scr : Screens.values()) {
                mainContainer.loadScreen(scr);
            }
//            mainContainer.setScreen(Screens.MAIN);
            mainContainer.setScreen(Screens.FAMILY_MEMBER);

            this.primaryStage.getIcons().add(new Image(ApplicationUtililities.MAIN_WINDOW_ICO));
            this.primaryStage.setTitle(ApplicationUtililities.MAIN_WINDOW_TITLE);
            ////http://stackoverflow.com/questions/19602727/how-to-reference-javafx-fxml-files-in-resource-folder
//            Group root = new Group();
//            root.getChildren().addAll(mainContainer);
//            Scene scene = new Scene(root);
            Scene scene = new Scene(mainContainer);
            primaryStage.setScene(scene);

            LOG.debug("hello: start");
            primaryStage.setMaximized(true);
            primaryStage.show();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }
}
