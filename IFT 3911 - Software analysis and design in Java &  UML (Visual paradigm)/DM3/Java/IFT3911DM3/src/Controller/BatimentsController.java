package Controller;
import java.util.ArrayList;

import Batiment.*;
import Compagnie.Compagnie;

public class BatimentsController extends Controller {

	private static ArrayList<Batiment> listeBatiments = new ArrayList<Batiment>();

	
	//verifier si le ID est present
	public static boolean valideID(String id) {
		for(Batiment c : listeBatiments) {
			if( c.getId() == id) {
				return false;
			}
		}
		return true;
	}
		
	/**
	 * 
	 * @param nom
	 * @param lieu
	 */
	public void createBatiment(Batiment b) {
		listeBatiments.add(b);
	}

	/**
	 * 
	 * @param id
	 */
	public void editBatiment(int id) {
		// TODO - implement BatimentsController.editBatiment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public void deleteBatiment(int id) {
		// TODO - implement BatimentsController.deleteBatiment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Batiment readBatiment(int id) {
		// TODO - implement BatimentsController.readBatiment
		throw new UnsupportedOperationException();
	}

}