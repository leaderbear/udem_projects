// Tabta Abderrahim 20133680

// Create shared variable for the vertex and fragment shaders
out vec3 interpolatedNormal;
uniform int time;
uniform sampler2D fft;
uniform vec3 controller0;

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
	vec3 pertu1 = ((sin(fft_bass*15.0))*intense1)/40.0 * interpolatedNormal;

	// HINT: Work with tvChannel, fft and normal here to perturb the armadillo's geometry.
	// You can use time as a seed for pseudo-random number generators.
	// By default, we simply multiply each vertex by the model-view matrix and the projection matrix to get final vertex position
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position + pertu1, 1.0);
}