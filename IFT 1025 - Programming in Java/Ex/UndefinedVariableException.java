/**
 * @author Abderrahim Tabta 20133680
 */
public class UndefinedVariableException extends Exception {

    private String erreur;

    /**
     * Constructeur qui appelle le constructeur parent avec le message d'erreur a afficher sur la console
     *
     * @param variable String : la variable invalide que l'utilisateur a utilise
     * @param ligne Int : A quelle ligne dans la feuille de calcul il a utilise cette variable
     */
    public UndefinedVariableException(String variable, int ligne){
        super("La variable " + variable + " est invalide, ligne N: " + ligne);
    }

    //Not used
    public UndefinedVariableException(){
        this.erreur = "La variable que vous avez utilise est invalide";
        System.err.print(erreur);
    }
}
