package Batiment;

import Voyage.*;

public abstract class Batiment {

	private String id;
	private String nom;
	private String lieu;
	private Voyage[] listeVoyagesDepart;
	private Voyage[] listeVoyageArrivee;
	
	public Batiment(String id, String lieu) {
		this.id = id;
		this.lieu = lieu;
	}
	public String getId() {
		
		return id;
	}

}