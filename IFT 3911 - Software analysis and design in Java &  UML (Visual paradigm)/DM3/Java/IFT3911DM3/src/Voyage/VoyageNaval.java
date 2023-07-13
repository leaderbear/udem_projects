package Voyage;


import Batiment.*;

import Compagnie.CompagnieDeCroisiere;
import Transport.Bateau;
import java.util.ArrayList;

public class VoyageNaval extends Voyage {
	
	private ArrayList<Port> listeItineraires = new ArrayList<Port>();
	public VoyageNaval(CompagnieDeCroisiere c, Bateau v, String id, Port depart, Port arrivee, String dateDepart,
			String dateArrivee) {
		super(c, v, id, depart, arrivee, dateDepart, dateArrivee);
		// TODO Auto-generated constructor stub
	}

	private ArrayList<Batiment> itineraire = new ArrayList<Batiment>();

	public ArrayList<Batiment> getItenraire() {
		// TODO - implement VoyageNaval.getItenraire
		throw new UnsupportedOperationException();
	}

	public ArrayList<Batiment> getItenraireSansDepart() {
		// TODO - implement VoyageNaval.getItenraireSansDepart
		throw new UnsupportedOperationException();
	}
	
	public void setItineraire(ArrayList<Port> l) {
		this.listeItineraires = l;
	}

}