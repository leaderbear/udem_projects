public class Date {
	
	private int jour;
	private int mois;
	private int annee;
	private double heure;
	
	public Date(int jour, int mois, int annee, double heure) {
		
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;
		this.heure = heure;
		
	}
	
	
	// GETTERS
	public int getJour() {return this.jour;}
	public int getMois() {return this.mois;}
	public int getAnnee() {return this.annee;}
	public double getHeure() {return this.heure;}
	public int[] getDate() {
		int[] toReturn = {this.jour,this.mois,this.annee};
		return toReturn;
	}
	
	// SETTERS
	public void setJour(int jour) {this.jour = jour;}
	public void setMois(int mois) {this.mois = mois;}
	public void setAnnee(int annee) {this.annee = annee;}
	public void setHeure(double heure) {this.heure = heure;}
	public void setDate(int[] date) {this.jour = date[0]; this.mois = date[1]; this.annee = date[2];}
	
	// METHODES
	public String toString() {
		String heureFormated = String.valueOf(this.heure);
		heureFormated = heureFormated.replace(".","h");
		String toReturn = "" + this.jour + "-" + this.mois + "-" + this.annee + " : " + heureFormated;
		return toReturn;
	}
	
}