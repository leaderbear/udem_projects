import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author Abderrahim Tabta 20133680
 */
public class Parser {

    private Scanner in;

    /**
     * Prend en paramètre le nom du fichier de la feuille de calculs à
     * lire.
     */
    public Parser(String filename){

        //Si fichier valide & existe, alors on le met dans scanner, sinon le scanner est null
        try {FileReader reader = new FileReader(filename); this.in = new Scanner(reader);}
        catch (FileNotFoundException e) { this.in = null; }

    }


    /**
     */
    public void parse() throws IOException, FileNotFoundException, ParseException, UndefinedVariableException {

        HashMap<String, Double> variables = new HashMap<>();
        ArrayList<Double> nombres = new ArrayList<>();
        String actualLine;
        char firstChar;

        double calculator = 0;
        int lineCounter = 0;

        //Si fichier n'existe pas ou fichier invalide erreur
        if (this.in == null){ throw new FileNotFoundException(); }

        //Boucle qui parcourt toutes les lignes du fichier
        while (in.hasNext()) {

            actualLine = in.nextLine();
            lineCounter++;

            if (actualLine.length() == 0){firstChar = ' ';}
            else {firstChar = actualLine.charAt(0);} //Si ligne n'est pas vide, on stock le premier char


            //Ignorez commentraires/lignes vides
            //SI commentaire invalide (Espace suivi de String sans # au debut, erreur)
            if (firstChar == '#' || actualLine.length() == 0 || firstChar ==' '){

                if (firstChar  == ' '){
                    for(int i = 0; i < actualLine.length(); i++){
                        if(actualLine.charAt(i) != ' '){
                            throw new ParseException("Erreur à la ligne N: " +lineCounter ,+lineCounter);}
                    }
                }
            }


            //Si ligne commence par '-' c-a-d symbole moins, voir si il est suivi par un nombre ou variable valide
            //Si oui, ajouter le nombre/var negatif dans la memoire, sinon lancer une exception
            else if (firstChar == '-' || Character.isDigit(firstChar)) {

                if (firstChar == '-' && isDouble(actualLine.substring(1))) {
                    nombres.add(Double.valueOf(actualLine));
                }

                else if (firstChar == '-' && isVar(actualLine.substring(1))){
                    if (variables.get(actualLine.substring(1)) == null){
                        throw new UndefinedVariableException(actualLine.substring(1), +lineCounter);
                    }
                    else {nombres.add(  - 1 *  variables.get(actualLine.substring(1)));}
                }

                else if (Character.isDigit(firstChar) && isDouble(actualLine)){
                    nombres.add(Double.valueOf(actualLine));
                }
                else {throw new ParseException("Erreur à la ligne N: " +lineCounter, +lineCounter );}
            }


            //Si ligne commence par = et suivi d'une variable valide
            //Mettre en memoire cette variable en aditionnant toutes les nombres en memoires
            //Enfin, supprimer tous les nombres en memoires et remettre a 0 la calculatrice
            else if (firstChar == '=' && isVar(actualLine.substring(1))){

                for (int i = 0; i < nombres.size(); i++){ calculator += nombres.get(i); }

                variables.put(actualLine.substring(1), calculator);
                calculator = 0; nombres = new ArrayList<>();
            }

            //Si ligne commence par une lettre, celle-ci doit soit etre un print ou une variable valide
            //SI c'est un print, regarder s'il est suivi d'une variable valide
            else if (Character.isLetter(firstChar)){

                if (getFirstWord(actualLine).equals("print")){

                    if (actualLine.charAt(6) == '-'){
                        throw new ParseException("Erreur à la ligne N: " +lineCounter ,+lineCounter);
                    }

                    else if (variables.get(actualLine.substring(6)) == null){
                        throw new UndefinedVariableException(actualLine.substring(6), +lineCounter);
                    }

                    else {System.out.println(variables.get(actualLine.substring(6)));}
                }

                else if ( wordsCounter(actualLine) == 1 && isVar(actualLine)){
                    if (variables.get(actualLine) == null){
                        throw new UndefinedVariableException(actualLine, lineCounter);
                    }
                    else {nombres.add( variables.get(actualLine));}
                }

                else {throw new ParseException("Erreur à la ligne N: " +lineCounter, +lineCounter);}
            }


            //Toute autres commandes (plus d'une variable, print -var . . . ) est une erreur
            else { throw new ParseException("Erreur à la ligne N: " +lineCounter, +lineCounter);}
        }
    }

    /**
     *
     * @param line On met string ligne qui a plusieurs mots sepeares par des espaces dans la fonction
     * @return la fonction retourne le premier mot de cette ligne
     */
    public String getFirstWord(String line){
        String firstWord;

        if (line.indexOf(' ') == -1){firstWord = line;} //Si la ligne a un seul mot, retourner le mot
        else{firstWord = line.substring(0, line.indexOf(' '));} //Sinon retourne le premier mot

        return firstWord;
    }

    /**
     *
     * @param line On met un string ligne de plusieurs mots separee par des espaces dans la fonction
     * @return la fonction retourne alors le nombre de mots qu'il y a dans cette ligne en utilisant ragex
     */
    public int wordsCounter(String line){

        int words;
        if (line.equals(" ") || line.equals("")){words = 0;} //Si ligne vide, retournez 0
        else {words = line.split(" ").length;} //Si ligne a plusieurs mots separee par des espaces, retournez nb
        return words;
    }

    /**
     *
     * @param word  Cette fonction prend comme paramatre n'importe quel mot (String)
     * @return la fonction retourne true si c'est un nombre (double) sinon retourne false
     */
    public boolean isDouble(String word){
        char actual;
        for (int i = 0; i < word.length(); i++) {
            actual = word.charAt(i);
            if ( !( Character.isDigit((actual)) || actual == '.' || actual == '-' ) ){return false;}
        }
        return true;
    }

    /**
     *
     * @param word Cette fonction prend comme parametre un mot String
     * @return retourne true si c'est une variable valide (Juste des lettres en MAJ) sinon retourne false
     */
    public boolean isVar(String word){
        char actual;
        for (int i = 0; i < word.length(); i++) {
            actual = word.charAt(i);
            if ( !(Character.isUpperCase(actual) && Character.isLetter(actual)) ) {return false;}
        }
        return true;
    }

    /**
     * Programme principal : devrait lire le fichier passé en argument
     * en ligne de commande et l'interpréter, par exemple :
     *
     * java Parser feuille-de-calcul
     *
     * -- Affiche les résultats des prints, ou affiche l'exception
     * lancée par parse() sur la console le fichier est mal formé
     *
     * Vous ne devez pas modifier cette méthode
     */
    public static void main(String[] args) throws Exception {
        Parser parser = new Parser(args[0]);
        parser.parse();
    }
}
