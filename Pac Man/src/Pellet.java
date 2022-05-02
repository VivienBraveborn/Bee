
/*
@ASSESSME.INTENSITY:LOW
*/
//IMPORTS
import java.io.*;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.Random;

public class Pellet extends Pane implements EventHandler<ActionEvent> {

    // Attributes
    private ImageView flowerView;
    private Image flower = new Image(getClass().getResourceAsStream("redflower.png"));

    // Constructor
    public Pellet(ImageView img) {
        // if(!img.getImage().getPixelReader().getColor((int)(this.getTranslateX()),
        // (int)(this.getTranslateY())).equals(Color.RED)){
        flowerView = new ImageView(flower);
        flowerView.setFitHeight(flowerView.getImage().getHeight() / 40);
        flowerView.setFitWidth(flowerView.getImage().getWidth() / 40);
        setHeight(flowerView.getFitHeight());
        setWidth(flowerView.getFitWidth());
        getChildren().add(flowerView);

        Random rand = new Random();
        setTranslateX(rand.nextInt(1890));
        setTranslateY(rand.nextInt(1050));
    }

    // Fix#2 - opet witdh height
    public int collide(Bee bee) {
        BoundingBox bpellet = new BoundingBox(getTranslateX(), getTranslateY(), flowerView.getFitWidth(), flowerView.getFitHeight());
        BoundingBox bbee = new BoundingBox(bee.getTranslateX(), bee.getTranslateY(), 61, 61);

        if (bpellet.intersects(bbee)) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub

    }

}
