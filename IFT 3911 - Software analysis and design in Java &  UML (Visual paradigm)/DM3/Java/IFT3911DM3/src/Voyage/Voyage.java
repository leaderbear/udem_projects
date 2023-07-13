package Voyage;


import Batiment.Batiment;
import Compagnie.*;
import Reservation.*;
import Systeme.*;
import Transport.Vehicule;


public abstract class Voyage {

	private Compagnie compagnie;
	private Batiment destinationDepart;
	private Batiment destinationArrivee;
	private String dateDepart;
	private String dateArrivee;
	private String id;
	private Reservation[] listeReservation;
	private Vehicule vehicule;

	public Voyage(Compagnie c, Vehicule v, String id, Batiment depart, Batiment arrivee, String dateDepart, String dateArrivee) {
		this.compagnie = c;
		this.vehicule = v;
		this.id = id;		
		this.destinationDepart = depart;
		this.destinationArrivee = arrivee;
		this.dateDepart = dateDepart;
		this.dateArrivee = dateArrivee;
		
	}
	/**
	 * 
	 * @param reservationID
	 */
	public void addReservation(int reservationID) {
		// TODO - implement Voyage.addReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param reservationID
	 */
	public void removeReservation(int reservationID) {
		// TODO - implement Voyage.removeReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param v
	 */
	public void accept(Visiteur v) {
		// TODO - implement Voyage.accept
		throw new UnsupportedOperationException();
	}

	public void lireInfoVoyage() {
		// TODO - implement Voyage.lireInfoVoyage
		throw new UnsupportedOperationException();
	}

	public String getId() {
		return id;
	}

}