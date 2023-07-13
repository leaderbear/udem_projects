import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {

		String options = null;
		Scanner scan = new Scanner(System.in);


		System.out.println("Bienvenue dans notre outil :");
		System.out.println("Cet outil calcule l'âge d'un projet ainsi que sa densité en commentaire.\n");
		System.out.println("Merci d'écrire dans la ligne de commande le chemin du dossier que vous shouaitez analyser: Exemple:");
		System.out.println("C:\\Users\\leade\\Desktop\\jfreechart-master\\src\\main");
		options = scan.nextLine();
		System.out.println("Vous avez choisi l'option:" + options +" , Analysons ce dossier:");


		JavaFiles javaFiles = new JavaFiles(0);
		javaFiles.jls(options);
		javaFiles.lastCommit();
		javaFiles.writeDataToCsv("lastCommit.csv", "location,package,name,creationDate\n", true);

		javaFiles = new JavaFiles(0);
		javaFiles.jls(options);
		javaFiles.commentsDensityProject();
		javaFiles.writeDataToCsv("cd.csv", "location,package,name,nvloc,cloc,cd\n", true );
	}
}
