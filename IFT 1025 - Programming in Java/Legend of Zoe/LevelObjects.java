import java.util.ArrayList;

public class LevelObjects {

    /**Attributs globales pour n'importe quel objet sur la carte du jeu**/
    private char apparence;
    private int positionX, positionY;

    /**
     * Constructeur de la classe levelObjects
     * @param x position x de l'objet sur le niveau
     * @param y position y de l'objet sur le niveau
     */
    public LevelObjects(int x, int y){
        this.positionX = x;
        this.positionY = y;
    }

    /**methode publiques pour les objets present dans chaque Level*/

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public char getApparence() { return apparence; }

    public void setApparence(char apparence) {
        this.apparence = apparence;
    }

    /**
     *
     * @param map : la map actuel sur laquelle l'objet est deplace
     * @param newX : nouvelle position x qu'on donne a l'objet
     * @param newY : nouvelle position y
     * @param oldX : ancienne position x de l'objet , qui permet aussi de savoir c'est quel objet qu'on bouge
     * @param oldY : ancienne position y
     * @see : fonciton removeObject qui enleve l'objet de son ancienne position
     */
    public void moveObject(Level map, int newX, int newY, int oldX , int oldY){

        this.positionY = newY;
        this.positionX = newX;

        removeObject(map, oldX, oldY);
    }

    /**
     *
     * @param map la carte sur laquelle on supprime l'objet
     * @param x : position x de l'objet supprime de la carte
     * @param y : position y de l'objet supprime de la carte
     * Ici, puisque seul les monstres/zoe se deplacent et se stack et puisqu'ils sont reconstruits dans la classe Level
     * a chaque tour, on peut se permettre de les supprimer de leur ancienne position pour eviter qu'il se double
     */
    public void removeObject(Level map, int x, int y){
        map.setLvlMap(x, y, null);
    }

    /**
     *
     * @param map : la map actuelle sur laquelle on regarde si le mouvement est possible
     * @param nextX : mouvement vers position x
     * @param nextY : mouvement vers position y
     * @return : true : on peut se deplacer vers x,y , sinon false : on ne peut pas
     */
    public boolean legalMove(Level map, int nextX, int nextY){

        //Eviter outOufBound puisque la taille de tous les niveaux est fixe a 14*40
        if (nextX == -1 || nextX == map.getLargeur() || nextY == -1 || nextY == map.getHauteur()) {return false;}

        //on ne peut pas aller sur un mur non creusee
        if (map.getLvlMap(nextX, nextY) instanceof Mur) {
            if (((Mur) map.getLvlMap(nextX, nextY)).getEtat() == true) {return false;}
        }

        //on ne peut pas aller sur la Sortie
        if (map.getLvlMap(nextX, nextY) instanceof Sortie) { return false; }

        //On ne peut pas aller sur un tresor
        for (int i = 0; i < map.getTresorsTab().size(); i++){
            if(map.getTresorsTab().get(i).getPositionY() == nextY && map.getTresorsTab().get(i).getPositionX() == nextX){
                return false;
            }
        }

        //Tous les autres mouvements sont possibles
        return true;
    }

    @Override
    public String toString(){
        String string = "";
        string += this.apparence;
        return string;
    }
}
