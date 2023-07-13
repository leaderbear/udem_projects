public class Ghost extends Characters{

    private double gravity;

    public Ghost(int range, double x, double y, double gravityDifficulity){
        super(range,x,y);
        this.gravity = gravityDifficulity;
    }


    /**
     * fonction qui fait sauter fantome vers le haut en s'assurant qu'il depasse pas une vitesse de 300px/s
     * ps s'il essaye de depasser la limite en haut il rebondit vers le bas
     */
    public void jump(){
        if (getSpeedY() <= -5.25){accelerate(0,5);} //-5.25 = environ 300px/s vers le haut, a pas depasser
        else if (getyPosition() <= 0 ){accelerate(0,3);
        }else { accelerate(0, -16);}


    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getGravity() {
        return gravity;
    }

    @Override
    /**
     * Ici on override nextmove pour ajouter l'Effet de gravité au ghost qui n'est pas sur les obstacles
     */
    public void nextMove(){
        accelerate(0, gravity);
        if( getyPosition() + getRange()*2 >= 400){accelerate(0,-10);}
        else if (getyPosition()  <= 0 ){accelerate(0,5);}
    }


}