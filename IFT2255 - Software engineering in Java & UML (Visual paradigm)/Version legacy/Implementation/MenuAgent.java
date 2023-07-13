import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class MenuAgent {
	
	private static Clients clients;
	private static Services services;
	
	public MenuAgent() {
		
		this.clients = new Clients();
		this.services = new Services();
		
		// Données hardcodées pour permettre les tests
		Date tmp = new Date(7,11,2020,12.05);
		Date tmp2 = new Date(10,11,2020,23.59);
		this.clients.protoAddClient(123456789,"the fox","Tails",true,tmp,false);
		this.clients.protoAddClient(987654321,"the hedgehog","Sonic",false,tmp,false);
		this.clients.protoAddClient(101010101,"Robotnik","Dr.Ivo",true,tmp,true);
		this.clients.protoAddClient(010101010,"Sonic","Metal",false,tmp,true);
		
		this.services.protoAddService(1234567, tmp2, tmp, 99.9, "Cours de deltaplane", 5);
		this.clients.addServiceClient(this.services.getService(1234567),this.clients.getClient(123456789));

	}
	
	
	// Méthode appelée pour ajouter un client 
	public int ajouterClient(String nom, String prenom, boolean isPro, String dateToSplit) {
		
		// Créer l'objet date
		String[] tmp = dateToSplit.split("-");
		String heureStr = (tmp[3]+"."+tmp[4]);
		double heure = Double.parseDouble(heureStr);
		Date dateInscription = new Date(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),heure);
		
		int num = this.clients.addClient(nom, prenom, isPro, dateInscription,false);
		
		// Here be some code to add that client to the save file
		return num;
		
	}
	
	public int addService(String dateToSplit, String dateToSplit2, Double frais, String commentaire, int capacite) {
		
		String[] tmp = dateToSplit.split("-");
		String heureStr = (tmp[3]+"."+tmp[4]);
		double heure = Double.parseDouble(heureStr);
		Date dateService = new Date(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),heure);
		
		tmp = dateToSplit2.split("-");
		heureStr = (tmp[3]+"."+tmp[4]);
		heure = Double.parseDouble(heureStr);
		Date dateActuelle = new Date(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),heure);
		
		int num = this.services.addService(dateService, dateActuelle, frais, commentaire, capacite);
		
		// Here be some code to add that service to the save file
		return num;
	}
	
	public ArrayList<Service> trouverService(int jour, int mois, int ans) {
		
		ArrayList<Service> toReturn = services.trouverService(jour,mois,ans);
		return toReturn;
	}
	
	public Client rechercherClient(int num) {
		
		return this.clients.getClient(num);
	}
	
	public Service rechercherService(int num) {
		
		return this.services.getService(num);
	}
	
	public void maJourClient(Client toChange, String nom, String prenom, boolean isPro) {
		
		this.clients.updateClient(toChange,nom,prenom,isPro);
	}
	
	public void maJourService(Service toChange, String newFrais, String newCap, String newDate, String newComment) {
		
		this.services.updateService(toChange,newFrais,newCap,newDate,newComment);
	}
	
	public void supprimerClient(Client toDelete) {
		
		this.clients.deleteClient(toDelete);
		
	}
	
	public static void main(String args[]) {
		
		String helpMsg = "Commandes disponibles:\nVerifier: Vérifier la validité d'un numéro.\nAjouter: Ajouter un nouveau client ou une séance.\nModifier: Modifier les informations d'un client ou d'un service.\nSupprimer: Supprimer un client ou un service.\nTrouver: Trouver les services à une date présise.\nInscrire: Inscrire un client à une séance.\nConfirmer: Confirmer sa présence à une séance.\nConsulter: Consulter le nomre de personnes inscrites à une séance.\nRechercher: Trouver un client ou un service à partir de son numéro.\nSynthese: Génère la synthèse.\nAide: Affiche ce message.";
		System.out.println(helpMsg);
		MenuAgent menu = new MenuAgent();
		
		while(true) {
			
			System.out.println("\nCommande:");
			Scanner input = new Scanner(System.in);
			String user = input.nextLine();
			int num;
			Client found;
			
			// TODO: Ajouter un check de la date & heure actuelle pour appeler AutoProc.procComptable le vendredi à minuit
			
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
							System.out.println("Entrez la date (jj-mm-aaaa-heure-minute):");
							String date = input.nextLine();
							System.out.println("Création en cours...");
							num = menu.ajouterClient(nom,prenom,isPro,date);
							System.out.println("Création terminée. Le numéro est:"+ num);
							break;
						
						case "service":
							System.out.println("Entrez la date à laquel le service aura lieu (jj-mm-aaaa-heure-minute):");
							String dateService = input.nextLine();
							System.out.println("Entrez la date actuelle (jj-mm-aaaa-heure-minute):");
							date = input.nextLine();
							System.out.println("Entrez le coût du service (e.g: 5.5):");
							double frais = Double.parseDouble(input.nextLine());
							System.out.println("Entrez la capacité maximale:");
							int capacite = Integer.parseInt(input.nextLine());
							System.out.println("Entrez un commentaire (optionel):");
							String commentaire = input.nextLine();
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
								System.out.println("Le client est-il un professionel. (oui/non ou laissez blanc pour ne pas changer):");
								String answer = input.nextLine();
							
								boolean isPro;	
								switch(answer.toLowerCase()) {
									case "oui":
										isPro = true;
										break;
									case "non":
										isPro = false;
										break;
									case "":
										isPro = found.getIsPro();
										break;
									default:
										System.out.println("Réponse invalide. La valeur ne sera pas changée");
										isPro = found.getIsPro();
										break;
								}
								
								menu.maJourClient(found, nom, prenom, isPro);
							
							} else {
								System.out.println("Client introuvable...");

							}
							
							break;
						
						case "service":
							System.out.println("Entrez le numéro du service:");
							num = Integer.parseInt(input.nextLine());
							Service found2 = menu.services.getService(num);
							
							if(found2 != null) {
								System.out.println("Service trouvé!");
								System.out.println("Entrez les nouveaux frais du service (laissez blanc pour ne pas changer):");
								String frais = input.nextLine();
								System.out.println("Entrez la nouvelle capacité maximale (laissez blanc pour ne pas changer):");
								String capacite = input.nextLine();
								System.out.println("Entrez la nouvelle date de service (jj-mm-aaaa-heure-minute ou laissez blanc pour ne pas changer):");
								String newDate = input.nextLine();
								System.out.println("Entrez un nouveau commentaire (laissez blanc pour ne pas changer):");
								String newComment = input.nextLine();
								
								menu.maJourService(found2,frais,capacite,newDate, newComment);
								
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
					System.out.println("Présence comfirmée!");
					
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
					System.out.println("Nomre de personnes inscrites: " + service.getListeMembre().size());
					
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
								System.out.println("Date: " + found2.getDateService().toString());
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
				
				default:
					System.out.println("Commande inconnue. Entrez \"Aide\" pour afficher les commandes disponibles.");
					break;
			}
		}
	}
}