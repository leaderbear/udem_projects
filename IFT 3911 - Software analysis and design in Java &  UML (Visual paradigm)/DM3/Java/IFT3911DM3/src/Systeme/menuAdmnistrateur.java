package Systeme;

import Reservation.compagnieEmploye;
import Transport.Avion;
import Transport.Bateau;
import Transport.Train;
import Voyage.*;
import Batiment.*;
import CommandeFactory.AvionFactory;
import CommandeFactory.BateauFactory;
import CommandeFactory.TrainFactory;
import CommandeFactory.VoyageFactory;
import Compagnie.*;
import Controller.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class menuAdmnistrateur extends Menu {
	private VoyageFactory VF;
	private BatimentsController BC;
	private CompagniesController CC;
	private VehiculeController VH;
	private VoyagesController VC;
	
	public menuAdmnistrateur() {
		BC = new BatimentsController();
		CC = new CompagniesController();
		VH = new VehiculeController();
		
		selectMenu();
		
		
	}
	
	private void addData() {
		CompagnieDeVol c1 = new CompagnieDeVol("AIRCAN", "AIR CANADA");
		CompagnieDeCroisiere c2 = new CompagnieDeCroisiere("CROCAN", "CROISIERE CANADA");
		LigneDeTrain c3 = new LigneDeTrain("VIACAN", "VIA RAIL CANADA");
		CC.createCompagnie(c1);
		CC.createCompagnie(c2);
		CC.createCompagnie(c3);
		
		Aeroport a1 = new Aeroport("YUL", "MONTREAL");
		Aeroport a2 = new Aeroport("YYZ", "TORONTO");
		Aeroport a3 = new Aeroport("YVR", "VANCOUVER");
		BC.createBatiment(a1); BC.createBatiment(a2); BC.createBatiment(a3);

		Gare g1 = new Gare("GAA", "MONTREAL");
		Gare g2 = new Gare("GAB", "TORONTO");
		Gare g3 = new Gare("GAC", "VANCOUVER");
		BC.createBatiment(g1); BC.createBatiment(g2); BC.createBatiment(g3);

		Port p1 = new Port("PAA", "MONTREAL");
		Port p2 = new Port("PAB", "TORONTO");
		Port p3 = new Port("PAC", "VANCOUVER");
		BC.createBatiment(p1); BC.createBatiment(p2); BC.createBatiment(p3);
		
		Avion av1 = new Avion("AIRCAN01", "");
		Avion av2 = new Avion("AIRCAN02", "");
		VH.createVehicule(av1); VH.createVehicule(av2); 
		
		Bateau b1 = new Bateau("CROCAN01", "");
		Bateau b2 = new Bateau("CROCAN02", "");
		VH.createVehicule(b1); VH.createVehicule(b2);
		
		Train t1 = new Train("VIACAN01", "");
		Train t2 = new Train("VIACAN02", "");
		VH.createVehicule(t1);	VH.createVehicule(t2);


		Vol v1 = new Vol(c1, av1, "AC481", a1, a2, "2014.11.28:06:00", "2014.11.28:07:24");
		Vol v2 = new Vol(c1, av1, "AC581", a1, a2, "2015.11.28:06:00", "2015.11.28:07:24");
		Vol v3 = new Vol(c1, av2, "AC681", a2, a3, "2016.11.28:06:00", "2016.11.28:07:24");
		VC.createVoyage(v1); VC.createVoyage(v2);VC.createVoyage(v3);
		
		
		VoyageNaval vn1 = new VoyageNaval(c2, b1, "VN1960", p1, p2, "2019.06.01:13:00", "2019.06.22:13:00");
		ArrayList<Port> l1 = new ArrayList<Port>();
		l1.add(p1); l1.add(p2); l1.add(p1); l1.add(p2); l1.add(p3);
		vn1.setItineraire(l1);
		VoyageNaval vn2 = new VoyageNaval(c2, b2, "VN1910", p2, p3, "2019.10.01:12:00", "2019.10.22:12:00");
		l1.clear();
		l1.add(p2); l1.add(p1); l1.add(p2); l1.add(p3); l1.add(p1); l1.add(p3);
		vn2.setItineraire(l1);
		VoyageNaval vn3 = new VoyageNaval(c2, b1, "VN2010", p2, p3, "2020.10.01:12:00", "2020.10.22:12:00");
		l1.clear();
		l1.add(p2); l1.add(p1); l1.add(p2); l1.add(p3); l1.add(p1); l1.add(p3);
		vn3.setItineraire(l1);
		VC.createVoyage(vn1); VC.createVoyage(vn2); VC.createVoyage(vn3);
		
		VoyageFerroviaire vf1 = new VoyageFerroviaire(c3, t1, "VF2011", g1, g3, "2019.06.01:13:00", "2019.06.22:13:00");
		ArrayList<Gare> l2 = new ArrayList<Gare>();
		l2.clear();
		l2.add(g1); l2.add(g2); l2.add(g3);
		vf1.setTrajets(l2);
		VoyageFerroviaire vf2 = new VoyageFerroviaire(c3, t2, "VF2012", g2, g3, "2020.06.01:13:00", "2020.06.22:13:00");
		l2.clear();
		l2.add(g2); l2.add(g3);
		vf2.setTrajets(l2);
		VoyageFerroviaire vf3 = new VoyageFerroviaire(c3, t1, "VF2013", g3, g1, "2019.06.01:13:00", "2019.06.22:13:00");
		l2.clear();
		l2.add(g3); l2.add(g2); l2.add(g1);
		vf3.setTrajets(l2);
		VC.createVoyage(vf1); VC.createVoyage(vf2); VC.createVoyage(vf3);



	}
	
	public void createBatiment() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Choisir l'option :" +
				"\n 1. Creation d'Aeroport" + 
				"\n 2. Creation de Port" +
				"\n 3. Creation de Gare");
		int n = sc.nextInt();
		switch(n) {
		case 1 : createAeroport(); break;
		case 2 : createPort(); break;
		case 3 : createGare(); break;
		
		default : sc.close(); return;
		
		}
	}

	private void createGare() {
		VF = TrainFactory.getInstance();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrer Id pour la Gare : ");
		String id = sc.nextLine();
		System.out.println("Entrer la ville pour la Gare : ");
		String ville = sc.nextLine();
		
		Batiment g = VF.createBatiment(id, ville);
		BC.createBatiment(g);
		
		System.out.println("Gare ajoutée à la base de donnée Batiment");
		
	}
	private void createPort() {
		VF = BateauFactory.getInstance();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrer Id pour le Port : ");
		String id = sc.nextLine();
		System.out.println("Entrer la ville pour le Port : ");
		String ville = sc.nextLine();
		
		Batiment g = VF.createBatiment(id, ville);
		BC.createBatiment(g);
		
		System.out.println("Port ajoutée à la base de donnée Batiment");
	}
	private void createAeroport() {
		VF = AvionFactory.getInstance();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrer Id pour l'Aéroport : ");
		String id = sc.nextLine();
		System.out.println("Entrer la ville pour l'Aéroport : ");
		String ville = sc.nextLine();
		
		Batiment g = VF.createBatiment(id, ville);
		BC.createBatiment(g);
		
		System.out.println("Aéroport ajouté à la base de donnée Batiment");
		
	}
	public void editBatiment() {
		// TODO - implement menuAdmnistrateur.editBatiment
		throw new UnsupportedOperationException();
	}

	public void deleteBatiment() {
		// TODO - implement menuAdmnistrateur.deleteBatiment
		throw new UnsupportedOperationException();
	}

	public void createVehicule() {
		// TODO - implement menuAdmnistrateur.createVehicule
		throw new UnsupportedOperationException();
	}

	public void editVehicule() {
		// TODO - implement menuAdmnistrateur.editVehicule
		throw new UnsupportedOperationException();
	}

	public void deleteVehicule() {
		// TODO - implement menuAdmnistrateur.deleteVehicule
		throw new UnsupportedOperationException();
	}

	public void createSection() {
		// TODO - implement menuAdmnistrateur.createSection
		throw new UnsupportedOperationException();
	}

	public void editSection() {
		// TODO - implement menuAdmnistrateur.editSection
		throw new UnsupportedOperationException();
	}

	public void deleteSection() {
		// TODO - implement menuAdmnistrateur.deleteSection
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param date
	 * @param depart
	 * @param arrivee
	 */
	public Voyage[] consulterVoyages(Calendar date, Batiment depart, Batiment arrivee) {
		// TODO - implement menuAdmnistrateur.consulterVoyages
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param isAdmin
	 */
	public void selectMenu() {
		Scanner sc = new Scanner(System.in);
		int n = 1;
		while(n != 0) {
		System.out.println("Choisir le menu :"+ 
				"\n 1. Création de Compagnie" + 
				"\n 2. Création de Batiment" +
				
				"\n 0. Quit");
		
		
		n = sc.nextInt();
		
		switch (n) {
		case 1 : createCompagnie(); break;
		case 2 : createBatiment(); break;
		case 3 : createVoyage(); break;
		case 4 : createVehicule(); break;
		
		case 0 :
		default : sc.close(); return;
		
		}}
	}

	private void createVoyage() {
		// TODO Auto-generated method stub
		
	}
	
	public void createCompagnie() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Choisir l'option :" +
				"\n 1. Création de Compagnie de Vol	" + 
				"\n 2. Création de Compagnie de Croisière" +
				"\n 3. Création de Ligne de Train");
		int n = sc.nextInt();
		switch(n) {
		case 1 : createCompagnieDeVol(); break;
		case 2 : createCompagnieDeCroisiere(); break;
		case 3 : createLigneDeTrain(); break;
		
		default : sc.close(); return;
		}
	}

	private void createLigneDeTrain() {
		VF = TrainFactory.getInstance();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrer Id pour la Ligne de Train : ");
		String id = sc.nextLine();
		System.out.println("Entrer le nom pour la Ligne de Train : ");
		String nom = sc.nextLine();
		
		Compagnie g = VF.createCompagnie(id, nom);
		CC.createCompagnie(g);
		
		System.out.println("Ligne de Train ajoutée à la base de donnée Compagnie");
			
	}
	private void createCompagnieDeCroisiere() {
		VF = BateauFactory.getInstance();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrer Id pour la Compagnie de Croisière : ");
		String id = sc.nextLine();
		System.out.println("Entrer le nom pour la Compagnie de Croisière : ");
		String nom = sc.nextLine();
		
		Compagnie g = VF.createCompagnie(id, nom);
		CC.createCompagnie(g);
		
		System.out.println("Compagnie de Croisière ajoutée à la base de donnée Compagnie");
		
	}
	private void createCompagnieDeVol() {
		VF = AvionFactory.getInstance();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrer Id pour la Compagnie de vol : ");
		String id = sc.nextLine();
		System.out.println("Entrer le nom pour la Compagnie de vol : ");
		String nom = sc.nextLine();
		
		Compagnie g = VF.createCompagnie(id, nom);
		CC.createCompagnie(g);
		
		System.out.println("Compagnie de vol ajoutée à la base de donnée Compagnie");
		
	}
	/**
	 * 
	 * @param c
	 * @param n
	 * @param id
	 * @param password
	 */
	public void editCompagnie() {
		// TODO - implement menuAdmnistrateur.editCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 */
	public void deleteCompagnie() {
		// TODO - implement menuAdmnistrateur.deleteCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Compagnie readCompagnie(int id) {
		// TODO - implement menuAdmnistrateur.readCompagnie
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Batiment readBatiment(int id) {
		// TODO - implement menuAdmnistrateur.readBatiment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param nom
	 * @param prenom
	 * @param password
	 */
	public int addEmploye(Compagnie c, String nom, String prenom, String password) {
		// TODO - implement menuAdmnistrateur.addEmploye
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param employe
	 */
	public boolean deleteEmploye(Compagnie c, compagnieEmploye employe) {
		// TODO - implement menuAdmnistrateur.deleteEmploye
		throw new UnsupportedOperationException();

	}

	/**
	 * 
	 * @param c
	 * @param id
	 */
	public compagnieEmploye readEmploye(Compagnie c, int id) {
		// TODO - implement menuAdmnistrateur.readEmploye
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param c
	 * @param nom
	 * @param prenom
	 * @param password
	 */
	public boolean editEmploye(Compagnie c, String nom, String prenom, String password) {
		// TODO - implement menuAdmnistrateur.editEmploye
		throw new UnsupportedOperationException();
	}

	public void updateView() {
		// TODO - implement menuAdmnistrateur.updateView
		throw new UnsupportedOperationException();
	}

}