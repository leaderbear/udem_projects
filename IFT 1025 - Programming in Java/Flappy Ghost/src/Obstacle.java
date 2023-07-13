import java.util.Random;

public class Obstacle extends Characters {

    private int type;
    private boolean sinusIntier = true;
    private double actualSpeed = 3;
    private double orginalY = getyPosition();
    private boolean colision;
    private int apparence;


    private Random random = new Random();


    /**
     * Constructeur d'un obstacle
     * @param range le rayon de l'obstacle
     * @param x position X
     * @param y position Y
     * @param newType son type (1 : simple , 2 : sinus , 3 : quantique)
     * @param difficultX ( multiplicateur de deplacement pour simuler la vitesse du Ghost)
     */
    public Obstacle(int range, double x, double y, int newType, double difficultX) {


        super(range, x, y);
        this.colision = false;
        this.apparence = random.nextInt(27);
        this.setDifficultyX(difficultX);

        switch (newType) {
            case 1:
                this.type = 1;
                break;
            case 2:
                this.type = 2;
                break;
            case 3:
                this.type = 3;
                break;
        }
    }


    /**
     *  Prochaine move du monstre
     * @param dt selon le temps
     */
    public void nextMove(double dt) {

        switch (type)  {
            case 1 :
            {move(-getDifficultyX(), 0);}
            break;

            case 2 :

            if (sinusIntier == true){move(-getDifficultyX(), 0.25); sinusIntier = false;}
            else if(orginalY - getyPosition() <= -25 - getRange() || getyPosition() >= 400){actualSpeed*=-1;}
            else if(orginalY - getyPosition() >= 25 + getRange() || getyPosition() <= 0){actualSpeed*=-1;}
            {move(-getDifficultyX(), actualSpeed);}
            break;

            case 3 :
            {move(-getDifficultyX(), 0);} //TODO : deplacement bizarres

            if(dt % 200 == 0){
                int nextX = random.nextInt(61); //genere chiffre [0,60]
                int nextY = random.nextInt(61);

                if (nextX > 30){nextX = (60 - nextX)*-1;} //si chiffre>30 , alors partie negatif [-30,0]
                if (nextY > 30){nextY = (60- nextY)*-1;}

                if (nextY + getyPosition() <= 0){nextY = + 30;}
                if (nextY + getyPosition() >= 400){nextY= - 30;}//no out of bound

                setxPosition(getxPosition() + nextX); setyPosition(getyPosition()+ nextY );
            }

            break;

        }
    }

    /**
     *
     * @return s'il ya une collision  avec le ghost ou pas
     */
    public boolean getColision() {
        return colision;
    }

    /**
     *
     * @param c dire que cet obstacle est en collision avec ghost
     */
    public void setColision(boolean c) {
        colision = c;
    }

    /**
     *
     * @return le int (0 a 26) qui correspond a l'apprence du monstre dans le dossier images/obstacles/
     */
    public int getApparence() {
        return apparence;
    }
}



