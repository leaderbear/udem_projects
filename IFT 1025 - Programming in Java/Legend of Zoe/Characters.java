public abstract class Characters  extends  LevelObjects{


    /**Attributs globales de tout personnages dans le jeu**/
    private int hp;

    /**
     * Constructeur de la classe des personnages du jeu comme Zoe et les monstres qui ont des hp
      * @param x position x du personnage
     * @param y position y du personnage
     */
    public Characters(int x, int y){
        super(x,y);
    }

    /** Methodes globales de toute personnages dans le jeu**/
    public int getHp() {
        return hp;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }


}
