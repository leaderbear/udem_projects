//Tabta Abderrahim 20133680
public class Noeud {

    //Ce code est en grande partie inspire de "Strucutres de Donnees" des notes de cours

    private double valeur;
    private int exposant;
    private Noeud prochain;

    public Noeud(double valeur, int exposant, Noeud procahain){
        this.valeur = valeur;
        this.exposant = exposant;
        this.prochain = prochain;
    }

    public double getValeur() {

        return this.valeur;
    }

    public int getExposant() {
        return this.exposant;
    }

    public Noeud getProchain() {
        return this.prochain;
    }

    public void setProchain(Noeud noeud){
        this.prochain = noeud;
    }

    public void setValeur(double valeur){
        this.valeur = valeur;
    }

    public void setExposant(int exposant){
        this.exposant = exposant;
    }
}