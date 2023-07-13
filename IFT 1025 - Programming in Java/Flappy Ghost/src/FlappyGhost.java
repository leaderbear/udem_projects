/**
 * @author
 * Abderrahim Tabta 20133680
 * Guilhem Robert 20125535
 */


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FlappyGhost {

    //variable utiles dans la mécanique de jeu (modèle)
    private Level lvl;
    private boolean debug;
    private int score;
    private int counter = 0;
    private double bgSpeed = 0;
    private double difficultyX;
    public  boolean pause = false;

    /**
     * Constrcteur principale, creer le jeu et lance un timer qui ajoute des obstacles toutes les 3secondes
     */
    public  FlappyGhost() {

        this.debug = false;
        lvl = new Level(640, 400);
        this.score = -5;
        this.difficultyX = 2.1;

        Timer timer = new Timer();
        timer.schedule(setAddMonsterT, 0, 3000);
    }


    /**
     * call next move(vitesse/position/etc...) de chaque obstacles et ghoste
     */
    public void next() {
        check();
        lvl.getGhost().nextMove();
        for (int i = 0; i < lvl.getObstacle().size(); i++) {
           lvl.getObstacle().get(i).nextMove(lvl.getDeltaTime());
        }
    }

    /**
     * Verifier les collisions et si obstacle depasse monstres il est "desactivé" pour pas donner + de pts
     * et met a jour le score , met aussi a jour la difficulté du jeu (gravité/vitesse)
     */
    public void check(){

        for (int i = 0; i < lvl.getObstacle().size(); i++ ){

            Obstacle obs = lvl.getObstacle().get(i);
            int range = obs.getRange();
            double x =  obs.getxPosition();

            if ( obs.isState() && checkSkip(obs, lvl.getGhost())  ){
                this.score += 5;
                counter++;
                this.getLvl().getObstacle().get(i).setState(false);

                if(counter != 0 && counter%2==0){
                    this.difficultyX+=0.2525;
                    bgSpeed++;
                    lvl.getGhost().setGravity(lvl.getGhost().getGravity()+0.05);
                }
            }

            else if ( x + range * 2 < 0){ lvl.getObstacle().remove(i);}

            else if (checkCollision(lvl.getObstacle().get(i) , lvl.getGhost())){

                if (!debug){resetGame();}
                else{ lvl.getObstacle().get(i).setColision(true);}}

            else { lvl.getObstacle().get(i).setColision(false); }
        }
    }


    /**
     *
     * @param obst est ce qu'il ya une collision entre l'obstacle
     * @param ghost et le le ghost
     * @return si ya collision return true , else return false
     */
    public boolean checkCollision(Obstacle obst, Ghost ghost){

        double dx = obst.getxPosition() - ghost.getxPosition();
        double dy = obst.getyPosition() - ghost.getyPosition();
        double dCarre = dx * dx + dy * dy;

        return dCarre < (obst.getRange() + ghost.getRange()) * (obst.getRange() + ghost.getRange());

    }

    /**
     *
     * @param obs verifier si cet obstacle a deja depasser
     * @param ghost le ghost
     * @return return true si obstacle a depasse monstre
     */
    public boolean checkSkip(Obstacle obs, Ghost ghost) {

        double obsDX = obs.getxPosition() ;
        double ghostDX = ghost.getxPosition();

        if (obsDX >= ghostDX){return true;}
        return false;

    }

    /**
     * remettre jeu a 0 ((difficulté, les obstacles, ghost, tout...)
     */
    public void resetGame(){
        this.score = -5   ;
        lvl.resetObstacles();
        difficultyX = 2.1;
        bgSpeed=0;
        counter=0;
        lvl.getGhost().setGravity(0.3);
    }

    /**
     * genrer un nouvel obstacle au hasard a une position y au hasard avec un range au hasard
     */
    public void generateObs(){

        Random random = new Random();

        //range and type of the obsacle
        int type = random.nextInt(3);
        int range = random.nextInt(45-10) + 10;

        //coords
        int newX = 640+range*2;
        int newY = random.nextInt(400-range*3);

        //add obstacle to the obstacles
        if (!pause){
        Obstacle newObs = new Obstacle(range,newX, newY, type +1 , this.difficultyX);
        lvl.getObstacle().add(newObs);}
        else{}
    }


    /**
     * utile pour le timer afin d'ajouter un monstre tt les 3 sec . Ps si jeu en pause, la fonction que cette tache
     * appelle s'occupe d'arreter de générer des monstres
     */
    TimerTask setAddMonsterT = new TimerTask() {
        @Override
        public void run() {
            generateObs();
        }
    };

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug(){
        return debug;
    }

    public int getScore() {
        return score;
    }

    public double getBgSpeed() {
        return bgSpeed;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Level getLvl() {
        return lvl;
    }
}
