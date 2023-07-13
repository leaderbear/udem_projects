package CommandeFactory;

import Batiment.*;
import Compagnie.*;
import Transport.*;
import Voyage.*;

public class TrainFactory extends VoyageFactory {

	private static VoyageFactory instance = new TrainFactory();

	
	public Vehicule createVehicule(String id, String nom) {
		// TODO - implement TrainFactory.createVehicule
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param v
	 */
	public Voyage createVoyage(Voyage v) {
		// TODO - implement TrainFactory.createVoyage
		throw new UnsupportedOperationException();
	}

	private TrainFactory() {	}

	public static VoyageFactory getInstance() {
		return instance;
	}

	@Override
	public Compagnie createCompagnie(String id, String nom) {
		Compagnie c = new LigneDeTrain(id, nom);
		valideIdCompagnie(id);
		return c;
	}

	@Override
	public Batiment createBatiment(String id, String ville) {
		Batiment b = new Gare(id, ville);
		valideIdBatiment(id);
		return b;
	}

	@Override
	public Voyage createVoyage(Compagnie c, Vehicule v, String id, Batiment depart, Batiment arrivee, String dateDebut, String dateArrivee) {
		Voyage _v = new VoyageFerroviaire((LigneDeTrain) c, (Train) v, id, (Gare) depart, (Gare) arrivee, dateDebut, dateArrivee);
		valideIdVoyage(id);
		return _v;
	}

}