import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.time.LocalDateTime;

/**
 * Cette classe génère la synthèse chaque semaine.
 */
public class AutoProc {

	/**
	 *
	 * @param clients : obtenir la liste des clients de #gym pour faire la procédure comptable
	 * @param services : obtenir la liste des services de #gym pour faire la procédure comptable
	 */
	public static void procComptable(Clients clients, Services services) {
		
		System.out.println("Execution de la proc�dure comptable en cours!");
		
		// R�cuperer les professionels
		ArrayList<Client> mbs = clients.getListeClients();
		
		for(Client mb : mbs) {
			
			String filename = Integer.toString(mb.getNumeroClient()) + ".tef";
			String content = Integer.toString(mb.getNumeroClient()) +"\n" + mb.getNom() + " " + mb.getPrenom() + "\n";
			double toPay = 0;
			String factureContent = "Nom: "+mb.getNom()+"\nNum�ro: "+Integer.toString(mb.getNumeroClient())+"\nAdresse: "+mb.getFullAddress()+"\n";
			String factureName = "FACTURE__"+mb.getPrenom()+"_"+mb.getNom()+"("+mb.getNumeroClient()+").txt";
			
			ArrayList<Service> toIter = mb.getListService();
			
			for(Service toCount : toIter) {
				
				if (toCount.getDateService().isBefore(LocalDateTime.now()) && toCount.getDateService().isAfter(LocalDateTime.now().minusDays(7))) {
					
					if (mb.getIsPro()) {  // Completer la facture pour un pro
						double toAdd = toCount.getListeMembre().size();
						double toMult = toCount.getFrais();
						toPay += (toAdd * toMult);
						factureContent += "\nService: " + toCount.getName();
						factureContent += "\nDate du service: "+toCount.getDateService().toString();
						factureContent += "\nCode de la s�ance: "+ Integer.toString(toCount.getCodeService());
						factureContent += "\nMontant � payer:"+Double.toString(toPay);
						factureContent += "\n";
					} else { // Facture d'un membre normal
						factureContent += "\nService: " + toCount.getName();
						factureContent += "\nDate du service: "+toCount.getDateService().toString();
						factureContent += "\nCode de la s�ance: "+ Integer.toString(toCount.getCodeService());
						factureContent += "\nPrix du service:" + Double.toString(toCount.getFrais());
						factureContent += "\n";
						
					}
				}
			}
			
			if (mb.getIsPro()) { // Completer le TEF si c'est un pro
				content += "$" + Double.toString(toPay);
				
				try {
					File tef = new File(filename);
					FileWriter writer = new FileWriter(filename);
					writer.write(content);
					writer.close();
	
				} catch (IOException e) {
					System.out.println("Cr�ation de fichier TEF �chou�e pour membre: "+Integer.toString(mb.getNumeroClient()));
					e.printStackTrace();
				}
			}
			
			try {
				File facture = new File(factureName);
				FileWriter writer = new FileWriter(factureName);
				writer.write(factureContent);
				writer.close();

			} catch (IOException e) {
				System.out.println("Cr�ation de facture �chou�e pour membre: "+Integer.toString(mb.getNumeroClient()));
				e.printStackTrace();
			}
		}
		
		genererSynthese(clients,services);
		
	}

	/**
	 * Recuperer les fichiers de RnB et mettre a jour la liste des paiments des clients
	 * @param clients
	 * @param services
	 */
	public static void checkPayments(Clients clients, Services services) {
		
		// R�cuperer les fichiers de RnB et mettre � jour la liste des paiments des clients
		
	}

	/**
	 * Cette fonction generese la synthese et essaye d'ecrire les donnees sur un fichier externe
	 * @param clients
	 * @param services
	 */
	public static void genererSynthese(Clients clients, Services services) {
		
		String filename = "synth�se.txt";
		
		double total = 0;
		int proCount = 0;
		int servCount = 0;
		
		
		ArrayList<Client> pros = clients.getPros();
		
		for (Client pro : pros) {
			
			proCount += 1;
			ArrayList<Service> toIter = pro.getListService();
			
			for(Service toCount : toIter) {
				
				if (toCount.getDateService().isBefore(LocalDateTime.now()) && toCount.getDateService().isAfter(LocalDateTime.now().minusDays(7))) {
				
					double toAdd = toCount.getListeMembre().size();
					double toMult = toCount.getFrais();
					total += (toAdd * toMult);
					servCount += 1;
					
				}
			}
		}
		
		String content = "Nombre de professionels ayant fourni un service cette semaine: "+Integer.toString(proCount)+"\nFrais � payer: $"+Double.toString(total)+"\nNombre de services cette semaine: "+Integer.toString(servCount);
		
		try {
				File tef = new File(filename);
				FileWriter writer = new FileWriter(filename);
				writer.write(content);
				writer.close();

			} catch (IOException e) {
				System.out.println("Cr�ation de la synth�se �chou�e: ");
				e.printStackTrace();
		}

		
	}
}