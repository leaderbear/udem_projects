import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;

public class AutoProc {

	public static void procComptable(Clients clients, Services services) {  // Ne gère pas encore les differentes semaines
		
		// Récuperer les professionels
		ArrayList<Client> pros = clients.getPros();
		
		for(Client pro : pros) {
			
			String filename = Integer.toString(pro.getNumeroClient()) + ".tef";
			String content = Integer.toString(pro.getNumeroClient()) +"\n" + pro.getNom() + " " + pro.getPrenom() + "\n";
			double toPay = 0;
			
			for(Service toCount : pro.getListService()) {
				
				double toAdd = toCount.getListeMembre().size();
				double toMult = toCount.getFrais();
				toPay += (toAdd * toMult);
			}
			
			content += "$" + Double.toString(toPay);
			
			try {
				File tef = new File(filename);
				FileWriter writer = new FileWriter(filename);
				writer.write(content);
				writer.close();

			} catch (IOException e) {
				System.out.println("Création de fichier TEF échouée pour professionel: "+Integer.toString(pro.getNumeroClient()));
				e.printStackTrace();
			}
		}
		
		genererSynthese(clients,services);
		
	}
	
	public static void checkPayments(Clients clients, Services services) {
		
		// Récuperer les fichiers de RnB et mettre à jour la liste des paiments des clients
		
	}
	
	public static void genererSynthese(Clients clients, Services services) { // TODO: Methode de génération de synthese pour le gérant. Appelé par procComptable.
		
		// Actuellement les données sont hardcodées pour le fichier de synthèse
		
		String filename = "synthèse.txt";
		String content = "Nombre de professionels ayant fourni un service cette semaine: 42\nFrais à payer: $42\nNombre de services cette semaine: 42";
		
		try {
				File tef = new File(filename);
				FileWriter writer = new FileWriter(filename);
				writer.write(content);
				writer.close();

			} catch (IOException e) {
				System.out.println("Création de la synthèse échouée: ");
				e.printStackTrace();
		}
		
	}
}