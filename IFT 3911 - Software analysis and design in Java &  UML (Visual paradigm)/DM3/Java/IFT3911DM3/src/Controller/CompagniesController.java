package Controller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Compagnie.*;
import Reservation.compagnieEmploye;

public class CompagniesController extends Controller {

	private static ArrayList<Compagnie> listeCompagnies;
	
	
	//verifier si le ID est present
	public static boolean valideID(String id) {
		for(Compagnie c : listeCompagnies) {
			if( c.getId() == id) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param nom
	 * @param id
	 */
	public void createCompagnie(Compagnie c) {
		listeCompagnies.add(c);
	}

	/**
	 * 
	 * @param c
	 * @param n
	 * @param id
	 */
	public void editCompagnie(Compagnie c, String nom, int id) {
		// TODO - implement CompagniesController.editCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 */
	public void deleteCompagnie(Compagnie c) {
		// TODO - implement CompagniesController.deleteCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Compagnie readCompagnie(int id) {
		// TODO - implement CompagniesController.readCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param employe
	 * @param password
	 */
	public boolean checkPassword(compagnieEmploye employe, String password) {
		// TODO - implement CompagniesController.checkPassword
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param nom
	 * @param prenom
	 * @param password
	 */
	public int addEmploye(String nom, String prenom, String password) {
		// TODO - implement CompagniesController.addEmploye
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public compagnieEmploye readEmploye(int id) {
		// TODO - implement CompagniesController.readEmploye
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param employe
	 * @param password
	 */
	public boolean editEmploye(compagnieEmploye employe, String password) {
		// TODO - implement CompagniesController.editEmploye

		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param employe
	 */
	public boolean deleteEmploye(compagnieEmploye employe) {
		// TODO - implement CompagniesController.deleteEmploye
		throw new UnsupportedOperationException();
	}

}