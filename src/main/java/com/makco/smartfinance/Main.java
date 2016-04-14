package com.makco.smartfinance;

import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.constants.ProgressForm;
import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.utils.ApplicationUtililities;
import com.makco.smartfinance.utils.Logs;

import java.util.EnumSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
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
    private Executor executor;
    private Worker<Void> migrateWorker;
    private ProgressForm pForm = new ProgressForm();

    public Main(){
        executor = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        migrateWorker = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LOG.debug("Main:migrate");
                H2DbUtils.migrate(DataBaseConstants.SCHEMA);
                return null;
            }
        };
        pForm.activateProgressBar(migrateWorker);
        pForm.getDialogStage().setAlwaysOnTop(true);
    }

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
////            H2DbUtils.checkIfSchemaExists(ApplicationConstants.DB_SCHEMA_NAME);
////http://www.hascode.com/2013/04/easy-database-migrations-using-flyway-java-ee-6-and-glassfish/
////            Flyway flyway = new Flyway();
////            flyway.setDataSource(DBConnectionResource.getDbConnectionUrl(),TestContext.INSTANCE.DB_USER(),TestContext.INSTANCE.DB_PASSWORD());
////            flyway.migrate();
////            if(H2DbUtils.checkIfSchemaExists(DataBaseConstants.SCHEMA)){
////                LOG.debug("db exists");
////            } else {
////                LOG.debug("db DOESN'T exist");
//
//            H2DbUtils.migrate(DataBaseConstants.SCHEMA);
            ((Task<Void>) migrateWorker).setOnSucceeded(event -> {
                LOG.debug("migrateWorker->setOnSucceeded");
                LOG.debug(">>>>>>>>migrateWorker->setOnSucceeded: pForm.getDialogStage().close()");

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
                mainContainer.setScreen(Screens.MAIN);
//                mainContainer.setScreen(Screens.FAMILY_MEMBER);

                this.primaryStage.getIcons().add(new Image(ApplicationConstants.MAIN_WINDOW_ICO));
                this.primaryStage.setTitle(ApplicationConstants.MAIN_WINDOW_TITLE);
                ////http://stackoverflow.com/questions/19602727/how-to-reference-javafx-fxml-files-in-resource-folder
//            Group root = new Group();
//            root.getChildren().addAll(mainContainer);
//            Scene scene = new Scene(root);
                Scene scene = new Scene(mainContainer);
                primaryStage.setScene(scene);

                LOG.debug(">>>>primaryStage.show()->hello: start");
//            primaryStage.setMaximized(true);
                primaryStage.show();

                pForm.getDialogStage().close();
                //                startButton.setDisable(false);
            });
            ((Task<Void>) migrateWorker).setOnFailed(event -> {
                LOG.debug("migrateWorker->setOnFailed");
                LOG.debug(">>>>>>>>migrateWorker->setOnFailed: pForm.getDialogStage().close()");

                DialogMessages.showErrorDialog("Error", EnumSet.of(ErrorEnum.MAIN_ERROR), null);

                pForm.getDialogStage().close();
//                onClear(actionEvent);
//                startButton.setDisable(false);
            });
            pForm.getDialogStage().show();
            executor.execute((Task<Void>)migrateWorker);
////            }


        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }
}
