# IFT3913-TP1

Tabta Abderrahim 
Berrachedi Wassim


IFT3913 QUALITÉ DE LOGICIEL ET MÉTRIQUES – AUTOMNE 2022 – TRAVAIL PRATIQUE 1
https://github.com/leaderbear/IFT3913-TP1.git 

Fichier exécutable : 

Afin d'exécuter le fichier exécutable autonome, il suffit d'ouvrir le fichier "run codesource program.bat" se trouvant dans 
le dossier principal. Il est important que le fichier "codesource.jar" soit dans le même dossier comme actuellement. De plus, 
nous avons utiliser la version JDK19 pour compiler et exécuter le code (https://download.oracle.com/java/19/latest/jdk-19_windows-x64_bin.exe). 
Lorsqu'on ouvre, une console est supposée apparaître avec un menu. Il faut suivre les instructions du menu afin de pouvoir exécuter  jls, nvloc, isec et egon.  



Code et commentaires : 

De plus, il est aussi possible d'exécuter  le projet en passant avec un compilateur. Nous avons utilisé Intellij pour créer notre projet 
qui se trouve dans le dossier "code source". Donc, avec Intellij ou même éclipse, on peut importer le projet et le lancer. D'ailleurs, le 
code source avec tous les commentaires se trouve dans : .\IFT3913-TP1\code source\src. D'ailleurs, dans ce même dossier, on peut trouver 
des fichiers CSV. Ces fichiers sont les résultats de nos tests qui sont basé sur l'énoncé. De plus, nous avons utiliser JavaDoc pour créer
toute la documentation disponible dans le dossier data source. Un racourcis "Documentation JavaDoc.html" a été mit à disposition.



PARTIE 0 

La fonction jls prend en entrée un chemin du dossier désiré. D'abord, récursivement, cette fonction parcourt chaque sous-dossier. 
Pour chaque dossier (ou sous-dossier), la fonction lit le nom et l'extension de chaque fichier. Si le fichier est un fichier .java, 
alors ce fichier est ajouté à une liste. Chaque fichier est ajouté à la liste en tant que objet "JavaFile.java". Pour ajouter un fichier/objet 
à la liste, on ajoute simplement sa location, son nom sans l'extension et enfin son package. Pour déterminer son package, la fonction jls ouvre le 
fichier et cherche le mot "package". 


PARTIE 1 

La fonction NVLOC prend en entrée un chemin du fichier java désiré et calcule la métrique de la taille de 
celui-ci. Il exécute cette fonction à l'aide d'un Scanner qui vérifie les lignes non-vides du fichier et
retourne la taille (le nombre de lignes) du document.


PARTIE 2 

Grace à une autre fonction qui est capable d'ouvrir n'importe quel fichier csv généré par jls, en lisant ligne par ligne, 
et en séparant les mots dans chaque ligne avec les virgules. Où Chaque ligne est un fichier et chaque mot est une information sur le 
fichier. En utilisant les informations de chaque fichier, on est capable de construire un objet de la classe JavaFile.java. 
Enfin, une fois tous les fichiers ajoutés, on les ouvre un par un pour calculer leur couplage avec Icsec. Pour se faire, il suffit de compter
toutes les mentions des autres classes, qui sont dans le même dossier, à l'intérieur du code de chaque JavaFile. Lorsqu'on trouve une
mention, on l'ajoute à une liste (couplage) dans le fichier JavaFile. De plus, si le nom d'un JavaFile est mentionné ailleurs, on 
ajoutera aussi à cette même liste plus tard le nom de la classe où JavaFile a été mentionné. Ainsi, nous avons tous les couplages 
pour chaque fichier. Enfin, nous écrivons tous ces JavaFiles dans un fichier csv en suivant la même méthode qu'à l'ouverture, mais à l'envers. 


PARTIE 3 

La fonction egon prend en entrée un chemin du dossier désiré et une valeur (entre 1 et 100) représentant 
le seuil en pourcentage. La fonction appeLlera les 3 autres méthodes du programme (jls, Nvloc et Icsec). Par la suite,
le programme trie la liste des classes par Nvloc et Icsec séparément  et à l'aide du seuil fourni précédemment, 
deux sous-listes des classes suspectes seront fournis. Finalement, si un fichier se retrouve dans les deux
listes, celui-ci sera détecter comme possible classes divines et sera mis dans une liste finale 
qui sera la liste des classes suspectes d'être divines. Le programme affichera sa liste pour l'utilisateur.
 
 
PARTIE 4 

Pour ce qui est de la partie 4, nous avons simplement utiliser tous ce que nous avions utilisé avant. 
En effet, d'abord, on utilise jls sur le dossier. Ensuite, on applique isec et nvloc sur le fichier csv 
généré par jls. Enfin, on utilise egon sur ce fichier csv pour pouvoir trouver les classes divines. 
Voici à quoi ressemble le code utilisé dans une classe Main.java afin d'avoir nos résultats: 

	JavaFiles javaFiles = new JavaFiles(0);
	javaFiles.jls("C:\\Users\\leade\\Desktop\\IFT3913-TP1\\jfreechart-master\\src\\main\\java");
	javaFiles.nvloc();
	javaFiles.isec();
	javaFiles.writeDataToCsv("C:\\Users\\leade\\Desktop\\part4.csv");
	javaFiles.egon(0.01);
	javaFiles.egon(0.05);
	javaFiles.egon(0.1);
	
Ici, JavaFiles est une classe où on retrouve une liste d'objet JavaFile. La classe JavaFile est 
une classe qui a comme attribut la location, package, et le nom ainsi que d'autres attributs et méthodes
qui servent à mieux gérer les fichiers de type Java selon notre contexte.