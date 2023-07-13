import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Classe data qui représente chaque service dans la base de données. Un service peut-etre donné par un pro et recu par
 * plusieurs membres.
 */
public class Service {
	
	private String name;
	private int codeService;
	private double frais;
	private LocalDateTime dateService;
	private	ArrayList<Client> listeMembre;
	private ArrayList<Client> listeMembreConf;
	private LocalDateTime dateActuelle;
	private String commentaire;
	private int capacite;
	private int numProfessionel;

	/**
	 * constructeur
	 * @param name
	 * @param codeService : généré par le système
	 * @param frais
	 * @param dateService
	 * @param dateActuelle : gérée par le systeme
	 * @param capacite
	 * @param commentaire
	 * @param numProfessionel : pro attaché à la science
	 */
	public Service(String name, int codeService, double frais, LocalDateTime dateService, LocalDateTime dateActuelle, int capacite, String commentaire, int numProfessionel) {
		
		this.name = name;
		this.codeService = codeService;
		this.frais = frais;
		this.dateActuelle = dateActuelle;
		this.dateService = dateService;
		this.capacite = capacite;
		this.commentaire = commentaire;
		this.listeMembre = new ArrayList<Client>(0);
		this.listeMembreConf = new ArrayList<Client>(0);
		this.numProfessionel = numProfessionel;
		
	}
	
	// GETTERS
	public String getName() {return this.name;}
	public int getCodeService() {return this.codeService;}
	public double getFrais() {return this.frais;}
	public LocalDateTime getDateCreation() {return this.dateActuelle;}
	public LocalDateTime getDateService() {return this.dateService;}
	public int getCapacite() {return this.capacite;}
	public String getCommentaire() {return this.commentaire;}
	public ArrayList<Client> getListeMembre() {return this.listeMembre;}
	public ArrayList<Client> getListMembreConf() {return this.listeMembreConf;}
	public int getNumProfessionel() {return this.numProfessionel;}
	
	// SETTERS
	public void setFrais(double tmp) {this.frais = tmp;}
	public void setCodeService(int tmp) {this.codeService = tmp;}
	public void setDateService(LocalDateTime tmp) {this.dateService = tmp;}
	public void setCapacite(int tmp) {this.capacite = tmp;}
	public void setCommentaire(String tmp) {this.commentaire = tmp;}
	public void setName(String tmp) {this.name = tmp;}
	
	//METHODES
	public void addList(Client toAdd) {this.listeMembre.add(toAdd);}
	public void addListConf(Client toAdd) {this.listeMembreConf.add(toAdd);}
	
}