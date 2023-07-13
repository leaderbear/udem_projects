import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public final class Level{

    /** Attributs pour chaque niveau du jeu**/
    private boolean done; //Indique si niveau est complete
    private int numero;
    private int hexaforcesFound; //Nombre d'hexaforces totales ramasses par Zoe
    private Zoe zoe;
    private Sortie sortie;
    private ArrayList<Monstre> monstresTab = new ArrayList<>(0);
    private ArrayList<Tresor> tresorsTab = new ArrayList<>(0);
    private LevelObjects[][] lvlMap;


    /**
     * Constructeur de niveau
     * @param num : numero du lvl qui sert d'indicateur de difficulte
     * @param stackedHexa : nombre d'hexaforces au total ramasses par Zoe
     * @param vieOld : Points de vie de Zoe actuellement
     */
    public Level(int num, int stackedHexa, int vieOld){

        this.numero = num;
        this.hexaforcesFound = stackedHexa;
        this.done = false;

        //Ici on utilise la class generateur pour generer une nouvelle map
        Paire<boolean[][],String[]> mapPaire;
        LevelGenerator level = new LevelGenerator();
        mapPaire = LevelGenerator.generateLevel(num);

        //Ici on stock les info generee dans nos variables
        boolean[][] map = mapPaire.getKey();
        String[] mapObjects = mapPaire.getValue();

        /**Variable la plus important ,car contient la carte du jeu qui va stocker les murs/objets et qu'on va print**/

        //D'abord on defini sa taille
        lvlMap = new LevelObjects[map.length][map[0].length];

        //Ensuite on place les murs
        for(int i = 0; i < lvlMap.length ; i++){
            for (int j = 0; j < lvlMap[0].length ; j++){
                if (map[i][j] == false) {}
                else {lvlMap[i][j] = new Mur(i,j);}
            }
        }

        /**On cree tous les objets qui vont etre mis dans la map**/

        for (int i = 0 ; i < mapObjects.length ; i++){

            Scanner objScanner;
            String input = mapObjects[i];
            //System.out.println(input);
            objScanner = new Scanner((input)).useDelimiter("[:]+");

            if (new Scanner(input).useDelimiter("[:]+").next().equals("tresor")) {
                objScanner.next();
                tresorsTab.add(new Tresor(objScanner.next(), objScanner.nextInt(), objScanner.nextInt()));
            }

            else if (new Scanner(input).useDelimiter("[:]+").next().equals("monstre")) {
                objScanner.next();
                monstresTab.add(new Monstre(objScanner.next(), objScanner.nextInt(), objScanner.nextInt(), num));
            }

            else if (new Scanner(input).useDelimiter("[:]+").next().equals("sortie")) {
                objScanner.next();
                this.sortie = new Sortie(objScanner.nextInt(), objScanner.nextInt());
            }

            else if ( new Scanner(input).useDelimiter("[:]+").next().equals("zoe")) {
                objScanner.next();
                this.zoe = new Zoe(objScanner.nextInt(), objScanner.nextInt());
            }


            objScanner.close();
        }

        //Maintenant on met les objets qu'on a defini dans la carte du jeu

        lvlMap[this.zoe.getPositionY()][zoe.getPositionX()] = this.zoe;
        lvlMap[this.sortie.getPositionY()][this.sortie.getPositionX()] = this.sortie;

        for (int i = 0; i < monstresTab.size(); i++){
            lvlMap[monstresTab.get(i).getPositionY()][monstresTab.get(i).getPositionX()] = this.monstresTab.get(i);
        }

        for (int i = 0; i < tresorsTab.size(); i++){
            lvlMap[tresorsTab.get(i).getPositionY()][tresorsTab.get(i).getPositionX()] = this.tresorsTab.get(i);
        }
        this.zoe.setHP(vieOld); //On set la vie de Zoe dans le niveau , selon sa vie du niveau precedent
    }


    /**Methodes publiques pour levels du jeu**/

    public Zoe getZoe() {
        return zoe;
    }

    public Sortie getSortie() {
        return sortie;
    }

    public ArrayList<Monstre> getMonstresTab() { return monstresTab; }

    public ArrayList<Tresor> getTresorsTab() {
        return tresorsTab;
    }

    public void setLvlMap(int x, int y, LevelObjects newObject){
        this.lvlMap[y][x] = newObject;
    }

    public LevelObjects getLvlMap(int x, int y){
        return this.lvlMap[y][x];
    }

    public int getLargeur(){ return lvlMap[0].length; }

    public int getHauteur(){ return lvlMap.length; }

    public void lvlDone(){
        this.done = true;
    }

    public boolean getLvlDone(){
        return this.done;
    }

    public void addHexaforce(){ this.hexaforcesFound++; }

    public int getHexaforcesFound(){ return this.hexaforcesFound; }


    @Override
    public String toString() {

        //Reconstruire map selon zoe/monstres qui changent toujours de position
        for (int i = 0; i < monstresTab.size(); i++){
            lvlMap[monstresTab.get(i).getPositionY()][monstresTab.get(i).getPositionX()] = this.monstresTab.get(i);
        }
        lvlMap[this.zoe.getPositionY()][zoe.getPositionX()] = this.zoe;

        String string = "";
        for (int i = 0; i < lvlMap.length; i++) {
            for (int j = 0; j < lvlMap[0].length; j++) {
                if (lvlMap[i][j] instanceof LevelObjects) { string += lvlMap[i][j].getApparence();}
                else { string += " ";}
            }
            string += "\n";
        }
        return string;
    }
}
