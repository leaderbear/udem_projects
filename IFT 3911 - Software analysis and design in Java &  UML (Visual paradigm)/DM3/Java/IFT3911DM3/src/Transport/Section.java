package Transport;

public abstract class Section {

	private int placesMax;
	private double prix;
	private int placesOccupées;
	private int[] listePlaces;
	private int numeroSection;

	/**
	 * 
	 * @param numéro
	 */
	public Place readPlace(int numéro) {
		// TODO - implement Section.readPlace
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param numero
	 */
	public Place createPlace(int numero) {
		// TODO - implement Section.createPlace
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Place
	 * @param numéro
	 */
	public void uptadePlace(int Place, int numéro) {
		// TODO - implement Section.uptadePlace
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Place
	 */
	public void deletePlace(int Place) {
		// TODO - implement Section.deletePlace
		throw new UnsupportedOperationException();
	}

}