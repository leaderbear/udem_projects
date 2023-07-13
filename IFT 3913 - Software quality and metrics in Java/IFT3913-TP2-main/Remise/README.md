# IFT3913-TP2

Tabta Abderrahim 
Berrachedi Wassim


IFT3913 QUALITÉ DE LOGICIEL ET MÉTRIQUES – AUTOMNE 2022 – TRAVAIL PRATIQUE 2
https://github.com/leaderbear/IFT3913-TP2 


Résumé : 

-> Notre rapport se trouve dans le fichier Rapport final.docx ou Rapport final.pdf 

-> Notre code fonctionne en deux étapes: 
	1. On produit toutes les données de métriques nécessaires avec soit notre outil ou des outils externes.
	2. On analyse ces données automatiquement avec notre analyseur qui répond aux 4 questions du tp. 

-> Chaque dossier est expliqué dans la suite, mais pour résumer : 
	1. "\Executables" contient notre code prêt à être executé de manière autonome. 
	2. "\Notre implementation de deux metriques" contient notre implémentation de la densité de commentaires et la dernière mise-à-jour des fichiers. 
	3. "\Résultats des métriques" contient les données brutes des métriques générés par soit notre outil (2.) ou des outils externes (ck et Junit4) 
	4. "\Analyseur automatique des donnees des metriques" contient le code qui analyse automatiquement toutes les données des métriques pour répondre automatiquement aux 4 questions.



Executables: 

	Afin de simplifier l'éxecution de notre code, nous avons mis à disposition des fichiers exécutables autonomes dans le dossier "\Executables"

	-> "Analyseur Automatique.bat" va compiler "autoAna.jar" qui est le produit de "..\Analyseur automatique des donnees des metriques". 
	Il est obligatoire que le dossier "src" déjà présent soit là pour le bon fonctionnement de autoAna.jar. Cela va produire un fichier 
	result.txt qui contient les mêmes informations que nous avons utilisé pour répondre à la question 3 du tp. 

	-> "Analyseur Manuel.bat" va compiler "ana.jar" qui est le produit de "..\Analyseur automatique des donnees des metriques". 
	Cela fait la même chose que Analyseur Automatique, mais demande de spécifier manuellement le chemin vers toutes les métriques. 
	Il est obligatoire d'utiliser les mêmes données de métriques que nous pour le bon fonctionnement. Des exemples ont été mit à disposition. 
	Cela va produire un fichier result.txt qui contient des informations similaires à celles que nous avons utiliser pour répondre à la question 3 du tp. 

	-> "Notre outil.bat" va compiler "ourMet.jar" qui est le produit de "..\Notre implementation de deux metriques". 
	Ce programme demande de spécifier le chemin vers un projet comme jfree. Ensuite, deux fichiers csv sont générés 
	contenant les métriques : dernière mise-à-jour des fichiers et densité de commentaires. 

	PS : nous avons utiliser la version JDK19 pour compiler et exécuter le code (https://download.oracle.com/java/19/latest/jdk-19_windows-x64_bin.exe). 

 

Code et commentaires : 

	Afin d'éxecuter le code ou lire nos commentaires, vous pouvez aussi utiliser un IDE. Nous avons utilisé IntelliJ IDEA, donc nous recommandons le même. 

   -> Notre implementation de deux metriques (Notre outil) : 
		Ce code est fortement inspiré de celui de notre tp1 https://github.com/leaderbear/IFT3913-TP1 avec quelques méthodes en plus 
		pour calculer nos nouvelles métriques. Le fonctionnement est le même: 
		
			1. La fonction jls prend en entrée un chemin du dossier désiré. D'abord, récursivement, cette fonction parcourt chaque sous-dossier. 
			   Pour chaque dossier (ou sous-dossier), la fonction lit le nom et l'extension de chaque fichier. Si le fichier est un fichier .java, 
			   alors ce fichier est ajouté à une liste. Chaque fichier est ajouté à la liste en tant que objet "JavaFile.java". Pour ajouter un fichier/objet 
			   à la liste, on ajoute simplement sa location, son nom sans l'extension et enfin son package. Pour déterminer son package, la fonction jls ouvre le 
			   fichier et cherche le mot "package". 
			   
			2. Pour chaque fichier Java, on obtient sa date de création. On calcule sa densité de commentaires avec le ratio nombre de lignes totales  /  nombre de
			   lignes de commentaires. Enfin, nous écrivons les données obtenues dans des fichiers csv.  
			

	-> Analyseur automatique des donnees des metriques 
		Ce code prend en entrées toutes les données des métriques (format: csv, xml, html) générés par soit notre outil (juste en haut) ou par des outils externes (ck et Junit4)
		que nous allons prendre le temps d'expliquer au prochain point. Le fonctionnement est le suivant : 
		
		1. On lit le fichier csv, xml, html 
		2. S'il s'agit du fichier xml ou html, on cherche un motif (pattern ragex) pour trouver une certains valeur (par exemple temps de test généré par Junit4) 
		3. S'il s'agit d'un fichier csv, on crée un dictionnaire où on map le header (première ligne) comme étant les clés et les éléments dans les lignes comme étant des valeurs. 
		4. Une fois les dictionnaires et toutes les informations voulues obtenues, on calcule les moyennes et on vérifie si les seuils sont respectés. 
		5. Pour chaque question on s'assure que les deux seuils sont respectés, sinon la réponse sera Non. 
		6. On produit un fichier texte qui résume le tout. 



Outils externes : 

	Commme expliqué avant, notre analyseur a besoin de certains données pour fonctionner. Ces données sont dans le dossier "\Résultats des métriques". 
	
	1. Nous avons utiliser l'outil externe ck : 
	
		Source : https://github.com/mauricioaniche/ck 
		Nous avons procédé comme suit : 
			1. Cloner le répositoire Git sur notre ordinateur local. 
			2. Ouvrir le projet avec l'IDE  : IntelliJ IDEA. 
			3. Nous avons, ajouter cette ligne comme argument donné au fichier Runner.java : 
			   "C:\Users\leade\Desktop\jfreechart-master\src\main True 0 True C:\Users\leade\Desktop\output" 
			4. les fichiers produits ont été déposé "\Résultats des métriques\ck"
	
		Nous avons obtenu cbo, lcom et le nombre de méthodes par classe avec cet outil. 
		
		
	2. Nous avons utiliser l'outil JUnit4 inclus dans IntelliJ: 

		Nous avons procédé comme suit : 
		1. Cloner le répositoire git https://github.com/jfree/jfreechart sur notre ordinateur local. 
		2. Ouvrir le projet avec l'IDE  : IntelliJ IDEA. 
		3. Nous avons fais clique droit sur le dossier "..\jfreechart-master\src\test" à partir du menu à gauche. 
		4. Nous avons cliquer sur "Run All Tests" et "Run All Tests with coverage". 
		5. Nous avons exporter les résultats dans "\Résultats des métriques\JUnit4" en format html et xml
		
		Nous avons obtenu nombre de méthodes testées et temps de test avec cet outil. 