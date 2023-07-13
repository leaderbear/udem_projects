package Controller;

import Transport.*;
import Compagnie.*;
import Voyage.Voyage;

import java.util.ArrayList;

public class VehiculeController extends Controller {
	private ArrayList<Vehicule> listeVehicules = new ArrayList<Vehicule>();


	/**
	 * 
	 * @param c
	 * @param id
	 */
	public void createVehicule(Vehicule v) {
		listeVehicules.add(v);
	}

	/**
	 * 
	 * @param c
	 * @param v
	 */
	public void editVehicule(Compagnie c, Vehicule v) {
		// TODO - implement VehiculeController.editVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param id
	 */
	public void deleteVehicule(Compagnie c, int id) {
		// TODO - implement VehiculeController.deleteVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param id
	 */
	public Vehicule readVehicule(Compagnie c, int id) {
		// TODO - implement VehiculeController.readVehicule
		throw new UnsupportedOperationException();
	}

}