package CommandeFactory;

import Voyage.*;
import Batiment.*;
import Compagnie.*;
import Transport.*;

public class AvionFactory extends VoyageFactory {

	private static VoyageFactory instance = new AvionFactory();

	public static VoyageFactory getInstance() {
		return instance;
	}

	private AvionFactory() {
		
	}
	
	


	/**
	 * 
	 * @param v
	 */
	public Vehicule createVehicule(String id, String nom) {
		// TODO - implement AvionFactory.createVehicule
		throw new UnsupportedOperationException();
	}

	@Override
	public Compagnie createCompagnie(String id, String nom) {
		Compagnie c = new CompagnieDeVol(id, nom);
		valideIdCompagnie(id);
		return c;
		
	}

	@Override
	public Batiment createBatiment(String id, String ville) {
		Batiment b = new Aeroport(id, ville);
		valideIdBatiment(id);
		return b;
	}

	@Override
	public Voyage createVoyage(Compagnie c, Vehicule v, String id, Batiment depart, Batiment arrivee, String dateDebut, String dateArrivee) {
		Voyage _v = new Vol((CompagnieDeVol) c, (Avion) v, id, (Aeroport) depart, (Aeroport) arrivee, dateDebut, dateArrivee);
		valideIdVoyage(id);
		return _v;
	}

}