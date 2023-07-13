import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe controlleur des clients. Qui permet de gérer tous les clients inscrits au gym
 */
public class Clients{

    private ArrayList<Client> listeClient= new ArrayList<Client>(0);
    private int nombreClients = listeClient.size();
	
	// public void protoAddClient(int num, String nom, String prenom, boolean isPro, LocalDateTime date, boolean isSus) { // CETTE METHODE N'EXISTE QUE POUR PERMETTRE LES DONNEES HARDCODE DU PROTOTYPE
	
		// Client client = new Client(num,nom,prenom,isPro,date,isSus);
		// this.listeClient.add(client);
	// }

	/**
	 * Fonction qui ajoute un client dans la base de donnés
	 * @param nom
	 * @param prenom
	 * @param isPro : es-il un pro ou un membre
	 * @param date
	 * @param isSus : est-il suspendu ou valide
	 * @param address
	 * @param town
	 * @param postCode
	 * @param courriel
	 * @return retourne son numéro (code) client
	 */
    public int addClient(String nom, String prenom, boolean isPro, LocalDateTime date, boolean isSus, String address, String town, String postCode, String courriel){

        int num = genererNumeroClient();
		
	
		if(isPro) {
			Professionel client = new Professionel(num,nom,prenom,date,isSus,address,town,postCode,courriel);
			this.listeClient.add(client);
		} else {
			Client client = new Client(num,nom,prenom,date,isSus,address,town,postCode,courriel);
			this.listeClient.add(client);
		}
        
		return num;
    }

	/**
	 * retrouver un client dans la base de donnée en utilisant son numéro client
	 * @param numeroClient
	 * @return objet client
	 */
    public Client getClient(int numeroClient){
        for (int i = 0; i < listeClient.size(); i++) {
            if (listeClient.get(i).getNumeroClient() == numeroClient) {return listeClient.get(i);}}
        return null;
    }
	
    public void updateClient(Client client, String nom, String prenom, String address, String town, String postCode, String courriel){
		if(!(nom.equals(""))) {
			client.setNom(nom);
		}
		
		if(!(prenom.equals(""))) { 
			client.setPrenom(prenom);
		}

		if(!(address.equals(""))) {
			client.setAddress(address);
		}

		if(!(town.equals(""))) {
			client.setTown(town);
		}

		if(!(postCode.equals(""))) {
			client.setPostCode(postCode);
		}
		
		if(!(courriel.equals(""))) {
			client.setCourriel(courriel);
		}
		

    }

    public void deleteClient(Client client){
        listeClient.remove(listeClient.indexOf(client));
    }

    public Boolean isPro(Client client){
        return client.getIsPro();
    }

	
    public Boolean checkClient(Client client){
        return client.getIsSuspended();
    }
	
	public int genererNumeroClient() {
		
		Random rand = new Random();
		int[] curNumbers = new int[this.listeClient.size()];
		
		// Récupérer la liste des nombres existants 
		for (int index = 0; index < this.listeClient.size(); index++) {
			curNumbers[index] = this.listeClient.get(index).getNumeroClient();
		}
		
		int randNum = rand.nextInt((999999999 - 100000000) + 1) + 100000000;
		
		// Keep generating numbers until one isn't in the list
		while (Arrays.asList(curNumbers).contains(randNum)) {
		
			randNum = rand.nextInt((999999999 - 100000000) + 1) + 100000000;
		
		}
		
		return randNum;
	}

	/**
	 * Permet d'ajouter un service à un client (Si pro, à la liste de services qu'il donne. Si membre, à sa liste de
	 * services qu'il recoit
	 * @param s :service
	 * @param c :client
	 */
    public void addServiceClient(Service s , Client c ){
        c.addListService(s);
    }

	/**
	 * Permet de supprimer un service dans la liste des services d'un client ( si pro, service qu'il donne. si membre,
	 * service qu'il recoit)
	 * @param s :service
	 * @param c :client
	 */
	public void removeServiceClient(Service s, Client c){
        int i = c.getListService().indexOf(s);
        c.removeListService(i);
    }

	/**
	 * Fonction qui retourne tous les pro inscrit dans la base de donnés
	 * @return liste de client pro.
	 */
	public ArrayList<Client> getPros() {
		
		ArrayList<Client> toReturn= new ArrayList<Client>(0);
		
		for (Client toCheck : this.listeClient) {
			
			if (toCheck.getIsPro()) {toReturn.add(toCheck);}
			
		}
		return toReturn;
	}
	
	//GETTER
	public ArrayList<Client> getListeClients() {return this.listeClient;}
	
}
