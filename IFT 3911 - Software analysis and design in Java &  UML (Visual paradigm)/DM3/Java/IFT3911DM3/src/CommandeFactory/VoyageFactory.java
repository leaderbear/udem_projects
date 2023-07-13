package CommandeFactory;

import Compagnie.*;
import Controller.CompagniesController;
import Controller.VoyagesController;
import Controller.BatimentsController;
import Batiment.*;
import Transport.*;
import Voyage.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class VoyageFactory {
	
	public VoyageFactory getInstance(VoyageType type) {
		switch(type) {
		case Avion : 
			return AvionFactory.getInstance();
		case Bateau : 
			return BateauFactory.getInstance();
		case Train : 
			return TrainFactory.getInstance();
			
		default : 
			return null;
		}
	}

	/**
	 * 
	 * @param c
	 */
	public abstract Compagnie createCompagnie(String id, String nom) ;

	/**
	 * 
	 * @param b
	 */
	public abstract Batiment createBatiment(String id, String ville) ;

	/**
	 * 
	 * @param v
	 */
	public abstract Vehicule createVehicule(String id, String nom) ;
	/**
	 * 
	 * @param v
	 */
	public abstract Voyage createVoyage(Compagnie c, Vehicule v, String id, Batiment depart, Batiment arrivee, String dateDebut, String dateArrivee) ;
	
	//Verifier le composition des IDs
	String regexCompagnie = "^[A-Z]{6}$";
	String regexBatiment = "^[A-Z]{3}$";
	String regexVoyage = "^[A-Z]{2}[0-9]*$";
	
	public void valideIdCompagnie(String id) {
		if(!verifyMatch(id, regexCompagnie)) {
			throw new IllegalArgumentException("Erreur ID Compagnie");
		}
		
		if(CompagniesController.valideID(id) == false ) throw new IllegalArgumentException("Compagnie ID: " + id + " est deja existant.");
		
	}
	
	public void valideIdBatiment(String id) {
		
		if(!verifyMatch(id, regexBatiment)) {
			throw new IllegalArgumentException("Erreur ID Batiment");
		}
		if(BatimentsController.valideID(id) == false) throw new IllegalArgumentException("Batiment ID: " + id + " est deja existant.");
	}
	
	public void valideIdVoyage(String id) {
		if(!verifyMatch(id, regexVoyage)) {
			throw new IllegalArgumentException("Erreur ID Voyage");
		}
		if(VoyagesController.valideId(id) == false) throw new IllegalArgumentException("Voyage ID: " + id + " est deja existant.");
		
	}
	
	private boolean verifyMatch(String id, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(id);
		
		boolean matchFound = matcher.find();
	    if(matchFound) return true;
	    else return false;
	}
}