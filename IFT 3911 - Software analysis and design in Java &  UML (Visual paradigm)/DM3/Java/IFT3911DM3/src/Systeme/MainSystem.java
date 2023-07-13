package Systeme;
import java.util.Locale;
import java.util.Scanner;  // Import the Scanner class


public class MainSystem {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO - implement MainSystem.main
		Scanner inputs = new Scanner(System.in);


		boolean isAdmin;
		int choice;

		System.out.println("Select menu:\n 0: user \n 1: admin");

		choice = inputs.nextInt();
		while (choice != 0  && choice != 1  ) {
			System.out.println("Try again , 0 or 1 only");
			choice = inputs.nextInt();

		}

		if (choice == 0 ) {
			System.out.println("User menu selected, type \"99\" to see commands or \"0\" to quit");
			Menu menuUser = new menuUtilisateur();
			//isAdmin = false;
		}

		else{
			System.out.println("Admin menu selected, type \"99\" to see commands or \"0\" to quit");
			Menu menuAdmin = new menuAdmnistrateur();
			//isAdmin = true;
			}


		choice = inputs.nextInt();


		/*if (isAdmin == false) {
			while (choice != 0) {
				if (choice !=99 ) {
					System.out.println("Command list: \n 1: \n2: \n3:");
				}
				System.out.println("Waiting for your command...");
				choice = inputs.nextInt();
			}
		}

		/*else{
			while (choice != 0 ) {
				if (choice != 99 ) {
					System.out.println("Command list: \n 1: createBatiment 2: deleteBatiment 3: EditBatiment  " +
							"\n4: createCompagnie 5: EditCompagnie 6: DeleteCompagnie 7: editCompagnie" +
							"\n8: createSection 9: editSection 10: DeleteSection" +
							"\n11: createVehicule 12:editVehicule 13: deleteVehicule ");
				}

				System.out.println("Waiting for your command...");
				choice = inputs.nextInt();

				/*switch(choice){
					case 1:

						/*System.out.println("entrez type du batiment");
						String type = inputs.nextLine();
						System.out.println("entrez lieu du batiment");
						String lieu = inputs.nextLine();
						System.out.println("entrez nom du batiment");*/




				//}
			//}
		//}

		System.out.println("Closing app");


	}
}