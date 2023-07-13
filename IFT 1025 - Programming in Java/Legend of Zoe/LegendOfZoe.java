import java.util.Scanner;

/**
 * @author Abderrahim Tabta et Guilhem Robert
 * 20133680 & 20125535
 */
public class LegendOfZoe {

    public static void main(String[] args) {

        Messages.afficherIntro(); //INTRO DU JEU

        int hexaforcesOld = 0;
        int vieOld = 5;
        int numNiv = 1;
        Level lvlActuel = new Level(numNiv, hexaforcesOld, vieOld); //Creer premier niveau du jeu hexa : 0 / vie : 5

        System.out.println("Zoe: \u2665 \u2665 \u2665 \u2665 \u2665  | _ _ _ _ _ _"); //Imprimer Barre de vie / hexa
        System.out.print(lvlActuel);


        for(;;) { //Boucle infini qui attend que joueur gagne ou perd

            Scanner keyboard = new Scanner(System.in); //Scanner pour les touches sur lequelles le joueurs cliqus
            String pressedKeys = keyboard.nextLine();
            int stringSize = pressedKeys.length();

            for (int k = 0; k < stringSize; k++) { //Touche cliquees executees une par une

                //informations sur le niveau actuel
                int hexaforces = lvlActuel.getHexaforcesFound();
                int health = lvlActuel.getZoe().getHp();
                int sortieX = lvlActuel.getSortie().getPositionX();
                int sortieY = lvlActuel.getSortie().getPositionY();
                String information = "Zoe: ";


                //Tour de Zoe
                char pressedKey = pressedKeys.charAt(k);

                if (lvlActuel.getLvlDone() == true) {} //Si niveau deja fini, skip le tour de Zoe, sinon action de Zoe

                else {lvlActuel.getZoe().nextMove(lvlActuel, pressedKey);

                    //Mise a jour des informations de Zoe apres son action
                    int x = lvlActuel.getZoe().getPositionX();
                    int y = lvlActuel.getZoe().getPositionY();
                    hexaforces = lvlActuel.getHexaforcesFound();
                    health = lvlActuel.getZoe().getHp();

                    //Verifier si Zoe est a cote d'une sortie ouverte pour passer au prochain niveau ou si Zoe a gagne
                    if ((lvlActuel.getSortie().getEtat() == true) && (((
                        sortieX == x || (sortieX-1 == x) || (sortieX+1 == x))
                        &&((sortieY == y) || (sortieY-1 == y) || (sortieY+1 == y))))){lvlActuel.lvlDone();}

                    if (hexaforces == 6){ Messages.afficherVictoire(); System.exit(0);}
                }

                //Tour des monstres (qu'on skip si le niveau est deja termine)
                for (int i = 0; i < lvlActuel.getMonstresTab().size(); i++) {
                    if (lvlActuel.getLvlDone() == true) {}
                    else { lvlActuel.getMonstresTab().get(i).nextMove(lvlActuel); }
                }

                //Dessiner Barre de vie/hexaforces de Zoe
                for (int hearth = 0; hearth < 5; hearth++) {
                    if (hearth < health) { information += "\u2665 "; }
                    else { information += "\u2661 "; }
                }

                information += "| ";

                for (int hexaCount = 0; hexaCount < 6; hexaCount++) {
                    if (hexaCount < hexaforces) { information += "\u0394 "; }
                    else { information += "_ "; }
                }

                // Important, si niveau termine : cree prochain niveau
                if (lvlActuel.getLvlDone() == true) {
                    hexaforcesOld++; numNiv++;
                    vieOld = lvlActuel.getZoe().getHp();
                    stringSize = 0;
                    lvlActuel = new Level(numNiv, hexaforcesOld, vieOld);
                }

                //Imprimer niveau actuel et la barre d'etat de Zoe jusqu'a victoire ou defaite
                System.out.println(information);
                System.out.print(lvlActuel);

            }
        }
    }
}

//TP1 par Abderrahim Tabta et Guilhem Robert
//20133680 & 20125535



