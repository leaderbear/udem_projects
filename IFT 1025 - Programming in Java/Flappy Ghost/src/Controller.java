public class Controller {

    private FlappyGhost vue;

    public Controller(FlappyGhost vue){
        this.vue = vue;
    }

    /**
     * Get le ghost du lvl et le fait sauter (acceleration vers le haut)
     */
    public void sauter(){
        this.vue.getLvl().getGhost().jump();
    }

    /**
     * Call la methode next du niveau (donc fait avancer obstacles et uptade aussi la vitesse/position du ghost)
     */
    public void next(){
        this.vue.next();
        this.vue.getLvl().getGhost().uptade();
    }

    /**
     *
     * @return combien d'obstacles il ya dans la partie
     */
    public int obstaclesCount(){
        return this.vue.getLvl().getObstacle().size();
    }

    /**
     *
     * @param i index dans l'array des obstacles dans la classe Level
     * @return retourne l'obstacle i
     */
    public Obstacle getObsI(int i){
        return this.vue.getLvl().getObstacle().get(i);
    }

    /**
     *
     * @param dt set le deltatime dans le niveau
     */
    public void setdTime(double dt){
        this.vue.getLvl().setDeltaTime(dt);
    }

    public double getdTime(){
        return this.vue.getLvl().getDeltaTime();
    }

    /**
     *
     * @param i regarde si l'obstacle i dans l'array
     * @return true si il ya collision et false s'il ya pas de collision
     */
    public boolean checkColision(int i) {
        return this.vue.getLvl().getObstacle().get(i).getColision();
    }

    /**
     *
     * @param bo true si le mode debug est activé sinon déscative le mode debug dans la classe Lvl
     */
    public void setDebug(Boolean bo){
        this.vue.setDebug(bo);
    }

    /**
     *
     * @return le score actuel du joueur dans la classe lvl
     */
    public int getScore(){
        if (this.vue.getScore()<0){return 0;}
        return this.vue.getScore();
    }

    /**
     *
     * @return a quelle vitesse de plus devrait aller le background
     */
    public double getBgSpeed(){
        return this.vue.getBgSpeed();
    }

    public void setPause(Boolean pause){
        this.vue.setPause(pause);
    }
}
