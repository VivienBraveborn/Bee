
/*
@ASSESSME.INTENSITY:LOW
*/
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.geometry.*;

import java.net.*;
import java.io.*;
import java.util.*;


/**
 * GUIStarter - Starter file for GUI examples
 * 
 * @author D. Patric, Alan Mutka
 * @version 2211
 */

public class Board extends Application implements EventHandler<ActionEvent> {
   // Attributes are GUI components (buttons, text fields, etc.)
   // are declared here.
   private Stage stage; // The entire window, including title bar and borders
   private Scene scene; // Interior of window

   private StackPane root = new StackPane();

   private Image beeIntroImage =  new Image("backgroundBees.png");

   private ImageView backgroundbase = new ImageView(new Image(getClass().getResourceAsStream("backgroundbase.png")));

   private Bee bee = new Bee(55, 0);
   private Bee bee2 = new Bee(55, (int) (1920 - bee.getWidth() - 2));
   private Frog frog = new Frog();
   private Map<Integer, Pellet> pelletmap = new TreeMap<>();
   private int pelletCount = 0;
   private int score = 0;
   private AnimationTimer timer;
   private TextField tfPlayerName = new TextField("Player 1");
   private TextArea chat = new TextArea();
   private TextField chatToSend = new TextField();
   private Scene instru;

   // CLIENT STUFF
   private Socket socket;
   private ObjectOutputStream oos;
   private ObjectInputStream ois;
   private static final int SERVER_PORT = 8543;

   private int clientCount = 0;

   private long lastFrame;
   private long accumulated;

   // Main just instantiates an instance of this GUI class
   public static void main(String[] args) {
      launch(args);
   }

   // Called automatically after launch sets up javaFX
   // TRY TO SET US INTRODUCTION SCENE SO YOU CAN MAKE A INTRODUCTION SCREEN TO
   // READ OVER THE RULES

   public void start(Stage _stage) throws Exception {
      stage = _stage; // save stage as an attribute
      stage.setTitle("Bee"); // set the text in the title bar

      backgroundbase.setVisible(false);
      BackgroundImage backgroundIntro = new BackgroundImage(beeIntroImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
      Background backgroundintro = new Background(backgroundIntro);


      Thread tfrog = new Thread(frog);
      tfrog.start();

      EventHandler<KeyEvent> key = new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent e) {
            if(clientCount==1){
               bee.move(e, stage.getHeight(), stage.getWidth());
               bee.collide(backgroundbase, e, frog);
            }
            
            if(clientCount==2){
               bee2.move(e, stage.getHeight(), stage.getWidth());
               bee2.collide(backgroundbase, e, frog);
            }

            for (int key : pelletmap.keySet()) {
               // System.out.println(pelletmap.get(key).collide(bee));
               if (pelletmap.get(key).collide(bee) == 1) {
                  score++;
                  System.out.println("SCORE\n" + score);
                  // System.out.println("DOES THE ROOT CONTAIN THE KEY\n" +
                  // root.getChildren().contains(pelletmap.get(key)));
                  // System.out.println("LISTING BEFORE REMOVING THE ROOT\n" +
                  // root.getChildren());
                  root.getChildren().remove(pelletmap.get(key));
                  // System.out.println("LISTING AFTER DELETING FROM ROOT\n" +
                  // root.getChildren());
                  pelletmap.remove(key);
               }
            }

            if(e.getCode() == KeyCode.ENTER){
               if(chat.isDisable()){
                  chat.requestFocus();
                  chat.setDisable(false);
                  chatToSend.setDisable(false);
               }
               else if(!chat.isDisable()){
                  root.requestFocus();
                  chat.setDisable(true);
                  chatToSend.setDisable(true);
                  sendmessage();
                  chatToSend.clear();
               }
               
            }

            
         }
      };

      stage.addEventHandler(KeyEvent.KEY_PRESSED, key);

      VBox chatBox = new VBox(8, chatToSend);
      
      root.getChildren().addAll(bee, bee2, frog, backgroundbase, chat, chatBox);
      root.setAlignment(chatBox, Pos.BOTTOM_LEFT);

      chat.setDisable(true);
      chatToSend.setDisable(true);
      chat.setMaxSize(200, 200);
      chat.setEditable(false);
      chatToSend.setMaxSize(200, 50);
      


      BackgroundImage backgroundBee = new BackgroundImage(new Image("background1.png"), BackgroundRepeat.REPEAT,
            BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
      Background background = new Background(backgroundBee);
      root.setBackground(background);
      stage.setFullScreen(true);

      if (clientCount == 1) {
         while (pelletCount < 25) {
            Pellet pellet = new Pellet(backgroundbase);
            if (backgroundbase.getImage().getPixelReader()
                  .getColor((int) (pellet.getTranslateX()), (int) (pellet.getTranslateY())).equals(Color.RED) ||
                  backgroundbase.getImage().getPixelReader()
                        .getColor((int) (pellet.getTranslateX() + pellet.getHeight()), (int) (pellet.getTranslateY()))
                        .equals(Color.RED)
                  ||
                  backgroundbase.getImage().getPixelReader()
                        .getColor((int) (pellet.getTranslateX()), (int) (pellet.getTranslateY() + pellet.getHeight()))
                        .equals(Color.RED)
                  ||
                  backgroundbase.getImage().getPixelReader()
                        .getColor((int) (pellet.getTranslateX() + pellet.getWidth()),
                              (int) (pellet.getTranslateY() + pellet.getHeight()))
                        .equals(Color.RED)) {

               continue;
            } else {
               pelletCount++;
               pelletmap.put(pelletCount, pellet);
               root.getChildren().add(pellet);

            }
         }
      }

      Button btnStart = new Button("Start");
      Button btnInstructions = new Button("Instructions");
      btnInstructions.setPrefHeight(150);
      btnInstructions.setPrefWidth(200);
      btnInstructions.setOnAction(this);

      btnStart.setMaxHeight(50);
      btnStart.setMaxWidth(100);

      btnInstructions.setMaxHeight(50);
      btnInstructions.setMaxWidth(100);

      tfPlayerName.setMaxSize(400, 200);

      VBox rooty = new VBox();
      VBox buttons = new VBox();
      buttons.setAlignment(Pos.CENTER);
      buttons.getChildren().addAll(btnStart, btnInstructions);
      rooty.getChildren().addAll(buttons, tfPlayerName);
      rooty.setAlignment(Pos.CENTER);
      btnStart.setOnAction(this);

      Scene scene1 = new Scene(rooty, 1500, 1000);

      scene = new Scene(root, 1500, 1000); // create scene of specified size
                                           // with given layout

      HBox root2 = new HBox();
      TextField tfEND = new TextField();
      tfEND.setEditable(false);
      root2.getChildren().add(tfEND);

      Scene scene2 = new Scene(root2, 1500, 1000);


      Button btnStartGame = new Button("Start");
      btnStartGame.setOnAction(this);
      VBox isturoot = new VBox();
      isturoot.setBackground(backgroundintro);
      TextArea instructionArea = new TextArea("Basic movement consists of WASD keys. Collect flowers along the way to finish the game. Oh, and a small tip. Watch out for the frog. It's not friendly today.");
      instructionArea.setMaxSize(1000, 1000);
      isturoot.setAlignment(Pos.CENTER);
      isturoot.getChildren().addAll(instructionArea, btnStartGame);
      instru = new Scene(isturoot, 1500, 1000);

      
      rooty.setBackground(backgroundintro);
      stage.setScene(scene1);
      stage.show();

      root.setBackground(background);
      // stage.setScene(scene); // associate the scene with the stage
      // stage.show();

      // TRY TO SWAP SCENES!!! so you can define the end of the game!!!
      timer = new AnimationTimer() {
         
         @Override
         public void handle(long now) {
         accumulated = now-lastFrame;
         lastFrame = now;
         if(accumulated>=2000){
            sendPackage();
         }
            if (score >= 2) {
               stage.setScene(scene2);
               stage.show();
            }

         }

      };

      // display the stage (window)

   }

   public void handle(ActionEvent evt) {
      Button btn = (Button)evt.getSource();
      switch(btn.getText()){
         case "Start":
            connect();
            stage.setScene(scene);
            stage.setFullScreen(true);
            break;
         case "Instructions":
            stage.setScene(instru);
            stage.setFullScreen(true);
            break;
      }
      
   }

   public void connect() {
      try {
         this.socket = new Socket("localhost", SERVER_PORT);

         this.oos = new ObjectOutputStream(this.socket.getOutputStream());
         this.ois = new ObjectInputStream(this.socket.getInputStream());

         // TODO SET UP STUFF TO WRITE FOR THE SERVER TO RECEIVE LIKE NAME AND ID - HAVE
         // SERVER MAYBE TO GIVE THE ID?
         this.oos.writeObject("STARTING@"+tfPlayerName.getText().trim());

         ClientThread th = new ClientThread();
         th.start();

      } catch (UnknownHostException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   class ClientThread extends Thread {
      @Override
      public void run() {

         while (true) {
            try {
               Object obj = ois.readObject();
               if(obj instanceof String){
                  String text = (String)obj;
                  chat.appendText(text+"\n");
               }

               if(obj instanceof Integer){
                  int index = (int)obj;
                  clientCount = index;
               }

               if(obj instanceof Package){
                  Package pack = (Package)obj;
                  if(clientCount==1){
                     bee2.setTranslateX(pack.getBee().getTranslateX());
                     bee2.setTranslateY(pack.getBee().getTranslateY());
                  }
                  if(clientCount!=1){
                  bee2.setTranslateX(pack.getBee().getTranslateX());
                  bee2.setTranslateY(pack.getBee().getTranslateY());
                  //frog.setTranslateX(pack.getFrog().getTranslateX());
                  //frog.setTranslateY(pack.getFrog().getTranslateY());
                  }
                  for(int key: pelletmap.keySet()){
                     root.getChildren().remove(pelletmap.get(key));
                  }
                  for(int key: pelletmap.keySet()){
                     root.getChildren().add(pelletmap.get(key));
                  }
                  score = pack.getScore();
               }

            } catch (ClassNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }

   public void sendPackage(){
      try {
         Package packages = new Package(bee, frog, pelletmap, score);
         this.oos.writeObject(packages);
         this.oos.flush();


      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void sendmessage(){
      try {
         this.oos.writeObject("CHAT@"+this.tfPlayerName.getText() + ": " + chatToSend.getText());
         this.oos.flush();

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
