package Reservation;

import Transport.*;
import Voyage.*;

import java.util.Calendar;

public abstract class Reservation {

	private Section choosedSection;
	private Place choosedPlace;
	private Confirmation conf;
	private Voyage choosedVoyage;
	private int id;

	public int confirmReservation() {
		// TODO - implement Reservation.confirmReservation
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
		// TODO - implement Reservation.payReservation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public boolean cancelReservation(int id) {
		// TODO - implement Reservation.cancelReservation
		throw new UnsupportedOperationException();
	}

}