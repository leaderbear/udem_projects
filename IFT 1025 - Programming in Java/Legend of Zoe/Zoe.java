import java.util.Scanner;

public class Zoe extends Characters{

    /**
     * Zoe est le personnage principale du jeu et il est controle par le joueur
     * @param x position x de Zoe
     * @param y position y de Zoe
     */
    public Zoe(int x, int y){
        super(x,y);
        setApparence('&');
    }

    /**Methodes pour Zoe**/

    /**
     *
     * @param map la carte du jeu laquelle se trouve Zoe
     * @param pressedKey la touche sur la quelle le joueur appuie pour que Zoe fait une action (touche qu'on le scanne)
     */
    public void nextMove(Level map, char pressedKey ){

        int x = map.getZoe().getPositionX();
        int y = map.getZoe().getPositionY();
        int nextX, nextY;


        //Deplacement de Zoe , w:haut, s:bas , a:gauche , d:droite. Verifier d'abord si c'est possible de faire ce
        //deplacement, si ce n'est pas possible, skip le tour de Zoe

        if (pressedKey == 'w'){
            nextX = x; nextY = y - 1;
            if(legalMove(map, nextX, nextY)){moveObject(map, nextX, nextY, x,y);}
        }

        else if (pressedKey == 's'){
            nextX = x; nextY = y + 1;
            if(legalMove(map, nextX, nextY)){moveObject(map, nextX, nextY, x,y);}
        }

        else if (pressedKey == 'a'){
            nextX = x - 1; nextY = y;
            if(legalMove(map, nextX, nextY)){moveObject(map, nextX, nextY, x,y);}
        }

        else if (pressedKey == 'd'){
            nextX = x + 1; nextY = y;
            if(legalMove(map,nextX,nextY)){moveObject(map, nextX, nextY, x,y);}
        }


        else if (pressedKey == 'c') {

            //Creuser tous les murs autant de Zoe (C-a-d set leurs etats a false, et change leur apparenace a ' '.
            for (int i = x - 1; i <= x + 1; i++) {

                if (i == -1 || i == map.getLargeur()) { continue; } //On peut pas creuser hors de la map
                for (int j = y - 1; j <= y + 1; j++) {

                    if (j == -1 || j == map.getHauteur()) { continue; }//on ne peut pas creuser hors de la map
                    else if (map.getLvlMap(i, j) instanceof Mur) { ((Mur) map.getLvlMap(i, j)).setEtat(false);
                    }
                }
            }
        }

        else if (pressedKey == 'x'){

            //Chercher la position de tous les monstres
            for (int i = 0 ; i < map.getMonstresTab().size(); i++){

                int monstreX = map.getMonstresTab().get(i).getPositionX();
                int monstreY = map.getMonstresTab().get(i).getPositionY();

                //regarde s'il ya un monstres dans les 9 cases autour de Zoe
                if (((monstreX == x) || (monstreX-1 == x) || (monstreX+1 == x)) &&
                        ((monstreY == y) || (monstreY-1 == y) || (monstreY+1 == y))) {

                    //Si oui, attaquer le monstre (sa vie - 1)
                    int monstreHp = map.getMonstresTab().get(i).getHp();
                    map.getMonstresTab().get(i).perdreVie();

                    //Si, la vie du monstre est = 0 ou moins , alors il drop un item et meurt, si deja mort, rien faire
                    if(map.getMonstresTab().get(i).getHp()<=0){

                        String dropedItem = map.getMonstresTab().get(i).getItem();
                        if (map.getMonstresTab().get(i).getEtat() == true){ itemEffect(map, dropedItem); }
                        map.getMonstresTab().get(i).setEtat(false);}
                }
            }
        }

        else if (pressedKey == 'o'){
            for (int i = 0; i < map.getTresorsTab().size(); i++){

                int tresorX = map.getTresorsTab().get(i).getPositionX();
                int tresorY = map.getTresorsTab().get(i).getPositionY();

                if (((tresorX == x) || (tresorX-1 == x) || (tresorX+1 == x)) &&
                        ((tresorY== y) || (tresorY-1 == y) || (tresorY+1 == y))) {

                    //Si coffre autour de Zoe est pas ouvert, alors l'ouvre et prendre l'item. Si deja ouvert, rien
                    if (map.getTresorsTab().get(i).getEtat() == true ) {

                        String dropedItem = map.getTresorsTab().get(i).getItem();
                        itemEffect(map, dropedItem);
                        map.getTresorsTab().get(i).setEtat(false);
                    }
                }
            }
        }

        else if (pressedKey == 'q') { System.exit(0);}
    }

    /**
     *
     * @param map la carte sur laquelle Zoe se trouve qui permet de mettre l'Effet sur Zoe
     * @param item l'item en question est un string qu'on compare pour savoir c'est quoi et apppliquer son effet:
     *             Hexaforce : ouvre la porte du niveau et ajouter un hexaforce a Zoe
     *             Potion de vie : met la vie de Zoe a 5 (son max)
     *             Coeur : ajoute un point de vie a Zoe sans depasser le max (5)
     */
    public void itemEffect(Level map, String item){

        if (item.equals("hexaforce")) {
            map.getSortie().setEtat(true);
            map.addHexaforce();
            System.out.println("Hexaforce found!");

        } else if (item.equals("potionvie")) {
            map.getZoe().setHP(5);
            System.out.println("Potion found!");

        } else if (item.equals("coeur")) {map.getZoe().setHP(Math.min(map.getZoe().getHp()+1, 5));
        System.out.println("Heart found!");}

    }

}
