import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Classe interface permettant Ã  l'agent/(ou autre) d'inscrire/vÃ©rifier/mettre Ã  jour des membres/services
 * Ã  travers des commandes dÃ©finies dans le manuel afin de protÃ©ger les donnÃ©es.
 */
public class MenuAgent {
	
	private static Clients clients;
	private static Services services;
	private static TimeChecker timeChecker;
	
	public MenuAgent() {
		
		this.clients = new Clients();
		this.services = new Services();
		this.timeChecker = new TimeChecker();
		this.timeChecker.addData(this.clients,this.services);
		this.timeChecker.start(); 
		
	}
	
	
	// Tests pour ajouterCient
	@Test
	public void testAjouterClient() {
		
		// Test pour un membre
		String nom="The Hedgehog",prenom="Sonic";
		int num = this.ajouterClient(nom, prenom, false, "59 Rue Caulaincourt", "Paris", "75018", "TheBlueBlurr@gmail.com");
		Client returned = this.rechercherClient(num);
		assertNotNull("TEST ERROR: Cannot find client with number "+Integer.toString(num)+" Despite it being added", returned);
		assertEquals("TEST ERROR: Name of found client doesn't match the one specified when added", "The Hedgehog", returned.getNom());
		assertEquals("TEST ERROR: Normal client marks itself as a professional", false, returned.getIsPro());
		
		// Supprimer le membre ajouté pour les tests
		this.supprimerClient(returned);
		
		// Test pour un professionel
		nom="Robotnik";prenom="Dr. Ivo";
		num = this.ajouterClient(nom, prenom, true, "49 Rue Bourgelat", "Maison-Alfort", "94700", "EvilScientist@yahoo.fr");
		returned = this.rechercherClient(num);
		assertNotNull("TEST ERROR: Cannot find client with number "+Integer.toString(num)+" Despite it being added", returned);
		assertEquals("TEST ERROR: Name of found client doesn't match the one specified when added", "Robotnik", returned.getNom());
		assertEquals("TEST ERROR: Professional client doesn't mark itself as professional", true, returned.getIsPro());
		
		// Supprimer le professionel ajouté pour les tests
		this.supprimerClient(returned);
		
	}
	
	@Test
	public void testAddService() {
		// Le professionel auquel sera lié le service testé
		String nom="Robotnik",prenom="Dr. Ivo";
		int numPro = this.ajouterClient(nom, prenom, true, "49 Rue Bourgelat", "Maison-Alfort", "94700", "EvilScientist@yahoo.fr");
		int numService = this.addService("Entrainement à la course d'hérisson", "2019-12-13-15-30", 5.5, "Ceci est un commentaire", 10, numPro);
		Service returned = this.rechercherService(numService);
		assertNotNull("TEST ERROR: Cannot find service with number"+Integer.toString(numService)+" Despite it being added",returned);
		assertEquals("TEST ERROR: Name of found service doesn't match the one specified when added", "Entrainement à la course d'hérisson", returned.getName());
		assertEquals("TEST ERROR: Service isn't attributed to the right professional", numPro, returned.getNumProfessionel());
		assertTrue("TEST ERROR: Service isn't added to the professional's list of services", this.rechercherClient(numPro).getListService().contains(this.rechercherService(numService)));

	}

	
	// Tests pour Mise à jour Client
	@Test
	public void testmaJourClient() {
		
		// Test pour un membre
		String nom="The Hedgehog",prenom="Sonic";
		int num = this.ajouterClient(nom, prenom, false, "59 Rue Caulaincourt", "Paris", "75018", "TheBlueBlurr@gmail.com");
		Client returned = this.rechercherClient(num);
		this.maJourClient(returned, "Hedgehog", "Sonnic" , "30 Avenue" , "Marseille", "99900", "TheBlueBlurr@gmail.com");
		
		assertEquals("TEST ERROR: Name of found client doesn't match the one specified when uptaded", "Hedgehog", returned.getNom());
		assertEquals("TEST ERROR: Forename of found client doesn't match the one specified when uptaded", "Sonnic", returned.getPrenom());
		assertEquals("TEST ERROR: Address of found client doesn't match the one specified when uptaded", "30 Avenue 99900, Marseille", returned.getFullAddress());

		
		// Supprimer le membre ajouté pour les tests
		this.supprimerClient(returned);
		
		// Test pour un professionel
		nom="Robotnik";prenom="Dr. Ivo";
		num = this.ajouterClient(nom, prenom, true, "49 Rue Bourgelat", "Maison-Alfort", "94700", "EvilScientist@yahoo.fr");
		returned = this.rechercherClient(num);
		this.maJourClient(returned, "Robotnikk", "Mr. Ivo" , "30 Avenue" , "Marseille", "99900", "EvilScientist@yahoo.fr");
		assertEquals("TEST ERROR: Name of found client doesn't match the one specified when uptaded", "Robotnikk", returned.getNom());
		assertEquals("TEST ERROR: Forename of found client doesn't match the one specified when uptaded", "Mr. Ivo", returned.getPrenom());
		assertEquals("TEST ERROR: Address of found client doesn't match the one specified when uptaded", "30 Avenue 99900, Marseille", returned.getFullAddress());
		
		// Supprimer le professionel ajouté pour les tests
		this.supprimerClient(returned);
		
	}
	
	//Test pour Mise à jour Service
	@Test
	public void testmaJourService() {
		// Le professionel auquel sera lié le service testé
		String nom="Robotnik",prenom="Dr. Ivo";
		int numPro = this.ajouterClient(nom, prenom, true, "49 Rue Bourgelat", "Maison-Alfort", "94700", "EvilScientist@yahoo.fr");
		int numService = this.addService("Entrainement à la course d'hérisson", "2019-12-13-15-30", 5.5, "Ceci est un commentaire", 10, numPro);
		Service returned = this.rechercherService(numService);
		this.maJourService(returned, "Entrainement à la nage dauphin" , "6.0" , "20" , "2020-12-13-15-30", "Ceci n'est pas un commentaire");
		assertEquals("TEST ERROR: Name of found service doesn't match the one specified when uptaded", "Entrainement à la nage dauphin", returned.getName());
		assertEquals("TEST ERROR: Price of found service doesn't match the one specified when uptaded", 6.0 , returned.getFrais(), 0);
		assertEquals("TEST ERROR: Capacity of found service doesn't match the one specified when uptaded", 20 , returned.getCapacite());
		assertEquals("TEST ERROR: LocalDate of found service doesn't match the one specified when uptaded", "2020-12-13T15:30", returned.getDateService().toString());
		assertEquals("TEST ERROR: Comment of found service doesn't match the one specified when uptaded", "Ceci n'est pas un commentaire", returned.getCommentaire());
	}

	/**
	 * Fonction permettant d'ajouter un nouveau client(membre ou pro) dans la base de donnÃ©es avec les informations
	 * fournies par le client
	 * @param nom
	 * @param prenom
	 * @param isPro : veut-t-il s'inscrire en tant que pro ou membre
	 * @param address
	 * @param town
	 * @param postCode
	 * @param courriel : utilisÃ© pour pouvoir entrer dans le gym sans devoir parler Ã  l'agent
	 * @return : un code client unique qui va servir Ã  identifier le client quand il veut un service ou autre
	 */
	// Méthode appelée pour ajouter un client 
	public int ajouterClient(String nom, String prenom, boolean isPro, String address, String town, String postCode, String courriel) {
		
		// Créer l'objet date
		LocalDateTime dateInscription = LocalDateTime.now();
		
		int num = this.clients.addClient(nom, prenom, isPro, dateInscription,false, address, town, postCode, courriel);
		
		return num;
		
	}

	/**
	 * Fonction qui permet d'ajouter un service liÃ© Ã  un pro Ã  la base de donnÃ©es
	 * @param name
	 * @param dateToSplit
	 * @param frais
	 * @param commentaire
	 * @param capacite
	 * @param numProfessionel
	 * @return : code unique du service qui va servir Ã  identifier le service pour le modifier ou le supprimer
	 */
	public int addService(String name, String dateToSplit, Double frais, String commentaire, int capacite, int numProfessionel) {
		
		String[] tmp = dateToSplit.split("-");
		String heureStr = (tmp[3]+"."+tmp[4]);
		double heure = Double.parseDouble(heureStr);
		LocalDateTime dateService = LocalDateTime.of(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),(int)heure, (int)((heure - (int)heure) *100));
		
		LocalDateTime dateActuelle = LocalDateTime.now();
		
		int num = this.services.addService(name, dateService, dateActuelle, frais, commentaire, capacite, numProfessionel);
		
		// Ajouter ce service à la liste des services de ce professionel
		this.rechercherClient(numProfessionel).addListService(this.rechercherService(num));
		return num;
	}

	/**
	 * Permet de trouver tous les services donnÃ©s dans une journÃ©e donnÃ©e en utilisant la date de la journÃ©e
	 * @param jour
	 * @param mois
	 * @param ans
	 * @return liste de services (ou rien)
	 */
	public ArrayList<Service> trouverService(int jour, int mois, int ans) {
		
		ArrayList<Service> toReturn = services.trouverService(jour,mois,ans);
		return toReturn;
	}

	/**
	 * Retorune un objet client en utilisant son code client
	 * @param num
	 * @return objet client
	 */
	public Client rechercherClient(int num) {
		
		return this.clients.getClient(num);
	}

	/**
	 * Retourne un objet service en utilisant son code service
	 * @param num
	 * @return objet service
	 */
	public Service rechercherService(int num) {
		
		return this.services.getService(num);
	}

	/**
	 * Fonction qui met Ã  jour les information de n'importe quel client dans la base de donnÃ©es. Pour ne pas modifier
	 * une certain donnÃ©e , donner un argument vide comme suit ""
	 * @param toChange client Ã  mettre Ã  jour
	 * @param nom
	 * @param prenom
	 * @param address
	 * @param town
	 * @param postCode
	 * @param courriel
	 */
	public void maJourClient(Client toChange, String nom, String prenom, String address, String town, String postCode, String courriel) {

		this.clients.updateClient(toChange,nom,prenom, address, town, postCode, courriel);
	}

	/**
	 * Fonction qui met Ã  jour les information de n'importe quel service dans la base de donnÃ©es. Pour ne pas modifier
	 * une certain donnÃ©e, donner un argument vide comme suit """
	 * @param toChange :Service Ã  changer
	 * @param newName
	 * @param newFrais
	 * @param newCap
	 * @param newLocalDate
	 * @param newComment
	 */
	public void maJourService(Service toChange, String newName, String newFrais, String newCap, String newLocalDate, String newComment) {
		
		this.services.updateService(toChange,newName, newFrais,newCap,newLocalDate,newComment);
	}

	/**
	 * Fonction qui supprime de la base de donnÃ©es n'importe quel client
	 * @param toDelete : objet client Ã  supprimer
	 */
	public void supprimerClient(Client toDelete) {
		
		this.clients.deleteClient(toDelete);
		
	}

	/**
	 * Fonction main : la plus importante de cette classe. S'occupe d'intÃ©ragir avec l'agent Ã  travers la console avec
	 * des commandes prÃ©cises. Chaque input donnÃ© par l'agent (ou autre) dans la console est lu et le commande entrÃ©e
	 * est Ã©xcutÃ©e. Ces mÃ©thodes ont dÃ©jÃ  Ã©tÃ© dÃ©finies dans cette classe. Elles sont excutÃ©es de maniÃ¨re Ã  respecter
	 * les spÃ©cification de #gym. On utilise un scanner avec une boucle while infinie pour que le systÃ¨me soit toujours
	 * en fonction et prÃªt Ã  recevoir de nouvelles commandes. On utilise des cases pour savoir quelle commande est entrÃ©e
	 * @param args
	 */
	public static void main(String args[]) {
		
		String helpMsg = "Commandes disponibles:\nVerifier: Vérifier la validité d'un numéro.\nAjouter: Ajouter un nouveau client ou une séance.\nModifier: Modifier les informations d'un client ou d'un service.\nSupprimer: Supprimer un client ou un service.\nTrouver: Trouver les services à une date précise.\nInscrire: Inscrire un client à une séance.\nConfirmer: Confirmer sa présence à une séance.\nConsulter: Consulter le nombre de personnes inscrites et/ou confirmées à une séance.\nRechercher: Trouver un client ou un service à partir de son numéro.\nSynthese: Génére la synthése.\nAide: Affiche ce message.";
		System.out.println(helpMsg);
		MenuAgent menu = new MenuAgent();
		
		while(true) {  // Main Loop
			
			System.out.println("\nCommande:");
			Scanner input = new Scanner(System.in);
			String user = input.nextLine();
			int num;
			Client found;			
			
			// En fonction de la commande, on execute la méthode correspondante.
			switch(user.toLowerCase()) {
				
				case "verifier":
					
					System.out.println("Entrez le numéro du client:");
					num = Integer.parseInt(input.nextLine());
					found = menu.rechercherClient(num);
					
					if(found != null) {
						
						if(found.getIsSuspended()) {
							System.out.println("Membre suspendu");
							
						} else {
							System.out.println("Validé");
						}
					} else {
						System.out.println("Numéro invalide");
					}
					
					break;
					
				case "ajouter":
				
					System.out.println("Ajouter un client ou un service? (client/service)");
					user = input.nextLine();
					switch(user.toLowerCase()) {
						
						case "client":
							System.out.println("Entrez le nom:");
							String nom = input.nextLine();
							System.out.println("Entrez le prénom:");
							String prenom = input.nextLine();
							System.out.println("Entrez l'addresse du client:");
							String address = input.nextLine();
							System.out.println("Entrez la ville du client:");
							String town = input.nextLine();
							System.out.println("Entrez le code postal du client:");
							String postCode = input.nextLine();
							System.out.println("Entrez le courriel du client:");
							String courriel = input.nextLine();
							System.out.println("Le membre est-il un professionel? (oui/non)");
							String answer = input.nextLine();
							boolean isPro;
							switch(answer.toLowerCase()) {
								case "oui":
									isPro = true;
									break;
								case "non":
									isPro = false;
									break;
								default:
									System.out.println("Réponse invalide. On assume qu'il ne s'agit pas d'un professionel.");
									isPro = false;
									break;
							}
							System.out.println("Création en cours...");
							num = menu.ajouterClient(nom,prenom,isPro,address,town,postCode,courriel);
							System.out.println("Création terminée. Le numéro est: "+ num);
							break;
						
						case "service":
							System.out.println("Entrez le nom du service:");
							String name = input.nextLine();
							System.out.println("Entrez la date à laquel le service aura lieu (aaaa-mm-jj-heure-minute):");
							String dateService = input.nextLine();
							System.out.println("Entrez le coét du service (e.g: 5.5):");
							double frais = Double.parseDouble(input.nextLine());
							System.out.println("Entrez la capacité maximale:");
							int capacite = Integer.parseInt(input.nextLine());
							System.out.println("Entrez le numéro du professionel:");
							int numProfessionel = Integer.parseInt(input.nextLine());
							
							Client pro = menu.rechercherClient(numProfessionel);
							if(pro == null) {
								System.out.println("Numéro inconnu. Annulation de l'opération.");
								break;
							}
							
							if(!pro.getIsPro()) {
								System.out.println("Ce client n'est pas un professionel. Annulation de l'opération.");
								break;
							}
							
							System.out.println("Entrez un commentaire (optionel):");
							String commentaire = input.nextLine();
							num = menu.addService(name, dateService, frais, commentaire, capacite, numProfessionel);
							System.out.println("Création terminée. Le numéro est: "+ num);
							break;
						
						default:
							System.out.println("Réponse inconnue. Annulation.");
					}
					break;
				
				case "modifier":
					
					System.out.println("Modifier les informations d'un client ou d'un service? (client/service)");
					user = input.nextLine();
					switch(user.toLowerCase()) {
						
						case "client":
							
							System.out.println("Entrez le numéro du client:");
							num = Integer.parseInt(input.nextLine());
							found = menu.rechercherClient(num);
							
							if(found != null) { 
								System.out.println("Client trouvé!");
								System.out.println("Entrez le nouveau nom (laissez blanc pour ne pas changer):");
								String nom = input.nextLine();
								System.out.println("Entrez le nouveau prénom (laissez blanc pour ne pas changer):");
								String prenom = input.nextLine();
								System.out.println("Entrez la nouvelle adresse (laissez blanc pour ne pas changer):");
								String address = input.nextLine();
								System.out.println("Entrez la nouvelle ville (laissez blanc pour ne pas changer):");
								String town = input.nextLine();
								System.out.println("Entrez le nouveau code postale (laissez blanc pour ne pas changer):");
								String postCode = input.nextLine();
								System.out.println("Entrez le nouveau courriel (laissez blanc pour ne pas changer):");
								String courriel = input.nextLine();
								
								menu.maJourClient(found, nom, prenom, address, town, postCode, courriel);
							
							} else {
								System.out.println("Client introuvable...");

							}
							
							break;
						
						case "service":
							System.out.println("Entrez le numéro du service:");
							num = Integer.parseInt(input.nextLine());
							Service found2 = menu.services.getService(num);
							
							if(found2 != null) {
								System.out.println("Service trouvé! ("+ found2.getName() + ")");
								System.out.println("Entrez le nouveau nom du service (laissez blanc pour ne pas changer):");
								String name = input.nextLine();
								System.out.println("Entrez les nouveaux frais du service (laissez blanc pour ne pas changer):");
								String frais = input.nextLine();
								System.out.println("Entrez la nouvelle capacité maximale (laissez blanc pour ne pas changer):");
								String capacite = input.nextLine();
								System.out.println("Entrez la nouvelle date de service (aaaa-mm-jj-heure-minute ou laissez blanc pour ne pas changer):");
								String newLocalDate = input.nextLine();
								System.out.println("Entrez un nouveau commentaire (laissez blanc pour ne pas changer):");
								String newComment = input.nextLine();
								
								menu.maJourService(found2,name,frais,capacite,newLocalDate, newComment);
								
							} else {
								System.out.println("Service introuvable...");

							}
							
							break;
					}
					break;
				
				case "supprimer":
				
					System.out.println("Supprimer un client ou un service? (client/service)");
					user = input.nextLine();
					switch(user.toLowerCase()) {
						case "client":
							
							System.out.println("Entrez le numéro du client:");
							num = Integer.parseInt(input.nextLine());
							found = menu.rechercherClient(num);
							
							if(found != null) {
								System.out.println("Client trouvé!");
								System.out.println("Confirmer la suppression? (oui/non)");
								user = input.nextLine();
								
								switch(user.toLowerCase()) {
									case "oui":
										menu.supprimerClient(found);
										break;
									
									case "non":
										break;
										
									default:
										break;
								}
								
							} else {
								System.out.println("Client introuvable...");
							}
							break;
						
						case "service":
							
							System.out.println("Entrez le numéro du client:");
							num = Integer.parseInt(input.nextLine());
							Service found2 = menu.services.getService(num);
							if(found2 != null) {
								System.out.println("Confirmer la suppression? (oui/non)");
								user = input.nextLine();
								
								switch(user.toLowerCase()) {
									case "oui":
										services.deleteService(found2);
										break;
									
									case "non":
										break;
										
									default:
										break;
								}
							} else {
								System.out.println("Service introuvable...");
							}
							break;
					}
					break;
				
				case "trouver":
					System.out.println("Entrez le jour (jj): ");
					int jour = Integer.parseInt(input.nextLine());
					System.out.println("Entrez le mois (mm): ");
					int mois = Integer.parseInt(input.nextLine());
					System.out.println("Entrez l'année (aaaa): ");
					int ans = Integer.parseInt(input.nextLine());
					
					ArrayList<Service> toShow = menu.trouverService(jour,mois,ans);
					
					if(toShow.size() == 0) {
						System.out.println("Aucun service ce jour.");
						break;
						
					}
					
					for (Service service : toShow) {
						System.out.println("Service numéro "+service.getCodeService()+" à lieu le "+service.getDateService().toString());
						
					}
					
					break;
				
				case "inscrire":
					
					System.out.println("Entrez le numéro du client: ");
					int numClient = Integer.parseInt(input.nextLine());
					Client toIns = menu.rechercherClient(numClient);
					if(toIns == null) {
						System.out.println("Client introuvable");
						break;
					}
					System.out.println("Client trouvé!");
					System.out.println("Entrez le numéro du service: ");
					int numService = Integer.parseInt(input.nextLine());
					Service service = menu.rechercherService(numService);
					if(service == null) {
						System.out.println("Service introuvable");
						break;
					}
					
					service.addList(toIns);
					toIns.addListService(service);
					System.out.println("Inscription terminée!");
					
					break;
				
				case "confirmer":
					System.out.println("Entrez le numéro du client: ");
					numClient = Integer.parseInt(input.nextLine());
					toIns = menu.rechercherClient(numClient);
					if(toIns == null) {
						System.out.println("Client introuvable");
						break;
					}
					System.out.println("Client trouvé!");
					System.out.println("Entrez le numéro du service: ");
					numService = Integer.parseInt(input.nextLine());
					service = menu.rechercherService(numService);
					if(service == null) {
						System.out.println("Service introuvable");
						break;
					}
					
					service.addListConf(toIns);
					System.out.println("Présence confirmée!");
					
					break;
				
				case "consulter":
					
					System.out.println("Entrez le numéro du service: ");
					numService = Integer.parseInt(input.nextLine());
					service = menu.rechercherService(numService);
					if(service == null) {
						System.out.println("Service introuvable");
						break;
					}
					System.out.println("Service trouvé!");
					System.out.println("Nombre de personnes inscrites: " + service.getListeMembre().size());
					System.out.println("Nombre de personnes confirmées: " + service.getListMembreConf().size());
					
					break;
				
				case "rechercher":
					System.out.println("Chercher un client ou un service? (client/service)");
					user = input.nextLine();
					switch(user.toLowerCase()) {
						
						case "client":
							System.out.println("Entrez le numéro du client:");
							num = Integer.parseInt(input.nextLine());
							found = menu.rechercherClient(num);
							
							if(found != null) {
								System.out.println("Client trouvé!");
								System.out.println(found.getPrenom()+" "+found.getNom());
								System.out.println("Inscrit depuis: "+found.getDateInscription().toString());
								if(found.getIsPro()) {
									System.out.println("Le client est enregistré comme professionel");
								}
								
							} else {
								System.out.println("Client introuvable..."); 
							}
							break;
							
						
						case "service":
							System.out.println("Entrez le numéro du service:");
							num = Integer.parseInt(input.nextLine());
							Service found2 = menu.rechercherService(num);
							
							if(found2 != null) {
								System.out.println("Service trouvé!");
								System.out.println("Frais: $" + found2.getFrais());
								System.out.println("LocalDate: " + found2.getDateService().toString());
								System.out.println("Capacité maximale: " + found2.getCapacite());
								System.out.println("Nombre d'inscrits actuels: " + found2.getListeMembre().size());
								System.out.println("Nombre de clients ayant confirmé leur présence: " + found2.getListMembreConf().size());
								
								if(!(found2.getCommentaire().equals(""))) {
									System.out.println("Commentaire: " + found2.getCommentaire());
									
								}
								
							}
							
							break;
					}
					break;
				
				case "synthese":
					AutoProc.genererSynthese(menu.clients, menu.services);
					break;
				
				case "aide":
					System.out.println(helpMsg);
					break;
				
				case "proc_comptable": // Cette commande n'est pas référencée dans l'aide, et sert à déclencher procComptable() pour debug
					AutoProc.procComptable(menu.clients, menu.services);
					break;
				
				default:
					System.out.println("Commande inconnue. Entrez \"Aide\" pour afficher les commandes disponibles.");
					break;
			}
		}
	}
}