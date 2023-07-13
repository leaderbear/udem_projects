package CommandeFactory;

import Batiment.*;
import Compagnie.*;
import Transport.*;
import Voyage.*;

public class BateauFactory extends VoyageFactory {

	private static VoyageFactory instance = new BateauFactory();

	

	/**
	 * 
	 * @param v
	 */
	public Vehicule createVehicule(String id, String nom) {
		// TODO - implement BateauFactory.createVehicule
		throw new UnsupportedOperationException();
	}

	

	private BateauFactory() {
	}

	public static VoyageFactory getInstance() {
		return instance;
	}


	@Override
	public Compagnie createCompagnie(String id, String nom) {
		Compagnie c = new CompagnieDeCroisiere(id, nom);
		valideIdCompagnie(id);
		return c;
	}


	@Override
	public Batiment createBatiment(String id, String ville) {
		Batiment b = new Port(id, ville);
		valideIdBatiment(id);
		return b;
	}


	@Override
	public Voyage createVoyage(Compagnie c, Vehicule v, String id, Batiment depart, Batiment arrivee, String dateDebut, String dateArrivee) {
		Voyage _v = new VoyageNaval((CompagnieDeCroisiere) c, (Bateau) v, id, (Port) depart, (Port) arrivee, dateDebut, dateArrivee);
		valideIdVoyage(id);
		return _v;
	}

}