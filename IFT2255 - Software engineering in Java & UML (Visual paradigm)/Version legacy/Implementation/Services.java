import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class Services {

    private ArrayList<Service> servicesListe= new ArrayList<Service>(0);
    private int nombreServices = servicesListe.size();

	public void protoAddService(int num, Date dateService, Date dateActuelle, Double frais, String commentaire, int capacite) { // CETTE METHODE N'EXISTE QUE POUR PERMETTRE LES DONNEES HARDCODE DU PROTOTYPE
		
		Service service = new Service(num, frais, dateService, dateActuelle, capacite, commentaire);
        this.servicesListe.add(service);
	
	}
    public int addService(Date dateService, Date dateActuelle, Double frais, String commentaire, int capacite) {
		
		int num = genererCode();
        Service service = new Service(num, frais, dateService, dateActuelle, capacite, commentaire);
        this.servicesListe.add(service);
		return num;
    }
	
	
	public void updateService(Service toChange, String newFrais, String newCap, String newDate, String newComment) {
		
		// Need better types for checking. Will change that for the next iteration.
		
		if(!(newFrais.equals(""))) {
			toChange.setFrais(Double.parseDouble(newFrais));
		}
		
		if(!(newCap.equals(""))) {
			toChange.setCapacite(Integer.parseInt(newCap));
		}
		
		if(!(newDate.equals(""))) {
			String[] tmp = newDate.split("-");
			String heureStr = (tmp[3]+"."+tmp[4]);
			double heure = Double.parseDouble(heureStr);
			Date dateService = new Date(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),heure);
			toChange.setDateService(dateService);
		}
		
		if(!(newComment.equals(""))) {
			toChange.setCommentaire(newComment);
		}
		
	}
	
    public Service getService(int codeService){
        for (int i = 0; i < servicesListe.size(); i++) {
            if (servicesListe.get(i).getCodeService() == codeService) {return servicesListe.get(i);}}
        return null;
    }

    public void updateService(Service service, Date date, Double frais, String commentaire, int capacite){

        service.setDateService(date);
        service.setFrais(frais);
        service.setCommentaire(commentaire);
		service.setCapacite(capacite);
    }

    public void deleteService(Service service){
        servicesListe.remove(servicesListe.indexOf(service));
    }
	
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
	
	public ArrayList<Service> trouverService(int jour, int mois, int ans) {
		
		ArrayList<Service> toReturn = new ArrayList<Service>(0);
		
		for (Service toCheck : this.servicesListe) {
			
			Date tmp = toCheck.getDateService();
			
			if(tmp.getJour() == jour && tmp.getMois() == mois && tmp.getAnnee() == ans) {toReturn.add(toCheck);}
			
			
		}
		return toReturn;
	}
	
}
