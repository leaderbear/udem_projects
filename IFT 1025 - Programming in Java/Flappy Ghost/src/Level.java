import java.util.ArrayList;

public class Level {

    public double deltaTime;
    private Ghost ghost;
    boolean state;
    private ArrayList<Obstacle> obstacles= new ArrayList<>(1);

    private int width;
    private int height;

    /**
     * constructeur d'un niveau avec ses monstres/ghost
     * @param w largeur du niveau
     * @param h hauteur du niveau
     */
    public Level(int w, int h){
        this.ghost = new Ghost(30,320,200, 0.3);
        this.state=true;
        this.width = w;
        this.height = h;
        this.deltaTime = 0;
    }

    public Ghost getGhost() {
        return ghost;
    }

    public ArrayList<Obstacle> getObstacle() {
        return obstacles;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void resetObstacles(){
        this.obstacles = new ArrayList<>();
    }

}
