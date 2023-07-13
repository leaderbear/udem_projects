import javax.swing.text.Position;
import java.lang.annotation.Documented;
//Tabta Abderrahim 20133680
public class Polynome {

    private Noeud polynome;
    private Noeud temp;
    private int polynomeLength = 0;
    private double tempSomme;
    private int positionI;


    /**
     * Additionne un terme cx^e au polynome.
     *
     * @param coeff    coefficient c du terme à ajouter
     * @param exposant exposant e du terme à ajouter
     */
    public void ajouter(double coeff, int exposant) {


        //Si on ajoute 0^0 au polynome, il ne passe rien.
        if (coeff == 0 && exposant == 0) {
        }

        //Sinon on ajoute le terme
        else {

            //Premiere lieu, verifier si le polynome est vide. Si, oui simplement ajouter le terme.
            if (polynome == null) {
                polynome = new Noeud(coeff, exposant, null);
                polynomeLength = 1;
            }

            //Si il y a juste un seul terme dans le polynome alors :
            else if (getPolynomeLength() == 1) {

                //Si ce terme du polynome a le meme exposant que le terme qu'on veut lui ajouter, alors les additionner
                if (this.polynome.getExposant() == exposant) { //meme exposant, donc aditionner
                    tempSomme = this.polynome.getValeur() + coeff;
                    if (tempSomme == 0) {
                        this.polynome = new Noeud(0,0,null);
                    } else {
                        this.polynome = new Noeud(tempSomme, exposant, null);
                    }

                }
                    //Si ce n'Est pas le meme exposant, alors soit ajouter ajouter le nouveau terme au debut ou a la fin

                else if (!(this.polynome.getExposant() < exposant)) { //ici on l'ajoute a la fin
                    addFin(coeff, exposant);
                    polynomeLength++;

                } else if (!(polynome.getExposant() > exposant)) { //ici on l'ajoute au debut
                    Noeud n = polynome;
                    this.polynome = new Noeud(coeff, exposant, null);
                    this.polynome.setProchain(n);
                    polynomeLength++;
                }
            }


            //Par contre, s'il y a plus d'un terme, nous allons parcourir tous ces termes pour decider a quel position
            //i , nous allons mettre le nouveau mettre qu'on veut ajouter si on l'aditionne pas
            else if (getPolynomeLength() > 1) {

                temp = getNoeudExp(exposant); //Terme du mm exposant que celui qu'on ajoute qu'on a trouve ou pas

                if (temp != null) {  //Si ce terme existe , alors aditionner les coeffeciant
                    tempSomme = temp.getValeur() + coeff;
                    if (tempSomme == 0) {
                        positionI = getPositionI(exposant);

                        if(positionI == 0){this.polynome = this.polynome.getProchain();}
                        else{
                            Noeud precedent = getNoeud(positionI - 1);
                            Noeud suivant = precedent.getProchain().getProchain();
                            precedent.setProchain(suivant);
                        }
                    }
                    else {temp.setValeur(temp.getValeur() + coeff);}

                } else if (temp == null) { //Si aucun terme du mm exposant existe, chercher ou mettre le nouveau mettre
                    positionI = getPositionI(exposant);

                    if (positionI == 0) { //mettre nouveau terme au debut
                        Noeud n = this.polynome;
                        this.polynome = new Noeud(coeff, exposant, null);
                        this.polynome.setProchain(n);

                    } else if (positionI == polynomeLength) { //mettre nouveau terme a la fin
                        addFin(coeff, exposant);
                        polynomeLength++;

                    } else {
                        addAtI(positionI, coeff, exposant);
                    } //ajouter terme a la position i
                }
            }
        }
    }

    @Override
    /** on override la fonction toString pour pouvoir print nos polynomes sur la console**/
    public String toString() {

        Noeud n = this.polynome;
        String string = "";

        if (this.polynome == null) {
            return "0";
        } //Si le polynome est vide ou = a 0,  alors on imprime 0

        else if (polynomeLength >= 1) {

            //Ici, on imprime le premier terme apres avoir regarder si c'est un nombre a virgule.
            //Si ce n'est pas un nombre a virgule, on le cast pour qu'on print pas 10,0 par exemple
            if (n.getValeur() % 1 == 0) {
                string += (int) n.getValeur();
            } else {
                string += n.getValeur();
            }

            //Ici, si c'est une constante on imprime pas de le x, si x^1 on imprime juste x, sinon on imprime x^exposant
            if (n.getExposant() == 0) {
                string += "";
            } else if (n.getExposant() == 1) {
                string += "x";
            } else {
                string += "x^" + n.getExposant();
            }


            //Meme chose que le premier terme, mais cette fois on imprime tous les autres termes dans le polynomes
            while (n.getProchain() != null) {

                n = n.getProchain();
                if (n.getValeur() % 1 == 0) {
                    string += " + " + (int) n.getValeur();
                } else {
                    string += " + " + n.getValeur();
                }

                if (n.getExposant() == 0) {
                    string += "";
                } else if (n.getExposant() == 1) {
                    string += "x";
                } else {
                    string += "x^" + n.getExposant();
                }
            }
        }

        return string;
    }

    /**
     * Addition d'un polynome par un autre. Cette fonction ne
     * modifie pas le polynome actuel.
     *
     * @param autre le polynome à additionner
     * @return un nouveau polynome contenant le résultat de
     * l'addition
     */
    public Polynome additionner(Polynome autre) {

        //change pas le polynome.(mais change autre) problem
        Polynome resultat = new Polynome();
        Noeud n = polynome;
        Noeud temp;
        Polynome p = autre;

        for (int i = 0; i < p.getPolynomeLength(); i++) {
            temp = p.getNoeud(i);
            resultat.ajouter(autre.getValeur(i), autre.getExposant(i));
            //resultat.ajouter(temp.getValeur(), n.getExposant());
        }

        while (n != null) {
            resultat.ajouter(n.getValeur(), n.getExposant());
            n = n.getProchain();
        }

        return resultat;
    }

    /**
     * Multiplication par une constante. Cette fonction ne modifie pas
     * le polynome actuel.
     *
     * @param c constante multiplicative
     * @return un nouveau polynome correspondant au polynome actuel
     * (this) multiplié par une constante
     */
    public Polynome multiplier(double c) {
        Polynome resultat = new Polynome();

        Noeud n = this.polynome;
        double newCoeff;
        int newExp;

        while (n != null) {
            newCoeff = n.getValeur() * c;
            newExp = n.getExposant();
            resultat.ajouter(newCoeff, newExp);
            n = n.getProchain();
        }

        return resultat;
    }

    /**
     * Multiplication d'un polynome par un autre. Cette fonction ne
     * modifie pas le polynome actuel.
     *
     * @param autre le polynome à multiplier
     * @return un nouveau polynome contenant le résultat de la
     * multiplication
     */
    public Polynome multiplier(Polynome autre) {
        Polynome resultat = new Polynome();
        Noeud n = this.polynome;
        double newCoeff;
        int newExp;


        for (int i = 0; i < autre.getPolynomeLength() ; i++ ) {
            n = this.polynome;
            while ( n != null) {
                newCoeff = n.getValeur() * autre.getNoeud(i).getValeur();
                newExp = n.getExposant() * autre.getNoeud(i).getExposant();
                resultat.ajouter(newCoeff,newExp);
                n = n.getProchain();
            }
        }


        return resultat;
    }

    /**
     * Retourne la dérivée sous la forme d'un nouveau polynome. Cette
     * fonction ne modifie pas le polynome actuel.
     *
     * @return la dérivée du polynome actuel sous la forme d'un
     * nouveau polynome
     */
    public Polynome derivee() {
        Polynome derivee = new Polynome();
        Noeud n = this.polynome;
        double newCoeff;
        int newExp;

        while (n != null) {
            if (n.getExposant() == 0) {
            } //si constante rien faire
            else {
                newCoeff = n.getValeur() * n.getExposant();
                newExp = n.getExposant() - 1;
                derivee.ajouter(newCoeff, newExp);
            }
            n = n.getProchain();
        }

        return derivee;
    }


    /**
     * Methodes qui vont facilter la transitiion entres les noeuds et leur modification, cad les termes du polynomes
     * La plupart de ces methodes sont trouvees dans les notes de cours , cad structures de donnees
     **/

    //Acceder a noeud ( terme) a la position idx
    public Noeud getNoeud(int idx) {
        Noeud n = this.polynome;
        for (int i = 0; i < idx; i++) {
            n = n.getProchain();
        }
        return n;
    }

    //Trouver terme avec un certain exposant sinon retourne null
    public Noeud getNoeudExp(int exp) {
        Noeud n = this.polynome;
        if (n.getExposant() == exp) {
            return this.polynome;
        } else {
            while (n.getProchain() != null) {
                n = n.getProchain();
                if (n.getExposant() == exp) {
                    return n;
                }
            }
        }
        return null;
    }

    public int getPositionI(int exposant) { //Sert a trouver ou faut placer un terme selon un exposant
        Noeud n = this.polynome;
        int i = 0;

        if (n.getExposant() < exposant) {
            return 0;
        } //Si terme plus grand que premier, retourne 0 (debut)

        while (n.getProchain() != null) {
            i++;
            n = n.getProchain();
            if (n.getExposant() < exposant) {
                return i;
            } //sinon cherche ou le mettre
        }
        return polynomeLength; //S'il trouve pas , propose de le mettre a la fin
    }

    //Obtenir la valeur du coeff du noeud (terme) a la position idx
    public double getValeur(int idx) {
        return getNoeud(idx).getValeur();
    }

    public int getExposant(int idx) {
        return getNoeud(idx).getExposant();
    }

    //Modifier la valeur du coeff du terme a la position idx
    public void setValeur(int idx, double valeur) {
        getNoeud(idx).setValeur(valeur);
    }

    public void setExposant(int idx, int exposant) {
        getNoeud(idx).setExposant(exposant);
    }

    //ajouter un nouveau Noeud a une position arbitraire i
    public void addAtI(int i, double coeff, int exposant) {
        if (i == 0) {
            Noeud n = this.polynome;
            this.polynome = new Noeud(coeff, exposant, null);
            this.polynome.setProchain(n);
        } else {
            Noeud nouveau = new Noeud(coeff, exposant, null);
            Noeud precedent = getNoeud(i - 1);
            nouveau.setProchain(precedent.getProchain());
            precedent.setProchain(nouveau);
        }
    }

    public int getPolynomeLength() {

        int size = 0;
        Noeud n = this.polynome;
        while (n != null) {
            size++;
            n = n.getProchain();
        }
        return size;
    }

    public void addFin(double valeur, int exposant) {

        if (this.polynome == null) {

            this.polynome = new Noeud(valeur, exposant, null);
        } else {

            Noeud fin = this.polynome;
            Noeud n = this.polynome.getProchain();
            while (n != null) {
                fin = n;
                n = n.getProchain();
            }
            fin.setProchain(new Noeud(valeur, exposant, null));
        }
    }


    public void remove(int i){

            int retour;
            if (i == 0) {
                this.polynome = this.polynome.getProchain();
            } else {
                Noeud precedent = getNoeud(i - 1);
                Noeud suivant = precedent.getProchain().getProchain();
                precedent.setProchain(suivant);
            }
    }

        /**
         * Fonction utilitaire pour tester la classe.
         *
         * @param test résultat du test unitaire
         * @param message description du test
         */
        public static void assertTest ( boolean test, String message){
            if (test) {
                System.out.println("OK: " + message);
            } else {
                System.out.println("ERREUR: " + message);
            }
        }

        /**
         * À compléter : quelques tests pour vous aider
         */
        public static void main (String[]args){
            Polynome p = new Polynome();
            Polynome s = new Polynome();
            Polynome sommePS = new Polynome();
            Polynome a = new Polynome();
            Polynome b = new Polynome();

            a.ajouter(10,2);
            a.ajouter(-10,2);
            System.out.print(a);


            p = new Polynome();
            p.ajouter(10, 0);
            System.out.println(p);
            assertTest(p.toString().equals("10"), "Un seul terme (constante)");

            p = new Polynome();
            p.ajouter(10, 1);
            System.out.println(p);
            assertTest(p.toString().equals("10x"), "Un seul terme (exposant=1)");

            p = new Polynome();
            p.ajouter(10, 2);
            System.out.println(p);
            assertTest(p.toString().equals("10x^2"), "Un seul terme (exposant=2)");

            p.ajouter(5, 4);
            System.out.println(p);
            assertTest(p.toString().equals("5x^4 + 10x^2"), "Deux termes");

            p.ajouter(3, 2);
            System.out.println(p);
            assertTest(p.toString().equals("5x^4 + 13x^2"), "Ajouter à un terme déjà présent");

            p.ajouter(7, 0);
            System.out.println(p);
            assertTest(p.toString().equals("5x^4 + 13x^2 + 7"), "Ajouter une constante");


            p.ajouter(-22, 3);
            System.out.println(p);
            assertTest(p.toString().equals("5x^4 + -22x^3 + 13x^2 + 7"), "Ajouter un coefficient négatif");

            s.ajouter(10, 2);
            s.ajouter(4, 3);
            System.out.println(s + " Ca c'est S initiee");

            //5x^4 + -18x^3 + 23x^2 + 7

            sommePS = p.additionner(s);
            System.out.println(sommePS + " CAA C'EST PS");
            System.out.println(s + " Ca c'est S");
            System.out.println(p + " Ca c'est P");
            assertTest(sommePS.toString().equals("5x^4 + -18x^3 + 23x^2 + 7"), "additionner s et p");

            sommePS = p.multiplier(s);
            System.out.println(sommePS);
            System.out.println(p);
            System.out.println(s);


            Polynome test = p.multiplier(-2);
            System.out.println(p);
            assertTest(test.toString().equals("-10x^4 + 44x^3 + -26x^2 + -14"), "multiplier par c2");

            Polynome derivee = p.derivee();
            System.out.println(p);
            System.out.print(derivee);
            assertTest(derivee.toString().equals("20x^3 + -66x^2 + 26x"), "Dérivée");

            derivee = p.derivee().derivee().derivee().derivee();
            assertTest(derivee.toString().equals("120"), "Dérivée 4 fois");

            derivee = p.derivee().derivee().derivee().derivee().derivee()
                    .derivee().derivee().derivee().derivee().derivee();
            assertTest(derivee.toString().equals("0"), "Dérivée 10 fois");
        }

}

