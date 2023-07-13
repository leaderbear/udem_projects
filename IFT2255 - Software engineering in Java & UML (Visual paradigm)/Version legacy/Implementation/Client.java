import java.util.ArrayList;

public class Client {
	
	private int numeroClient;
	private boolean isPro;
	private boolean isSuspended;
	private ArrayList<Double> listPaiments;
	private String nom;
	private String prenom;
	private Date dateInscription;
	private ArrayList<Service> listeService;
	
	public Client(int numeroClient, String nom, String prenom, boolean isPro, Date date, boolean isSus) {
		
		this.numeroClient = numeroClient;
		this.nom = nom;
		this.prenom = prenom;
		this.isPro = isPro;
		this.isSuspended = isSus;
		this.dateInscription = date;
		this.listPaiments = new ArrayList<Double>(0);
		this.listeService = new ArrayList<Service>(0);
		
	}
	
	// GETTERS
	public int getNumeroClient() {return this.numeroClient;}
	public boolean getIsPro() {return this.isPro;}
	public boolean getIsSuspended() {return this.isSuspended;}
	public ArrayList<Double> getListPaiments() {return this.listPaiments;}
	public String getNom() {return this.nom;}
	public String getPrenom() {return this.prenom;}
	public Date getDateInscription() {return this.dateInscription;}
	public ArrayList<Service> getListService() {return this.listeService;} // Liste des services inscrits pour un membre, ou organisé pour un pro
	public int getIndexListeService(int codeService) {
		for(int i = 0; i<this.listeService.size(); i++) {
			if(this.listeService.get(i).getCodeService() == codeService) {return i;}
		}
		return -1;
	}
	
	// SETTERS
	public void setNumeroClient(int tmp) {this.numeroClient = tmp;}
	public void setIsPro(boolean tmp) {this.isPro = tmp;}
	public void setIsSuspended(boolean tmp) {this.isSuspended = tmp;}
	public void setNom(String tmp) {this.nom = tmp;}
	public void setPrenom(String tmp) {this.prenom = tmp;}
	public void setDateInscription(Date tmp) {this.dateInscription = tmp;}
	public void setListService(ArrayList<Service> tmp) {this.listeService = tmp;}
	
	//METHODES
	public void removeListService(int i) {this.listeService.remove(i);}
	public void addListService(Service tmp) {this.listeService.add(tmp);}
}