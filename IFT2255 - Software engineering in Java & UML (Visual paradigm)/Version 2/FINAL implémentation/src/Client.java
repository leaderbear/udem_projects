import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Classe data qui represente chaque Client de Gym (Membre ou Pro)
 */
public class Client {
	
	private int numeroClient;
	private boolean isSuspended;
	private ArrayList<Double> listPaiments;
	private String nom;
	private String prenom;
	private LocalDateTime dateInscription;
	private ArrayList<Service> listeService;
	private String address;
	private String town;
	private String postCode;
	private String courriel;

	/**
	 *  Le constructeur d'un client
	 * @param numeroClient
	 * @param nom
	 * @param prenom
	 * @param date
	 * @param isSus : S'il est suspendu selon s'il s'est acquité ou non de ses frais
	 * @param address
	 * @param town
	 * @param postCode
	 * @param courriel
	 */
	public Client(int numeroClient, String nom, String prenom, LocalDateTime date, boolean isSus, String address, String town, String postCode, String courriel) {
		
		this.numeroClient = numeroClient;
		this.nom = nom;
		this.prenom = prenom;
		this.isSuspended = isSus;
		this.dateInscription = date;
		this.listPaiments = new ArrayList<Double>(0);
		this.listeService = new ArrayList<Service>(0);
		this.address = address;
		this.town = town;
		this.postCode = postCode;
		this.courriel = courriel;
		
	}

	/**
	 *
	 * @return True si le client est un pro , ou false si c'est un membre
	 */
	public boolean getIsPro() {return false;}
	
	// GETTERS
	public int getNumeroClient() {return this.numeroClient;}
	public boolean getIsSuspended() {return this.isSuspended;}
	public ArrayList<Double> getListPaiments() {return this.listPaiments;}
	public String getNom() {return this.nom;}
	public String getPrenom() {return this.prenom;}
	public LocalDateTime getDateInscription() {return this.dateInscription;}
	public ArrayList<Service> getListService() {return this.listeService;} // Liste des services inscrits pour un membre, ou organisé pour un pro
	public int getIndexListeService(int codeService) {
		for(int i = 0; i<this.listeService.size(); i++) {
			if(this.listeService.get(i).getCodeService() == codeService) {return i;}
		}
		return -1;
	}
	public String getFullAddress() {return this.address+" "+this.postCode+", "+this.town;}
	public String getCourriel() {return this.courriel;}
	
	// SETTERS
	public void setNumeroClient(int tmp) {this.numeroClient = tmp;}
	public void setIsSuspended(boolean tmp) {this.isSuspended = tmp;}
	public void setNom(String tmp) {this.nom = tmp;}
	public void setPrenom(String tmp) {this.prenom = tmp;}
	public void setAddress(String tmp) {this.address = tmp;}
	public void setTown(String tmp) {this.town = tmp;}
	public void setPostCode(String tmp) {this.postCode = tmp;}
	public void setDateInscription(LocalDateTime tmp) {this.dateInscription = tmp;}
	public void setListService(ArrayList<Service> tmp) {this.listeService = tmp;}
	public void setCourriel(String tmp) {this.courriel = tmp;}
	
	//METHODES
	public void removeListService(int i) {this.listeService.remove(i);}
	public void addListService(Service tmp) {this.listeService.add(tmp);}
}