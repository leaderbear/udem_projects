package Voyage;

import java.util.ArrayList;

import Batiment.Gare;
import Compagnie.LigneDeTrain;
import Transport.Train;

public class VoyageFerroviaire extends Voyage {
	ArrayList<Gare> listeGares = new ArrayList<Gare>();
	
	public VoyageFerroviaire(LigneDeTrain c, Train v, String id, Gare depart, Gare arrivee, String dateDepart,
			String dateArrivee) {
		super(c, v, id, depart, arrivee, dateDepart, dateArrivee);
		// TODO Auto-generated constructor stub
	}
	
	public void setTrajets(ArrayList<Gare> l) {
		this.listeGares = l;
	}
}