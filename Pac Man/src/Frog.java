/*
@ASSESSME.INTENSITY:LOW
*/
//IMPORTS

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Frog extends Pane implements EventHandler<ActionEvent>, Runnable {

    /**
     * ImageView for the Frog. A container for the images. 
     */
    private ImageView frogView;
    /**
     * First Image of the frog. Front view of the screen. 
     */
    private Image frog1 = new Image(getClass().getResourceAsStream("frog1.png"));
    /**
     * Second image of the frog. Sideways view, turned to the right of the screen. 
     */
    private Image frog2 = new Image(getClass().getResourceAsStream("frog2.png"));


    /**
     * Basic Frog constructor. 
     */
    public Frog() {
        frogView = new ImageView(frog1);
        frogView.setFitHeight(frog1.getHeight() / 10);
        frogView.setFitWidth(frog1.getWidth() / 10);
        setWidth(frogView.getFitWidth());
        setHeight(frogView.getFitHeight());
        frogView.setPreserveRatio(true);
        getChildren().add(frogView);
        setTranslateY(500);
        setTranslateX(450);
    }


    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < 180; i++) {
                setTranslateX(getTranslateX() - 1);
                setTranslateY(getTranslateY() + 2);
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            frogView.setImage(frog2);

            for (int i = 0; i < 320; i++) {
                setTranslateX(getTranslateX() + 1);
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            frogView.setImage(frog1);

            for (int i = 0; i < 180; i++) {
                setTranslateY(getTranslateY() - 2);
                setTranslateX(getTranslateX() - 1);
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }









    
}