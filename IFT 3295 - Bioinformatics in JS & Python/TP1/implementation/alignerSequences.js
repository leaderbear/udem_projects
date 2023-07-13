function alignerSequences(seq1, seq2) {

	// On ajoute un "_" au début des séquences pour faciliter la gestion de l'index 0
	var xi = "_" + seq1;
	var xj = "_" + seq2;

	var longueur_xi = xi.length;
	var longueur_xj = xj.length;

	// crée un tableau de tableau [[_,_,_],[_,_,_],[_,_,_]] avec la  taille de xi xj
	var tableau_scores = Array.from(Array(longueur_xi), () => new Array(longueur_xj).fill("_"));
	var tableau_pointeurs = Array.from(Array(longueur_xi), () => new Array(longueur_xj).fill("_"));

	// On complète le tableau ligne par ligne
	for(var i=0; i<longueur_xi; i++) {
		for(var j=0; j<longueur_xj; j++) {

			// conditions initiales de la première ligne
			if(i==0) {
				tableau_scores[0][j] = -8*j;
				tableau_pointeurs[0][j] = "←";
			}

			// conditions initiales de la première colonne
			if(j==0) {
				tableau_scores[i][0] = 0;
				tableau_pointeurs[i][0] = "↑";
			}

			if(i>0 && j>0) {
				var matchOuMismatch = 4;
				if(xi.charAt(i) != xj.charAt(j)) { matchOuMismatch = -4; }

				// à la case i,j on choisit le max entre trois options
				tableau_scores[i][j] = Math.max(
					tableau_scores[i-1][j]-8, 
					tableau_scores[i][j-1]-8, 
					tableau_scores[i-1][j-1] + matchOuMismatch
				)

				// à la position i,j du tableau de pointeurs, on met le symbole de pointeur
				// adéquat par rapport à la provenance de la valeur du score
				if(tableau_scores[i][j] == tableau_scores[i-1][j]-8) {
					tableau_pointeurs[i][j] = "↑";
				}
				if(tableau_scores[i][j] == tableau_scores[i][j-1]-8) {
					tableau_pointeurs[i][j] = "←";
				}
				if(tableau_scores[i][j] == tableau_scores[i-1][j-1] + matchOuMismatch) {
					tableau_pointeurs[i][j] = "↖";
				}
			}
		}
	}

	// à la dernière ligne, prendre tout simplement le maximum
	// et sa position dans le tableau
	var scoreMax = Math.max(...tableau_scores[tableau_scores.length-1]);
	var scoreMaxPosition = [tableau_scores.length-1, tableau_scores[tableau_scores.length-1].indexOf(scoreMax)];


	// pour le backtracking, on part de la case du score max
	var i_retour = scoreMaxPosition[0];
	var j_retour = scoreMaxPosition[1];

	// contient le chevauchement
	var chevauchements = ["", ""];// par exemple ["TCG","TCG"] pour deux séquences "AATCG" et "TCGAA"

	// tant qu'on n'est pas encore à la première colonne
	// on parcourt le tableau de pointeurs et en fonction du pointeur
	// on ajoute aligne les différents caractères des séquences
	while(j_retour != 0) {
		var pointeur_actuel = tableau_pointeurs[i_retour][j_retour];

		if(pointeur_actuel == "↑") {
			chevauchements[0] = xi.charAt(i_retour) + chevauchements[0];
			chevauchements[1] = "-" + chevauchements[1];

			i_retour--;
		}
		if(pointeur_actuel == "←") {
			chevauchements[0] = "-" + chevauchements[0];
			chevauchements[1] = xj.charAt(j_retour) + chevauchements[1];

			j_retour--;
		}
		if(pointeur_actuel == "↖") {
			chevauchements[0] = xi.charAt(i_retour) + chevauchements[0];
			chevauchements[1] = xj.charAt(j_retour) + chevauchements[1];

			i_retour--;
			j_retour--;
		}
	}

	var longueur_chevauchement = chevauchements[0].length;


	// reconstituer les séquences entier avec les gaps
	//  avant et après de la zone de chevauchement
	// par exemple : "AATCG" et "TCGAA", la zone de chevauchement est "TCG",
	// alors il faut récupérer le "AA" dans "AATCG" et "AA" dans "TCGAA"
	var prefixe_xi = seq1.substring(0, i_retour);
	var suffixe_xj = seq2.substring(scoreMaxPosition[1]);

	chevauchements[0] = prefixe_xi + chevauchements[0] + "-".repeat(suffixe_xj.length); //on met "AA" + "TCG " + "-" (x longueur du suffixe de xj)
	chevauchements[1] = "-".repeat(prefixe_xi.length) + chevauchements[1] + suffixe_xj; // même principe

	// on retourne un objet avec tous résultats qu'on a eu
	return {
		"sequence1" : seq1,
		"sequence2" : seq2,
		"tableau_scores" : tableau_scores,
		"tableau_pointeurs" : tableau_pointeurs,
		"score_alignement_optimal" : scoreMax,
		"position_score_alignement_optimal" : scoreMaxPosition,
		"alignement_optimal" : chevauchements.join("\n"),
		"longueur_chevauchement" : longueur_chevauchement
	}
}