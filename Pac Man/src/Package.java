import java.util.*;

/*
@ASSESSME.INTENSITY:LOW
*/

/**
 * <pre>
 * Class for gathering information to send over the server. 
 * Inputs that are required are Bee, Frog and a Map containing Pellets as the value input.
 * Integers are required for the key input.
 */
public class Package {
    //Attributes 
    /**
     * Frog attribute for the Package class. Accepts the Frog input.
     */
    private Frog frog;
    private Bee bee;
    private Map<Integer, Pellet> pelletmap = new TreeMap<>();
    private int score;

    public Package(Bee bee, Frog frog, Map<Integer, Pellet> pelletmap, int score){
        this.bee = bee;
        this.frog = frog;
        this.pelletmap = pelletmap;
        this.score= score;
    }

    public Bee getBee(){
        return this.bee;
    }

    public Frog getFrog(){
        return this.frog;
    }

    public Map<Integer, Pellet> getMap(){
        return this.pelletmap;
    }

    public int getScore(){
        return this.score;
    }

    
}
