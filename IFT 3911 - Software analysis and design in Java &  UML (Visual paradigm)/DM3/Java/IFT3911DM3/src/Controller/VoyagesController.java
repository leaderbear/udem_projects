package Controller;

import Voyage.*;
import Compagnie.*;
import Batiment.*;
import Transport.*;

import java.util.ArrayList;

public class VoyagesController extends Controller {

	private static ArrayList<Voyage> listeVoyages = new ArrayList<Voyage>();
	private int updateChange;

	/**
	 * 
	 * @param c
	 * @param v
	 * @param id
	 * @param depart
	 * @param arrivee
	 * @param date
	 */
	public void createVoyage(Voyage v) {
		listeVoyages.add(v);
	}

	/**
	 * 
	 * @param v
	 * @param c
	 */
	public void deleteVoyage(Voyage v, Compagnie c) {
		// TODO - implement VoyagesController.deleteVoyage
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
		// TODO - implement VoyagesController.editVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Voyage readVoyage(int id) {
		// TODO - implement VoyagesController.readVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param depart
	 * @param arrivee
	 * @param date
	 */
	public ArrayList<Voyage> consulterVoyage(Batiment depart, Batiment arrivee, String date) {
		// TODO - implement VoyagesController.consulterVoyage
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 */
	public ArrayList<Voyage> consulterVoyageCompagnie(Compagnie c) {
		// TODO - implement VoyagesController.consulterVoyageCompagnie
		throw new UnsupportedOperationException();
	}

	public static boolean valideId(String id) {
		for(Voyage v : listeVoyages) {
			if(v.getId() == id) {
				return false;
			}
		}
		return true;
	}

}