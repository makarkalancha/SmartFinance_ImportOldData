package com.makco.smartfinance;

import com.makco.smartfinance.h2db.utils.H2DbUtils;
import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.ApplicationConstants;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.constants.ProgressBarForm;
import com.makco.smartfinance.user_interface.constants.ProgressIndicatorForm;
import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.utils.Logs;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Created by mcalancea on 2016-03-28.
 */
public class Main extends Application{
    private final static Logger LOG = LogManager.getLogger(Main.class);

    private Stage primaryStage;
//    private Worker<Void> preStartWorker;
    private PreStartWorker preStartWorker;
    private ProgressBarForm pFormStart = new ProgressBarForm();
    private BooleanProperty isEmpty = new SimpleBooleanProperty();

    public Main(){
        LOG.debug("before java.locale.providers: " + System.getProperty("java.locale.providers"));
        System.setProperty("java.locale.providers", "HOST");
        LOG.debug("after java.locale.providers: " + System.getProperty("java.locale.providers"));
        preStartWorker = new PreStartWorker();
        isEmpty.bind(preStartWorker.isEmptyProperty());
//        isEmpty.addListener((observable, oldValue, newValue) -> {
//            LOG.debug(String.format("Main->oldValue: %b; newValue: %b", oldValue, newValue));
//            if(newValue){
//                DialogMessages.showConfirmationDialog("Create", "Create", "insert or not", null);
//            }
//        });
        pFormStart.activateProgressBar(preStartWorker);
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
        System.setErr(Logs.createLoggingErrors(System.err));
        System.setOut(Logs.createLoggingDebugs(System.out));

        try {
            this.primaryStage = primaryStage;
            LOG.debug("First day of the week: "+WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
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
            ((Service<Void>) preStartWorker).setOnSucceeded(event -> {
                if(isEmpty.get()){
                    pFormStart.close();
                    boolean isConfirmed = DialogMessages.showDateUnitConfirmationDialog();
                    if(isConfirmed){

                    }
                    openApp();
                } else {
                    openApp();
                    pFormStart.close();
                }
            });
            ((Service<Void>) preStartWorker).setOnFailed(event -> {
                LOG.debug("preStartWorker->setOnFailed");
                LOG.debug(">>>>>>>>preStartWorker->setOnFailed: pFormStart.getDialogStage().close()");
                pFormStart.close();
                DialogMessages.showExceptionAlert(preStartWorker.getException());
            });
            pFormStart.show();
            ((Service<Void>) preStartWorker).restart();
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    public void openApp(){
        try {
            LOG.debug("preStartWorker->setOnSucceeded");
            LOG.debug(">>>>>>>>preStartWorker->setOnSucceeded: pFormStart.getDialogStage().close()");

            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Main.quit(event);
                }
            });

            ScreensController mainContainer = new ScreensController();
            for (Screens scr : Screens.values()) {
                mainContainer.loadScreen(scr);
            }
            mainContainer.setScreen(Screens.FAMILY_MEMBER);

            this.primaryStage.getIcons().add(new Image(ApplicationConstants.MAIN_WINDOW_ICO));
            this.primaryStage.setTitle(ApplicationConstants.MAIN_WINDOW_TITLE);
            ////http://stackoverflow.com/questions/19602727/how-to-reference-javafx-fxml-files-in-resource-folder
            Scene scene = new Scene(mainContainer);
            primaryStage.setScene(scene);
            LOG.debug(">>>>primaryStage.show()->hello: start");
            primaryStage.show();
        } catch (Throwable t) {
            DialogMessages.showExceptionAlert(t);
        }
    }

    public static void quit(Event evt){
        try {
            LOG.debug("Closing the applicatoin...");
            Worker<Void> preQuitWorker = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            LOG.debug("1-Main:backup");
                            H2DbUtils.backup("quit");
                            return null;
                        }
                    };
                }
            };
            ProgressIndicatorForm pFormQuit = new ProgressIndicatorForm();
            pFormQuit.activateProgressBar(preQuitWorker);

            ((Service<Void>) preQuitWorker).setOnSucceeded(event -> {
                Platform.exit();
                System.exit(0);
            });
            ((Service<Void>) preQuitWorker).setOnFailed(event -> {
                pFormQuit.close();
                DialogMessages.showExceptionAlert(preQuitWorker.getException());
            });
            pFormQuit.show();
            ((Service<Void>) preQuitWorker).restart();
        } catch (Throwable t) {
            DialogMessages.showExceptionAlert(t);
        }
    }
}
