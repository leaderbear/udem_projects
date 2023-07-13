import java.util.ArrayList;

public class Service {
	
	private int codeService;
	private double frais;
	private Date dateService;
	private	ArrayList<Client> listeMembre;
	private ArrayList<Client> listeMembreConf;
	private Date dateActuelle;
	private String commentaire;
	private int capacite;
	
	
	public Service(int codeService, double frais, Date dateService, Date dateActuelle, int capacite, String commentaire) {
		
		this.codeService = codeService;
		this.frais = frais;
		this.dateActuelle = dateActuelle;
		this.dateService = dateService;
		this.capacite = capacite;
		this.commentaire = commentaire;
		this.listeMembre = new ArrayList<Client>(0);
		this.listeMembreConf = new ArrayList<Client>(0);
		
	}
	
	// GETTERS
	public int getCodeService() {return this.codeService;}
	public double getFrais() {return this.frais;}
	public Date getDateCreation() {return this.dateActuelle;}
	public Date getDateService() {return this.dateService;}
	public int getCapacite() {return this.capacite;}
	public String getCommentaire() {return this.commentaire;}
	public ArrayList<Client> getListeMembre() {return this.listeMembre;}
	public ArrayList<Client> getListMembreConf() {return this.listeMembreConf;}
	
	// SETTERS
	public void setFrais(double tmp) {this.frais = tmp;}
	public void setCodeService(int tmp) {this.codeService = tmp;}
	public void setDateService(Date tmp) {this.dateService = tmp;}
	public void setCapacite(int tmp) {this.capacite = tmp;}
	public void setCommentaire(String tmp) {this.commentaire = tmp;}
	
	//METHODES
	public void addList(Client toAdd) {this.listeMembre.add(toAdd);}
	public void addListConf(Client toAdd) {this.listeMembreConf.add(toAdd);}
	
}