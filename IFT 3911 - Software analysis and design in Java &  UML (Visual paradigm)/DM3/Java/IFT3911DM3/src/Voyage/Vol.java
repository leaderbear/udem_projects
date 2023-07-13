package Voyage;

import Batiment.Aeroport;
import Batiment.Batiment;
import Compagnie.Compagnie;
import Compagnie.CompagnieDeVol;
import Transport.Avion;
import Transport.Vehicule;

public class Vol extends Voyage {

	public Vol(CompagnieDeVol c, Avion v, String id, Aeroport depart, Aeroport arrivee, String dateDepart,
			String dateArrivee) {
		super(c, v, id, depart, arrivee, dateDepart, dateArrivee);
		// TODO Auto-generated constructor stub
		
	}
}