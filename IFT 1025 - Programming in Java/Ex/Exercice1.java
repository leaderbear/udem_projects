/* Tabta Abderrahim 20133680 code JAVA 2019-01-19

petit programme qui lit des mots sur
la ligne de commande jusqu'a ce que l'utilisateur entre "stop", qui
trie ces mots et qui retire les doublons entres.
*/

import java.util.Scanner;

public class Exercice1 {

	/**
	 * Cette fonction prend un tableau de Strings en paramètre et retourne un
	 * nouveau tableau contenant les mêmes éléments, avec une case disponible de
	 * plus.
	 */
	public static String[] agrandirTab(String[] tab) {

		//cree nouveau tableau pour des string txt avec une case en plus 
		String[] temp = new String[tab.length + 1]; 

		//copie ancien tableau dans nouveau tableau 
		for (int i = 0; i < tab.length; i++) {
			temp[i] = tab[i]; 
		}

		//retourne nouveau tab avec une case vide
		return temp;
	}

	/**
	 * Cette fonction lit des mots sur la ligne de commande et les retourne dans
	 * un tableau de Strings. Chaque "mot" est définit comme une ligne complète,
	 * du début de la ligne entrée jusqu'au prochain \n.
	 */
	public static String[] lireMots() {

		String[] tab = new String[1];//cree nouveau tableau vide 
		Scanner mot = new Scanner(System.in); //initier scanner
		String motTemp = mot.nextLine(); //variable tomproraire scanner->string
		tab[tab.length-1] = motTemp; //ajoute premier mot dans le tab en string


		while(!motTemp.equals("stop")){//boucle arret scan au mot "stop"
			motTemp = mot.nextLine();//scanne prochain mot /n et le string
			tab = agrandirTab(tab); //aggrandit le tableau de 1 
			tab[tab.length-1] = motTemp; //ajoute le "mot" dans le tableau
		}

		tab = rapetisser(tab); //vu que "stop" est tjrs dernier mot, l'enelver

		return tab; //retourne le tableau finale 
	}

	/**
	 * Cette fonction prend en paramètre un tableau de mots et retourne un
	 * nouveau tableau contenant ces mots triés en ordre croissant.
	 */
	public static String[] trier(String[] mots) {

		for (int i = 0; i < mots.length; i++){ 
			for (int j = i + 1; j < mots.length; j++){

				//compare chaque mot aux mots d'apres,si +grand : 

				if ( mots[i].compareTo(mots[j]) > 0){ 

					String tempI = mots[i]; //stock les deux mots compares
					String tempJ = mots[j];

					mots[i] = tempJ; //echanger les mots dans le tableau 
					mots[j] = tempI; 

					j = i + 1; //relancer comparaison 
				}
			}

		}

		return mots;
	}

	/**
	 * Tab N - 1 , enleve dernier element du tableau 
	 */
	public static String[] rapetisser(String[] mots) { //similaire a agrandirTab
		//cree nouveau tableau pour des string txt avec une case en moins
		String[] temp = new String[mots.length -1];
		//copie ancien tableau dans nouveau tableau 
		for (int i = 0; i < mots.length-1; i++) {
			temp[i] = mots[i]; 
		}
		return temp;
	}

	/**
	 * Cette fonction prend en paramètre un tableau de mots trié et retourne un
	 * nouveau tableau où chaque mot est unique (tous les doublons sont
	 * retirés).
	 */
	public static String[] retirerDoublons(String[] mots) {    

		for (int i = 0; i < mots.length; i++){//Parcourir element comme trier
			for (int j = i+1; j < mots.length; j++){
				
				if ( mots[i].equals(mots[j])){ //si elements sont egaux

					//mettre dernier element du tab a la place du doublon
					mots[j]=mots[mots.length-1]; 
					mots = rapetisser(mots); //supprimer dernier element
					j--; //continue a parcourir avant element supp

				}                 
			}      
		}
		return mots;
	}


	/**
	 * Fonction principale du programme (utilisée pour tester le code).
	 */
	public static void main(String[] args) {

		String[] mainTab = lireMots(); //scan mot les mets ds tab
		mainTab = retirerDoublons(mainTab); //retire les doublons du tab
		mainTab = trier(mainTab); //trier tab en ordre croissant

		for (int i=0; i< mainTab.length; i++) {//print chq string du tab
			System.out.println(mainTab[i]);
		}

	}
}
