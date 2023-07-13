import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class Clients{

    private ArrayList<Client> listeClient= new ArrayList<Client>(0);
    private int nombreClients = listeClient.size();
	
	public void protoAddClient(int num, String nom, String prenom, boolean isPro, Date date, boolean isSus) { // CETTE METHODE N'EXISTE QUE POUR PERMETTRE LES DONNEES HARDCODE DU PROTOTYPE
	
		Client client = new Client(num,nom,prenom,isPro,date,isSus);
		this.listeClient.add(client);
	}
    public int addClient(String nom, String prenom, boolean isPro, Date date, boolean isSus){

        int num = genererNumeroClient();
 
		Client client = new Client(num,nom,prenom,isPro,date,isSus);
        this.listeClient.add(client);
		return num;
    }
	
    public Client getClient(int numeroClient){
        for (int i = 0; i < listeClient.size(); i++) {
            if (listeClient.get(i).getNumeroClient() == numeroClient) {return listeClient.get(i);}}
        return null;
    }
	
    public void updateClient(Client client, String nom, String prenom, Boolean isPro){
		if(!(nom.equals(""))) {
			client.setNom(nom);
		}
		
		if(!(prenom.equals(""))) { 
			client.setPrenom(prenom);
		}
		
        client.setIsPro(isPro);

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

    public void addServiceClient(Service s , Client c ){
        c.addListService(s);
    }

    public void removeServiceClient(Service s, Client c){
        int i = c.getListService().indexOf(s);
        c.removeListService(i);
    }
	
	public ArrayList<Client> getPros() {
		
		ArrayList<Client> toReturn= new ArrayList<Client>(0);
		
		for (Client toCheck : this.listeClient) {
			
			if (toCheck.getIsPro()) {toReturn.add(toCheck);}
			
		}
		return toReturn;
	}
	
}
