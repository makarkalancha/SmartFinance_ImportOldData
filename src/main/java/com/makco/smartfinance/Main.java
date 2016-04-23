package com.makco.smartfinance;

import com.makco.smartfinance.user_interface.ScreensController;
import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.utility_screens.forms.ProgressBarForm;
import com.makco.smartfinance.user_interface.utility_screens.forms.ProgressIndicatorForm;
import com.makco.smartfinance.user_interface.workers.QuitWorker;
import com.makco.smartfinance.user_interface.workers.prestart.InsertDateUnitWorker;
import com.makco.smartfinance.user_interface.workers.prestart.PreStartWorker;
import com.makco.smartfinance.utils.Logs;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.MainMapLookup;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by mcalancea on 2016-03-28.
 */

//TODO run h2 db and launch main
//exception will appear with progress bar
public class Main extends Application{
    private final static Logger LOG;
    static {
        //set locale must be before initializing LOG, otherwise it will not work
        System.setProperty("java.locale.providers", "HOST,SPI,JRE");
        System.setErr(Logs.createLoggingErrors(System.err));
        System.setOut(Logs.createLoggingDebugs(System.out));
        LOG = LogManager.getLogger(Main.class);
    }

    private static final Worker<Void> quitWorker = new QuitWorker();;

    private Stage primaryStage;
    private Worker<Void> preStartWorker;
    private Worker<Void> insertDateUnitWorker;

    private ProgressBarForm pFormStart = new ProgressBarForm();
    private ProgressBarForm pFormInsertDateUnit = new ProgressBarForm();


    private BooleanProperty isEmpty = new SimpleBooleanProperty();

    public Main(){
        preStartWorker = new PreStartWorker();
        isEmpty.bind(((PreStartWorker) preStartWorker).isEmptyProperty());


        insertDateUnitWorker = new InsertDateUnitWorker();

    }

    //https://www.youtube.com/watch?v=5GsdaZWDcdY
    //https://github.com/acaicedo/JFX-MultiScreen/tree/master/ScreensFramework/src/screensframework
    public static void main(String[] args) {
        //https://logging.apache.org/log4j/2.0/manual/lookups.html
        MainMapLookup.setMainArguments(args);
        Application.launch(args);
    }
    //http://www.devx.com/Java/Article/48193/0/page/2
    @Override
    public void start(Stage primaryStage){
        try {
            this.primaryStage = primaryStage;
            LOG.debug("First day of the week: "+ WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
            ((Service<Void>) preStartWorker).setOnSucceeded(event -> {
                if (isEmpty.get()) {
                    pFormStart.close();
                    Optional<LocalDate> ldt = DialogMessages.showDateUnitConfirmationDialog();
                    if (ldt.isPresent()) {
                        LocalDate localDate = ldt.get();
                        LOG.debug("Main->localDatetime: " + localDate.toString());
                        pFormInsertDateUnit.activateProgressBar(insertDateUnitWorker);
                        pFormInsertDateUnit.show();
                        ((InsertDateUnitWorker) insertDateUnitWorker).setStartLocalDate(localDate);
                        ((Service<Void>) insertDateUnitWorker).restart();
                    } else {
                        Main.quit(null);
                    }
                } else {
                    openApp();
                    pFormStart.close();
                }
            });
            ((Service<Void>) preStartWorker).setOnFailed(event -> {
                pFormInsertDateUnit.close();
                pFormStart.close();
                LOG.debug("preStartWorker->setOnFailed");
                DialogMessages.showExceptionAlert(preStartWorker.getException());
                preStartWorker.cancel();
                Main.quit(null);
            });
            pFormStart.activateProgressBar(preStartWorker);
            pFormStart.show();
            ((Service<Void>) preStartWorker).restart();


            ((Service<Void>) insertDateUnitWorker).setOnSucceeded(event -> {
                openApp();
                pFormInsertDateUnit.close();
            });
            ((Service<Void>) insertDateUnitWorker).setOnFailed(event -> {
                LOG.debug("insertDateUnitWorker->setOnFailed");
                pFormInsertDateUnit.close();
                pFormStart.close();
                DialogMessages.showExceptionAlert(insertDateUnitWorker.getException());
                insertDateUnitWorker.cancel();
                Main.quit(null);
            });
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
            Main.quit(null);
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
            mainContainer.setScreen(Screens.ORGANIZATION);

            this.primaryStage.getIcons().add(new Image(UserInterfaceConstants.MAIN_WINDOW_ICO));
            this.primaryStage.setTitle(UserInterfaceConstants.MAIN_WINDOW_TITLE);
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
        ProgressIndicatorForm pFormQuit = new ProgressIndicatorForm();
        try {
            LOG.debug("Closing the applicatoin...");
            pFormQuit.activateProgressBar(quitWorker);

            ((Service<Void>) quitWorker).setOnSucceeded(event -> {
//                https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
                Platform.exit();
                System.exit(0);
            });
            ((Service<Void>) quitWorker).setOnFailed(event -> {
                pFormQuit.close();
                DialogMessages.showExceptionAlert(quitWorker.getException());
                quitWorker.cancel();
                Platform.exit();
                System.exit(0);
            });
            pFormQuit.show();
            ((Service<Void>) quitWorker).restart();
        } catch (Throwable t) {
            pFormQuit.close();
            if(quitWorker != null){
                quitWorker.cancel();
            }
            DialogMessages.showExceptionAlert(t);
        }
    }
}
