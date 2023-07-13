public abstract class Characters {

    private int range;
    public boolean state;

    private double xPosition, yPosition;
    private double speedX , speedY;

    //multiplicateur de vitesse
    private double difficultyX;


    /**
     *
     * @param newRange on set le range du ghost/obstacles
     * @param x et sa position x
     * @param y position y
     */
    public Characters(int newRange, double x, double y){
        this.range = newRange;
        this.xPosition = x;
        this.yPosition = y;
        this.state = true;
        this.difficultyX = 1.2;
    }

    /**
     *
     * @param deltaX variation de distance selon le temps (axe des x)
     * @param deltaY (axe des y)
     */
    public void move(double deltaX, double deltaY){
        xPosition += deltaX;
        yPosition += deltaY;
    }

    /**
     *
     * @param accX ajouter une accleration a la vitesse x
     * @param accY
     */
    public void accelerate(double accX, double accY){
        speedX += accX;
        speedY += accY;

    }

    /**
     * mettre à jour position et * vitesse par un taux de friction de 0.9 pour eviter qu'elle rebondit comme une balle
     */
    public void uptade(){
        move(speedX, speedY);
        speedX *= 0.9;
        speedY *= 0.9;
    }

    public int getRange(){
        return this.range;
    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }


    public boolean isState() {
        return state;
    }

    public void setState(boolean newState ){
        this.state = newState;
    }

    public void nextMove(){
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getDifficultyX() {
        return difficultyX;
    }

    public void setDifficultyX(double difficultyX) {
        this.difficultyX = difficultyX;
    }

    public double getSpeedY() {
        return speedY;
    }
}
