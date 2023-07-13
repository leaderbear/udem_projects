public class Matrice{

    //cree matrice private pour la proteger (+ une matriceTemp pour les methodes qui modifie pas la matrice principale)
    private double[][] matrice;
    private Matrice matriceTemp;


    //Constructeur pour matrice de taille Nlignes*Mcolonne rempli de '0'
    public Matrice(int lignes, int cols){

        this.matrice = new double[lignes][cols];
    }


    //Additionne valeur n dans toutes les cellules(modifie la matrice)
    public void additionnerScalaire(double n) {

        for (int i = 0; i < this.matrice.length; i++) { //mm boucle, mais chaque cellule : valeur + sclaire n
            for (int j = 0; j < this.matrice[0].length; j++) { this.matrice[i][j] =  this.matrice[i][j] + n;}
        }
    }


    // Multiplie toutes les cellules par un scalaire (modifie la matrice)
    public void multiplierScalaire(double n){

        for (int i = 0; i < this.matrice.length; i++) { //mm boucle, mais chaque cellule : valeur * sclaire n
            for (int j = 0; j < this.matrice[0].length; j++) {
                this.matrice[i][j] =  this.matrice[i][j] * n;}
        }
    }


    //retourne dimension de la ligne de la matrice
    public int getLignesLength (){

        double[][] matriceClone =  this.matrice.clone();
        return( matriceClone.length);
    }


    //retourne dimension de la collone de la matrice
    public int getColsLength () {

        double[][] matriceClone = this.matrice.clone();
        return( matriceClone[0].length);
    }


    //produit matriciel entre deux matrices (cree nouvelle sans modifier les deux)
    public Matrice dotProduct(Matrice m){

        //Vecteur tomporaire et variables
        Vecteur ligneActuelle;
        Vecteur colActuelle;

        if (this.matrice[0].length == m.getLignesLength()) { //verifie si dimensions sont coherantes, sinon erreur

            matriceTemp = new Matrice(this.matrice.length, m.getColsLength()); //nouvelle matrice avec bonne dimension

            for (int i = 0; i < matriceTemp.getLignesLength(); i++) {
                for (int j = 0; j < matriceTemp.getColsLength(); j++) {

                    //vecteurs :
                    ligneActuelle = new Vecteur(this.matrice[i]); //ligne i de matrice A
                    colActuelle = m.getCol(j); //coll j de matrice B

                    //valeur de chaque cellule(i,j) remplacé par produit sclairede vecteur ligne(i) * vecteur collone(j)
                    matriceTemp.setCell(i, j, ligneActuelle.dotProduct(colActuelle));
                }
            }
            return matriceTemp;
        }

        else {System.err.println("Erreur dans les dimensions des matrices"); return null;}
    }


    //Accesseur pour la cellule à une ligne/colonne donnée
    public double getCell(int ligne, int col){

        double[][] matriceClone = this.matrice.clone();
        return matriceClone[ligne][col];
    }


    //Mutateur pour la cellule à une ligne/colonne donnée
    public void setCell(int ligne, int col, double valeur){

        this.matrice = matrice.clone();
        matrice[ligne][col] = valeur;
    }


    //Retourne un nouveau vecteur contenant la Nième ligne
    public Vecteur getLine(int ligne){

        Vecteur vecteurLigne = new Vecteur(this.matrice.clone()[ligne]);
        return vecteurLigne;
    }


    //Retourne un nouveau vecteur contenant la Nième colonne
    public Vecteur getCol(int col){

        double[] tabCol = new double[this.matrice.length]; //tab tomporaire

        for (int i = 0; i < this.matrice.length; i++) { //mettre valeurs de la collone dans tableau tomporaire
            tabCol[i] = this.matrice.clone()[i][col];
        }

        Vecteur vecCol = new Vecteur(tabCol); //transformer tableau tomporaire en vecteur et le retourner
        return vecCol;
    }


    //Affiche la matrice sur la console, chaque ligne entre crochets [ ]
    public void afficher() {

        for (int j = 0; j < this.matrice.length; j++){

            String matriceString = "[ ";

            for (int i = 0; i < this.matrice[j].length; i++){
                matriceString += this.matrice[j][i] + " "; }

            matriceString += "]";

            System.out.println(matriceString);
        }
    }


    // Retourne une version transposée de la matric(sans modifier la matrice actuelle)
    public Matrice transpose(){

        matriceTemp = new Matrice(this.matrice[0].length, this.matrice.length); //matrice tompraire de meme taille

        for (int i = 0; i < matriceTemp.getLignesLength(); i++){ //Parcourir cellules
            for (int j = 0; j < matriceTemp.getColsLength(); j++){

                //si nous sommes dans diagonales simplement mettre valeur[i][j] dans matrice, sinon permuter [i]et[j]
                if (i != j) { matriceTemp.setCell(i,j, this.matrice[j][i]); }
                else { matriceTemp.setCell(i,j, this.matrice[i][j]); }

            }
        }
        return matriceTemp;
    }


    //Retourne une instance de la matrice identité N x N
    public static Matrice identite(int n){

        Matrice identite = new Matrice(n,n); //nouvelle matrice n par n

        for (int i = 0; i < n; i++){ //mettre des 1 dans la diagonale
            for (int j = 0; j < n; j++) {
                if (i == j) { identite.setCell(i, j, 1);} }
        }
        return identite;
    }

} //Tabta Abderrahim