package com.makco.smartfinance.user_interface;

import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.constants.Screens;
import java.util.EnumMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Created by mcalancea on 2016-04-01.
 */
//public class ScreensController extends VBox {
public class ScreensController extends BorderPane {
    private final static Logger LOG = LogManager.getLogger(ScreensController.class);
    EnumMap<Screens, Node> screens = new EnumMap<>(Screens.class);

    public ScreensController(){
        super();
        setPrefHeight(USE_COMPUTED_SIZE);
        setPrefWidth(USE_COMPUTED_SIZE);
//        setStyle("-fx-background-color: #336699;");
//        setPrefWidth(400f);
//        setPrefHeight(640f);
//        setAlignment(Pos.CENTER);
        loadVbox();
//        setVgrow(getChildren().get(0), Priority.ALWAYS);
        LOG.debug("ScreensController.constr");
    }

    public void addScreen(final Screens screen, final Node screenNode) {
        screens.put(screen, screenNode);
        LOG.debug(String.format("ScreensController.addScreen: name=%s; Node.screen=%s", screen.toString(), screenNode.toString()));
    }

    public Node getScreen(final String name) {
        return screens.get(name);
    }


    public boolean loadVbox(){

        LOG.debug("ScreensController.loadVbox");
        try {
            VBox vbox = new VBox();
//            vbox.setStyle("-fx-background-color: red;");
//            vbox.setPadding(new Insets(10));
//            vbox.setSpacing(8);
            setTop(vbox);
            vbox.getChildren().addAll(
                    loadMenuBar(),
                    loadToolBar()
            );

            return true;
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
            return false;
        }
    }
    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
    public Parent loadMenuBar(){
        LOG.debug("ScreensController.loadMenuBar");
        Parent menuBar = null;
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("menu_bar.fxml"));
            menuBar = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenPage(this);
//            setTop(menuBar);
//            getChildren().add(menuBar);
//            setTopAnchor(menuBar, );
            return menuBar;
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return menuBar;
    }

    public Parent loadToolBar(){
        LOG.debug("ScreensController.loadToolBar");
        Parent toolBar = null;
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("tool_bar.fxml"));
            toolBar = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenPage(this);
//            setTop(toolBar);
//            getChildren().add(menuBar);
//            setTopAnchor(menuBar, );
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return toolBar;
    }

    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
//    public boolean loadScreen(final String name, final String resource){
    public boolean loadScreen(final Screens screen){
        LOG.debug(String.format("ScreensController.loadScreen: name=%s; resource=%s", screen, screen.getFxmlFilePath()));
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(screen.getFxmlFilePath()));
            Parent childScreenNode = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenPage(this);
            addScreen(screen, childScreenNode);
            return true;
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
            return false;
        }
    }

    //This method tries to display the screen with a predefined name.
    //First it makes sure the screen has been already loaded. Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    //If there isn't any screen being desplayed, the new screen is just added to the root.
    public boolean setScreen(final Screens screen) {
        LOG.debug(String.format("ScreensController.setScreen: name=%s", screen.toString()));
        LOG.debug(String.format("getChildren().size()=%d", getChildren().size()));
        Node child = screens.get(screen);
        if (child != null) {
            if (!getChildren().isEmpty() && getChildren().size() > 1) {
                LOG.debug("getChildren() is greater than 1");

                LOG.debug("children:"+getChildren().size());
                getChildren().remove(1);
//                getChildren().add(1, child);

//                setVgrow(child,Priority.ALWAYS);
            } else {
                LOG.debug("getChildren() is less or equal than 1");
//                getChildren().add(screens.get(screen));
//                getChildren().add(child);
            }
            setCenter(child);
            return true;
        } else {
            DialogMessages.showExceptionAlert(new Exception(screen+" -> screen hasn't been loaded!!!"));
            return false;
        }

        /*
        Node screenToRemove;
        if(screens.get(name) !- null){ //screen loaded
            if(!getChildren().isEmpty()){ //if there is more than one screen
                getChildren().add(0, screens.get(name)); //add the screen
                screentoRemove = getChildren().get(1);
                getChildren().remove(1);
            } else {
                LOG.error(name + "-> screen hasn't been loaded!!!");
                return false;
            }
         */
    }

    //this method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(final Screens screen){
        LOG.debug(String.format("ScreensController.unloadScreen: name=%s", screen.toString()));
        if(screens.remove(screen) == null) {
            DialogMessages.showExceptionAlert(new Exception(screen.toString() + "->screen didn't exist"));
            return false;
        } else {
            return true;
        }
    }
}
