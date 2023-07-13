import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe controleur qui permet de gérer tous les services dans la base de données. Un service est donné par un pro , et
 * peut etre recu par plusieurs membres valides
 */
public class Services {

    private ArrayList<Service> servicesListe= new ArrayList<Service>(0);
    private int nombreServices = servicesListe.size();

	// public void protoAddService(int num, LocalDateTime dateService, LocalDateTime dateActuelle, Double frais, String commentaire, int capacite) { // CETTE METHODE N'EXISTE QUE POUR PERMETTRE LES DONNEES HARDCODE DU PROTOTYPE
		
		// Service service = new Service(num, frais, dateService, dateActuelle, capacite, commentaire);
        // this.servicesListe.add(service);
	
	// }

	/**
	 * Fonction qui ajoute une service dans la base de donnés et dans la liste des services donnés par un pro.
	 * @param name
	 * @param dateService
	 * @param dateActuelle
	 * @param frais
	 * @param commentaire
	 * @param capacite
	 * @param numProfessionel
	 * @return code du service
	 */
    public int addService(String name, LocalDateTime dateService, LocalDateTime dateActuelle, Double frais, String commentaire, int capacite, int numProfessionel) {
		
		int num = genererCode();
        Service service = new Service(name, num, frais, dateService, dateActuelle, capacite, commentaire, numProfessionel);
        this.servicesListe.add(service);
		return num;
    }

	/**
	 * Fonction mettant à jour un service
	 * @param toChange : l'objet service à mettre à jour
	 * @param newName
	 * @param newFrais
	 * @param newCap
	 * @param newLocalDate
	 * @param newComment
	 */
	public void updateService(Service toChange,String newName, String newFrais, String newCap, String newLocalDate, String newComment) {
		
		if (!(newName.equals(""))) {
			toChange.setName(newName);
		}
		
		
		if(!(newFrais.equals(""))) {
			toChange.setFrais(Double.parseDouble(newFrais));
		}
		
		if(!(newCap.equals(""))) {
			toChange.setCapacite(Integer.parseInt(newCap));
		}
		
		if(!(newLocalDate.equals(""))) {
			String[] tmp = newLocalDate.split("-");
			String heureStr = (tmp[3]+"."+tmp[4]);
			double heure = Double.parseDouble(heureStr);
			LocalDateTime dateService = LocalDateTime.of(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]), (int)heure ,(int)((heure - (int)heure) *100));
			toChange.setDateService(dateService);
		}
		
		if(!(newComment.equals(""))) {
			toChange.setCommentaire(newComment);
		}
		
	}

	/**
	 * Fonction qui permet de retrouver un service dans la base de données en utilisant son code service associé
	 * @param codeService
	 * @return objet service
	 */
    public Service getService(int codeService){
        for (int i = 0; i < servicesListe.size(); i++) {
            if (servicesListe.get(i).getCodeService() == codeService) {return servicesListe.get(i);}}
        return null;
    }

	/**
	 * Fonction qui permet de mettre à jour des informations d'un service
	 * @param service
	 * @param date
	 * @param frais
	 * @param commentaire
	 * @param capacite
	 */
    public void updateService(Service service, LocalDateTime date, Double frais, String commentaire, int capacite){

        service.setDateService(date);
        service.setFrais(frais);
        service.setCommentaire(commentaire);
		service.setCapacite(capacite);
    }

	/**
	 * Fonction qui permet de supprimer un service de la base de données et de la liste de services données par un pro
	 * @param service à supprimer
	 */
	public void deleteService(Service service){
        servicesListe.remove(servicesListe.indexOf(service));
    }

	/**
	 * Fonction interne qui génère un code unique et random pour chaque service
	 * @return le code généré
	 */
	public int genererCode() { 
	
		Random rand = new Random();
		int[] curNumbers = new int[this.servicesListe.size()];
		
		// Récupérer la liste des nombres existants 
		for (int index = 0; index < this.servicesListe.size(); index++) {
			curNumbers[index] = this.servicesListe.get(index).getCodeService();
		}
		
		int randNum = rand.nextInt((9999999 - 1000000) + 1) + 1000000;
		
		// Keep generating numbers until one isn't in the list
		while (Arrays.asList(curNumbers).contains(randNum)) {
			randNum = rand.nextInt((9999999 - 1000000) + 1) + 1000000;
		}
		
		return randNum;
	
	}

	/**
	 * Fonction utilisé par l'agent pour trouver un service à un membre qui shouaite s'inscrire à une séance donnée
	 * dans une journée donnée.
	 * @param jour voulu pour le service
	 * @param mois voulu pour le service
	 * @param ans voulu pour le service
	 * @return une liste de services ou rien
	 */
	public ArrayList<Service> trouverService(int jour, int mois, int ans) {
		
		ArrayList<Service> toReturn = new ArrayList<Service>(0);
		
		for (Service toCheck : this.servicesListe) {
			
			LocalDateTime tmp = toCheck.getDateService();
			
			if(tmp.getDayOfMonth() == jour && tmp.getMonthValue() == mois && tmp.getYear() == ans) {toReturn.add(toCheck);}
			
			
		}
		return toReturn;
	}
	
}
