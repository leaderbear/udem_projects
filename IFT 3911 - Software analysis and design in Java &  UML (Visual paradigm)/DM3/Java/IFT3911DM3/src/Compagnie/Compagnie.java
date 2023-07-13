package Compagnie;

import Reservation.compagnieEmploye;
import Transport.*;
import Voyage.*;

import java.util.ArrayList;

public abstract class Compagnie {

	private String nom;
	private ArrayList<Vehicule> listeVehicules = new ArrayList<Vehicule>(0);
	private ArrayList<Voyage> listeVoyages = new ArrayList<Voyage>(0);
	protected String id;
	private String password;
	private ArrayList<compagnieEmploye> listeEmploye = new ArrayList<compagnieEmploye>(0);
	
	public Compagnie(String id, String nom) {
		this.id = id;
		this.nom = nom;
	}
	
	

	/**
	 * 
	 * @param nom
	 */
	public int addVehicule(String nom) {
		// TODO - implement Compagnie.addVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Vehicule readVehicule(int id) {
		// TODO - implement Compagnie.readVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param v
	 * @param nom
	 */
	public void editVehicule(Vehicule v, String nom) {
		// TODO - implement Compagnie.editVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param v
	 */
	public void delete(Vehicule v) {
		// TODO - implement Compagnie.delete
		throw new UnsupportedOperationException();
	}
	
	public String getId() {
		return id;
	}

}