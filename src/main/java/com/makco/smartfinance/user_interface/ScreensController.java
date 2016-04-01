package com.makco.smartfinance.user_interface;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class ScreensController extends StackPane {
    private final static Logger LOG = LogManager.getLogger(ScreensController.class);
    private HashMap<String, Node> screens = new HashMap<>();

    public ScreensController(){
        super();
    }

    public void addScreen(final String name, final Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(final String name) {
        return screens.get(name);
    }

    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
    public boolean loadScreen(final String name, final String resource){
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e){
            LOG.error("!>>>ScreensController.loadScreen:",e);
            return false;
        }
    }

    //This method tries to display the screen with a predefined name.
    //First it makes sure the screen has been already loaded. Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    //If there isn't any screen being desplayed, the new screen is just added to the root.
    public boolean setScreen(final String name) {
        if (screens.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();
            if (!getChildren().isEmpty()) {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1_000), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                getChildren().remove(0);
                                getChildren().add(0, screens.get(name));
                                Timeline fadeIn = new Timeline(
                                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                        new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0))
                                );
                                fadeIn.play();
                            }
                        },
                                new KeyValue(opacity, 0.0)
                        ));
                fade.play();
            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(2_500), new KeyValue(opacity, 1.0))
                );
                fadeIn.play();
            }
            return true;
        } else {
            LOG.error(name+" -> screen hasn't been loaded!!!");
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
    public boolean unloadScreen(final String name){
        if(screens.remove(name) == null) {
            LOG.error(name + "->screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}
