package Systeme;

import Compagnie.*;
import Batiment.*;
import Voyage.*;
import Transport.*;

public class menuCompagnie extends Menu {

	private int compagniesController;
	private int voyagesController;
	private int batimentsController;

	/**
	 * 
	 * @param c
	 * @param v
	 * @param id
	 * @param depart
	 * @param arrivee
	 * @param date
	 */
	public int createVoyage(Compagnie c, Vehicule v, String id, Batiment depart, Batiment arrivee, String date) {
		// TODO - implement menuCompagnie.createVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param v
	 * @param c
	 */
	public void deleteVoyage(Voyage v, Compagnie c) {
		// TODO - implement menuCompagnie.deleteVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param v
	 * @param depart
	 * @param arrivee
	 * @param date
	 */
	public void editVoyage(Vehicule v, Batiment depart, Batiment arrivee, String date) {
		// TODO - implement menuCompagnie.editVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Voyage readVoyage(int id) {
		// TODO - implement menuCompagnie.readVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 */
	public Voyage[] consulterVoyageCompagnie(Compagnie c) {
		// TODO - implement menuCompagnie.consulterVoyageCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param id
	 */
	public Vehicule createVehicule(Compagnie c, int id) {
		// TODO - implement menuCompagnie.createVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param v
	 */
	public void editVehicule(Compagnie c, Vehicule v) {
		// TODO - implement menuCompagnie.editVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param v
	 * @param t
	 * @param rangéeD
	 * @param rangéeF
	 * @param prix
	 */
	public int createSection(Compagnie c, Vehicule v, String t, int rangéeD, int rangéeF, double prix) {
		// TODO - implement menuCompagnie.createSection
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param s
	 * @param c
	 * @param v
	 * @param type
	 * @param p
	 */
	public void editSection(Section s, Compagnie c, Vehicule v, String type, double prix) {
		// TODO - implement menuCompagnie.editSection
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param v
	 * @param numero
	 */
	public void deleteSection(Compagnie c, Vehicule v, int numero) {
		// TODO - implement menuCompagnie.deleteSection
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param numero
	 */
	public Section readSection(int numero) {
		// TODO - implement menuCompagnie.readSection
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param id
	 */
	public Vehicule readVehicule(Compagnie c, int id) {
		// TODO - implement menuCompagnie.readVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idCompagnie
	 * @param idEmploye
	 * @param password
	 */
	public Compagnie readCompagnie(int idCompagnie, int idEmploye, String password) {
		// TODO - implement menuCompagnie.readCompagnie
		throw new UnsupportedOperationException();
	}

	public void consulterView() {
		// TODO - implement menuCompagnie.consulterView
		throw new UnsupportedOperationException();
	}

}