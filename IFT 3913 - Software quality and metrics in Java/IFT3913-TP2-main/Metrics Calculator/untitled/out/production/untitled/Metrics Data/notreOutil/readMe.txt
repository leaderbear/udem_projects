Tous les données ici ont été généré avec notre outil qui est l'extension de notre code du tp1. 
Souce : voir le dossier suivant : 


Nous avons procédé comme suit : 

1. Lors du TP1 , nous étions déjà capable d'obtenir récursivement tous les fichiers java dans un dossier et ses sous-dossiers. 
2. Nous avons , pour chaque fichier java trouvé, calculer le nombre de lignes totales et le nombre de commentaires. 
3. Nous générons un fichier csv avec le chemin du fichier, son nom et le ratio (lignes totales / nombre de commentaires). 




Résumé du code utilisé (voir code source pour plus de détails en commentaire) : 


	O : Main: 

		JavaFiles javaFiles = new JavaFiles(0);
		javaFiles.jls("..\\jfreechart-master\\src\\main");
		javaFiles.commentsDensityProject();
		javaFiles.writeDataToCsv("..\commentsDensity.csv", true); 

	
	
	O : JavaFiles: 

		a : private ArrayList<JavaFile> list; 
		f : jls() : ajouter tous les fichiers java du dossier (et sous-dossiers) dans list. 
		f : commentsDensityProject() : appel la fonction commentDensity() sur chaque objet JavaFile dans list. 
	
	O : JavaFile : 
		a : private String location;	
		f : commentDensity() : Ouvrir chaque fichier -> le lire avec scanner -> Pour chaque ligne -> Si vide rien faire | sinon #lignes++ . Si isComment(ligne) == true -> #commentaires++. 	
		f : isComment(String) : Fonction qui analyse simplement les premières lettres ( '/'|'*' ) pour savoir si la ligne est un commentaire. 