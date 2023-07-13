package Reservation;

import Voyage.*;
import Transport.*;

import java.util.Calendar;

public class Utilisateur extends Personne {

	private String courreil;
	private String addresse;
	private int telephone;
	private String passportNumber;
	private String passportExpDate;
	private Reservation[] listeReservations;

	/**
	 * 
	 * @param v
	 * @param s
	 * @param p
	 */
	public int createReservation(Voyage v, Section s, Place p) {
		// TODO - implement Utilisateur.createReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public void editReservation(int id) {
		// TODO - implement Utilisateur.editReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public boolean deleteReservation(int id) {
		// TODO - implement Utilisateur.deleteReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public int confirmReservation(int id) {
		// TODO - implement Utilisateur.confirmReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public boolean cancelReservation(int id) {
		// TODO - implement Utilisateur.cancelReservation
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
		// TODO - implement Utilisateur.payReservation
		throw new UnsupportedOperationException();
	}

}