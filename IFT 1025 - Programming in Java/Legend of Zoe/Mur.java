public class Mur extends LevelObjects {

    /**Attributs pour les murs du jeu**/
    private boolean etat;

    /** Constructeur pour la classe Mur**/
    public Mur(int x, int y){
        super(x,y);
        setApparence('#');
        etat = true;
    }

    /**Methodes publiques pour la classe Mur**/

    public void setEtat(boolean nouvelEtat){
        this.etat = nouvelEtat;
        if(nouvelEtat == false){setApparence(' ');}
    }

    public boolean getEtat(){
        return this.etat;
    }
}
