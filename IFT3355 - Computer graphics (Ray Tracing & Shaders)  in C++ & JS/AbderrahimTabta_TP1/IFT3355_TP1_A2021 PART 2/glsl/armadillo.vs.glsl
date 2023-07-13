// Tabta Abderrahim 20133680

// Create shared variable for the vertex and fragment shaders
out vec3 interpolatedNormal;
uniform int time;
uniform sampler2D fft;
uniform vec3 controller0;
uniform vec3 controller1;
uniform vec3 controller2;


////////////////////////////////////////////////////////////////////////////////////////////////////
// CE CODE N'EST PAS LE MIEN ! 																	 ///
// SOURCE DU CODE EMPRUNTÉ : https://www.youtube.com/watch?v=rpBd-6n5q5w       					 ///
float noise1d(float value){																	     ///
	return cos(value + cos(value * 90.0) * 100.0)  *0.5 +0.5 ;					                 ///
}																							     ///
/////////////////////////////////////////////////////////////////////////////////////////////////////

void main() {
	// Set shared variable to vertex normal
	interpolatedNormal = normal;

	// Get components of sounds from the FFT texture
	float fft_bass = texture(fft, vec2(0.1, 0.0)).x;
	float fft_middle = texture(fft, vec2(0.25, 0.0)).x;
	float fft_treble = texture(fft, vec2(0.5, 0.0)).x;

	// "La hauteur des contrôleurs doit affecter l’intensité des perturbations sur le personnage"
	float intense1 = controller0.y + 1.0;

    // "La première perturbation doit réagir aux fréquences basses et déformer la géométrie dans la direction de la nor"
	// Remarque : Ici , le sin crée un effet intéressant 1/2 du temps : La déformation se fait dans le sens opposée.
	// Le reste de la formule a été calculé avec des essais erreurs pour choisir un effet intéressant.


	// Ici pour la partie, j'ai remplacer la fonction sin par la fonction tan pour donner un effet plus intéressant.
	// En effet, à des moments au hasard le personnage surgrandit de taille. De plus , la multiplication par le temps
	// crée une effet de vibration très intéressant. Le modolu 5 est utile parce que le temps va à l'infini.
	// Si on DEZOOM assez, cela donne un effet intressant (D'aura d'énergie autour du modèle)
	vec3 pertu1 = ((tan(fft_bass*15.0))*intense1)/40.0 * float(time%5) * interpolatedNormal;


	// Explication de la formule : D'abord, on veut simplement avoir des fois un signe négatif et parfois un signe
	// positif, d'où le sin au début. Ensuite, on utilise la fonction noise emprunté sur internet (source citée en haut)
	// Enfin , on utilise le controller du milieu pour controler l'intensité du bruit.
    float pertu = sin(float(time%5)) * noise1d(float(time)) * (controller1.y - 0.5) / 10.0;


	// HINT: Work with tvChannel, fft and normal here to perturb the armadillo's geometry.
	// You can use time as a seed for pseudo-random number generators.
	// By default, we simply multiply each vertex by the model-view matrix and the projection matrix to get final vertex position
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position + pertu + pertu1, 1.0);
}