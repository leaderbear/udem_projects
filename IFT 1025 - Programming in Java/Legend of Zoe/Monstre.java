public class Monstre extends Characters {

    private boolean etat;
    private String item;
    private int force;
    private int hp;

    /**
     * Constructeur des monstres qui sont les ennemis principales du jeu et les seuls capable de tuer Zoe
     * @param item l'item contenu dans le monstre et qui va etre jete quand monstre tue
     * @param x position x du monstre
     * @param y position y du monstre
     * @param niveau numero du niveau indique sa difficulte au monstre , donc ses points de vie et sa force d'attaque
     */
    public Monstre(String item, int x, int y, int niveau){
        super(x,y);
        setApparence('@');
        this.item = item;
        this.etat = true;
        this.hp = ( (int) Math.max((0.6*niveau), 1) );
        this.force = ( (int) Math.max(0.4*niveau,1));
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
        if (etat == false){setApparence('x'); }
    }

    /**
     *
     * @param map la carte sur laquelle le monstre se trouve, si monstre est vivant (etat = true) :
     *            Si Zoe proche, le monstre l'attaque sinon la cherche et se dirige vers elle si possible
     *
     */
    public void nextMove(Level map){

        if (this.etat == false){} //si monstre mort, rien faire. S'il est vivant, il va agir

        else {

            int nextX; int nextY;
            int zoeX = map.getZoe().getPositionX();
            int zoeY =  map.getZoe().getPositionY();
            int x = getPositionX(); int y = getPositionY();

            if (((zoeX == x) || (zoeX-1 == x) || (zoeX+1 == x)) &&
                    ((zoeY == y) || (zoeY-1 == y) || (zoeY+1 == y))){
                attaquerZoe(map);}

            else {

                //Case ou le monstre doit aller pour se rapprocher de Zoe , ensuite voir si c'est possible d'y aller
                if (x < zoeX){nextX = x+1; }else if(x > zoeX){nextX = x-1;}else{nextX = zoeX;}
                if (y < zoeY){nextY = y+1;}else if (y > zoeY){nextY = y-1;}else{nextY = zoeY;}

                if(legalMove(map,nextX,nextY)){moveObject(map, nextX, nextY, x,y);}
            }

        }
    }

    /**
     *
     * @param map carte du jeu sur laquelle se trouve monstre et Zoe pour enlever de la vie a Zoe selon
     *            la force du monstre
     */
    public void attaquerZoe(Level map){
        map.getZoe().setHP(map.getZoe().getHp()-force);
        if (map.getZoe().getHp() <= 0){
            Messages.afficherDefaite();
            System.exit(0);
        }
    }

    public boolean getEtat(){
        return this.etat;
    }

    public String getItem() {
        return item;
    }

    public void perdreVie(){
        this.hp--;
    }

    @Override
    public int getHp(){
        return this.hp;
    }
}
