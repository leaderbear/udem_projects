public class Sortie extends LevelObjects{

    private boolean etat;

    /**
     * Constructeur de la sortie du jeu qui permet de passer au prochaine niveau si elle est ouverte
     * @param x position x de la sortie sur la carte du jeu
     * @param y position y de la sortie sur la carte du jeu
     * l'etat indique si la porte est fermee ou ouverte, la porte du niveau s'ouvre qd l'hexaforce du niveau est ramasse
     */
    public Sortie(int x, int y){
        super(x,y);
        setApparence('E');
        etat = false; //porte fermee de base
    }

    /**Methodes publiques pour la Sortie, utiles lors des passages de niveaux**/
    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public boolean getEtat(){
        return this.etat;
    }
}
