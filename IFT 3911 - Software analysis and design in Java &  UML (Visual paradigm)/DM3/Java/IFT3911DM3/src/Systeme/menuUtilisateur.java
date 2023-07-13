package Systeme;

import Voyage.*;
import Transport.*;
import Batiment.*;
import Reservation.*;

import java.util.Calendar;

public class menuUtilisateur extends Menu {

	private int utilisateursController;

	/**
	 * 
	 * @param courreil
	 * @param password
	 * @param adresse
	 * @param telephone
	 * @param passport
	 * @param passexp
	 */
	public int createUser(String courreil, String password, String adresse, int telephone, int passport, Calendar passexp) {
		// TODO - implement menuUtilisateur.createUser
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param courreil
	 * @param adresse
	 * @param telephone
	 * @param passport
	 * @param passexp
	 */
	public boolean editUser(String courreil, String adresse, int telephone, int passport, Calendar passexp) {
		// TODO - implement menuUtilisateur.editUser
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param courreil
	 * @param password
	 */
	public void deleteuser(int courreil, String password) {
		// TODO - implement menuUtilisateur.deleteuser
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voyage
	 * @param choosedSection
	 * @param choosedPlace
	 */
	public boolean createReservation(Voyage voyage, Section choosedSection, Place choosedPlace) {
		// TODO - implement menuUtilisateur.createReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param voyage
	 * @param choosedSection
	 * @param choosedPlace
	 */
	public boolean editReservation(int id, Voyage voyage, Section choosedSection, Place choosedPlace) {
		// TODO - implement menuUtilisateur.editReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public int confirmReservation(int id) {
		// TODO - implement menuUtilisateur.confirmReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param date
	 * @param depart
	 * @param arrivee
	 */
	public Voyage[] consulterVoyages(Calendar date, Batiment depart, Batiment arrivee) {
		// TODO - implement menuUtilisateur.consulterVoyages
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param chn
	 * @param n
	 * @param ccv
	 * @param exp
	 */
	public int payReservation(String chn, long n, int ccv, Calendar exp) {
		// TODO - implement menuUtilisateur.payReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param courreil
	 * @param Pass
	 */
	public Utilisateur readUser(String courreil, String Pass) {
		// TODO - implement menuUtilisateur.readUser
		throw new UnsupportedOperationException();
	}

	public void updateVIew() {
		// TODO - implement menuUtilisateur.updateVIew
		throw new UnsupportedOperationException();
	}



}