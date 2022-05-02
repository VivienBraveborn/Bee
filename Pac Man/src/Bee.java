
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

/**
 * Bee Class - player of the game 
 */
public class Bee extends Pane implements EventHandler<ActionEvent> {
    // ATTRIBUTES
    private ImageView beeView;
    private Image bee1 = new Image(getClass().getResourceAsStream("bee1.png"));
    private Image bee2 = new Image(getClass().getResourceAsStream("bee2.png"));
    private Image beeback1 = new Image(getClass().getResourceAsStream("beeback1.png"));
    private Image beeback2 = new Image(getClass().getResourceAsStream("beeback2.png"));
    private Image bee1flip = new Image(getClass().getResourceAsStream("bee1flipped.png"));
    private Image bee2flip = new Image(getClass().getResourceAsStream("bee2flipped.png"));
    private Image beeback1flip = new Image(getClass().getResourceAsStream("beeback1flipped.png"));
    private Image beeback2flip = new Image(getClass().getResourceAsStream("beeback2flipped.png"));

    // Movement
    int xlocation = 0;
    int ylocation = 0;
    int xmoved = 0;
    int ymoved = 0;

    /**
     * Collision method. Add image, key and frog to check for
     * @param img ImageView of the background for collision
     * @param key KeyEvent from the stage
     * @param frog Enemy player
     */
    public void collide(ImageView img, KeyEvent key, Frog frog) {
        if (img.getImage().getPixelReader()
                .getColor((int) (this.getTranslateX() + 20), (int) (this.getTranslateY() + 20)).equals(Color.RED) ||
                img.getImage().getPixelReader()
                        .getColor((int) (this.getTranslateX() + beeView.getFitHeight() - 20),
                                (int) (this.getTranslateY() + 20))
                        .equals(Color.RED)
                ||
                img.getImage().getPixelReader()
                        .getColor((int) (this.getTranslateX() + 20),
                                (int) (this.getTranslateY() + beeView.getFitHeight() - 20))
                        .equals(Color.RED)
                ||
                img.getImage().getPixelReader().getColor((int) (this.getTranslateX() + beeView.getFitWidth() - 20),
                        (int) (this.getTranslateY() + beeView.getFitHeight()) - 20).equals(Color.RED)) {

            if (key.getCode() == KeyCode.W) {
                setTranslateY(getTranslateY() + 10);
            } else if (key.getCode() == KeyCode.A) {
                setTranslateX(getTranslateX() + 10);
            } else if (key.getCode() == KeyCode.D) {
                setTranslateX(getTranslateX() - 10);
            } else if (key.getCode() == KeyCode.S) {
                setTranslateY(getTranslateY() - 10);

            }
        }

        // FIX 1 - the width and height were not good
        BoundingBox bbee = new BoundingBox(getTranslateX(), getTranslateY(), 50, 50);
        BoundingBox bfrog = new BoundingBox(frog.getTranslateX(), frog.getTranslateY(), 50, 50);

        if (bbee.intersects(bfrog)) {
            setTranslateX(0);
            setTranslateY(55);
            updateBounds();
        }

    }

    // CONSTRUCTOR
    /**
     * Constructor for the player class
     * @param y position on the stage - must be integer
     * @param x position on the stage - must be integer
     */
    public Bee(int y, int x) {
        beeView = new ImageView(bee1);
        beeView.setFitHeight(bee1.getHeight() / 18.5);
        beeView.setFitWidth(bee1.getWidth() / 18.5);
        this.setHeight(beeView.getFitHeight());
        this.setWidth(beeView.getFitWidth());
        beeView.setPreserveRatio(true);
        this.getChildren().add(beeView);
        if (x != 0) {
            beeView.setImage(bee1flip);
        }
        setTranslateY(y);
        setTranslateX(x);
    }

    /**
     * Method for basic movement of the player. Uses basic WASD movement. 
     * @param key
     * @param stagey
     * @param stagex
     */
    public void move(KeyEvent key, double stagey, double stagex) {
        if (key.getCode() == KeyCode.W) {
            if (getTranslateY() <= 0)
                setTranslateY(0);
            else
                setTranslateY(getTranslateY() - 10);
            if (beeView.getImage().equals(beeback1)) {
                beeView.setImage(beeback2);
            } else
                beeView.setImage(beeback1);
        } else if (key.getCode() == KeyCode.A) {
            if (getTranslateX() <= 0)
                setTranslateX(0);
            else
                setTranslateX(getTranslateX() - 10);
            if (beeView.getImage().equals(bee1flip)) {
                beeView.setImage(bee2flip);
            } else {
                beeView.setImage(bee1flip);
            }
        } else if (key.getCode() == KeyCode.D) {
            if (getTranslateX() >= stagex - beeView.getFitWidth())
                setTranslateX(stagex - beeView.getFitWidth());
            else
                setTranslateX(getTranslateX() + 10);
            if (beeView.getImage().equals(bee1)) {
                beeView.setImage(bee2);
            } else
                beeView.setImage(bee1);
        } else if (key.getCode() == KeyCode.S) {
            if (getTranslateY() >= stagey - beeView.getFitHeight())
                setTranslateY(stagey - beeView.getFitHeight());
            else
                setTranslateY(getTranslateY() + 10);
            if (beeView.getImage().equals(beeback1flip)) {
                beeView.setImage(beeback2flip);
            } else
                beeView.setImage(beeback1flip);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub

    }

}