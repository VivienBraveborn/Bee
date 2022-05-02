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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.geometry.*;

import java.net.*;
import java.io.*;
import java.util.*;




public class BeeServer extends Application implements EventHandler<ActionEvent> {
  
/**
 * GUIStarter - Starter file for GUI examples
 * 
 * @author D. Patric, Alan Mutka
 * @version 2211
 */


    // Attributes are GUI components (buttons, text fields, etc.)
    // are declared here.
    private Stage stage; // The entire window, including title bar and borders
    private Scene scene; // Interior of window
 
    //private StackPane root = new StackPane();
 
    private ImageView backgroundbase = new ImageView(new Image(getClass().getResourceAsStream("backgroundbase.png")));
 
    private Bee bee = new Bee(55, 0);
    private Bee bee2 = new Bee(55, (int) (1920 - bee.getWidth() - 2));
    private Frog frog = new Frog();
    private Map<Integer, Pellet> pelletmap = new TreeMap<>();
    private int pelletCount = 0;
    private int score = 0;

    private Map<Integer, String> clientsInServer = new TreeMap<>();


    private VBox root = new VBox(16);
    private Label lblClients = new Label("Clients Connected");
    private TextArea taClients = new TextArea();
    





    //SERVER STUFF
    private static final int SERVER_PORT = 8543;
    //Will probably need a list to store stuff 
    private List<ObjectOutputStream> players = new LinkedList<>();









 
    // Main just instantiates an instance of this GUI class
    public static void main(String[] args) {
        launch(args);
    }
 
    // Called automatically after launch sets up javaFX
    //TRY TO SET US INTRODUCTION SCENE SO YOU CAN MAKE A INTRODUCTION SCREEN TO READ OVER THE RULES 
    public void start(Stage _stage) throws Exception {
       stage = _stage; // save stage as an attribute
       stage.setTitle("Bee"); // set the text in the title bar
 
       
        root.getChildren().addAll(lblClients, taClients);
        taClients.setPrefHeight(300);
        root.setAlignment(Pos.CENTER);














       scene = new Scene(root, 350, 700); // create scene of specified size
                                          // with given layout
 
       stage.setScene(scene); // associate the scene with the stage
       stage.show();
 
       //TRY TO SWAP SCENES!!! so you can define the end of the game!!! 
      
       // display the stage (window)
       ServerThread st = new ServerThread();
       st.start();
 
    }
 
    public void handle(ActionEvent evt) {

    }

    class ServerThread extends Thread{
        @Override
        public void run(){
            try{
                System.out.println("Opening Server");
                ServerSocket sSocket = new ServerSocket(SERVER_PORT);
               
               while(true){
                  Socket cSocket = sSocket.accept(); 

                  ClientThread cT = new ClientThread(cSocket);
                  cT.start();
               }
                

            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    class ClientThread extends Thread{
        private Socket cSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois; 

        public ClientThread(Socket cSocket){
            this.cSocket = cSocket;
        }

        @Override
        public void run(){
            try{
                this.ois = new ObjectInputStream(this.cSocket.getInputStream());
                this.oos = new ObjectOutputStream(this.cSocket.getOutputStream());

                players.add(this.oos);
                

                while(true){
                    Object obj = ois.readObject();
                    if(obj instanceof String){
                        String string = (String)obj;
                        String[] command = string.split("@");
                        if(command.length >= 2){
                            switch(command[0]){
                                case "STARTING":
                                    boolean added = false;
                                    int index = 1;
                                    while(!added){
                                        if(!clientsInServer.containsKey(index)){
                                            clientsInServer.put(index, command[1]);
                                            added = true;
                                            this.oos.writeObject(index);
                                        }
                                        else index++;
                                    }
                                    addClientToText();
                                    break;
                                case "CHAT": 
                                    for(int i=0; i<players.size();i++){
                                        taClients.appendText("\nMESSAGE SENT \n" + command[1]);
                                        players.get(i).writeObject(command[1]);
                                        players.get(i).flush();
                                    }
                                    break;
                                    
                            }
                        }
                    }
                    if(obj instanceof Package){
                        for(int i=0;i<players.size();i++){
                            if(players.get(i)!=this.oos){
                                Package pack = (Package)obj;
                                this.oos.writeObject(pack);
                            }
                        }
                    }
                }

            } 
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    private void addClientToText(){
        taClients.clear();
        for(int key:clientsInServer.keySet()){
            taClients.appendText(clientsInServer.get(key)+"\n");
        }
        
    }
 
 }
 