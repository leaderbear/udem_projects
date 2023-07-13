public class Vecteur{

    //tableau private et variable pour somme produit scalaires proteges
    private double[] vecteur;
    private double dotProductAnswer;


    //Constructeur avec en paramètre un tableau d'éléments de type 'double'
    public Vecteur(double[] elements) {

        this.vecteur = elements;
    }


    //Permet de changer la valeur à l'index donné
    public void setElement(int index, double valeur){

        this.vecteur = vecteur.clone();
        vecteur[index] = valeur;
    }


    //Retourne la valeur à l'index demandé
    public double getElement(int index){

        double[] vecteurClone = this.vecteur.clone();
        return vecteurClone[index];
    }


    //Retourne dimension du vecteur
    public int getLength(){

        double[] vecteurClone = this.vecteur.clone();
        return( vecteurClone.length);
    }


    // produit scalaire avec un autre vecteur (v)
    public double dotProduct(Vecteur v){

        if ( v.getLength() == this.vecteur.length){ //dimensions des vec. doit etre egale, sinon erreur

            for (int i = 0; i < this.vecteur.length; i++){
                dotProductAnswer += this.vecteur[i] * v.getElement(i);
            }
            return dotProductAnswer;
        }

        else { System.err.println("Erreur dans les dimensions des vecteurs"); return Double.NaN;}
    }


    //Affiche le contenu du vecteur entres accolades sur la console en creant un string pour stocker les valeurs.
    public void afficher(){

        String vecteurString = "{";

        for (int i = 0; i<this.vecteur.length-1; i++){
            vecteurString+= this.vecteur[i] + ", ";}

        vecteurString += this.vecteur[this.vecteur.length-1]+"}";

        System.out.println(vecteurString);
    }

} //Tabta Abderrahim