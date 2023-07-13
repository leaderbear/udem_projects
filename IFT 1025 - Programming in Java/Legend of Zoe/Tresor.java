public class Tresor extends LevelObjects {

    private String item;
    private boolean etat;

    /**
     *
     * @param item string d'item qu'on le trouve dans le coffre
     * @param x position x du coffre
     * @param y position y du coffre
     */
    public Tresor(String item, int x , int y ){
        super(x,y);
        this.item = item;
        this.etat = true; //coffre fermee et peut etre ouvert de base
        setApparence('$');
    }


    /**Methodes publique pour les tresors, utiles lors de l'ouverture des tresor par Zoe**/
    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
        if(etat == false){setApparence('_');}
    }

    public String getItem() {
        return item;
    }
}
