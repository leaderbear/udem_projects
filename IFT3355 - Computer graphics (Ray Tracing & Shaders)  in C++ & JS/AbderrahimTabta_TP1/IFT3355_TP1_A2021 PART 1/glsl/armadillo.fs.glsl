// Tabta Abderrahim 20133680

// Create shared variable. The value is given as the interpolation between normals computed in the vertex shader
in vec3 interpolatedNormal;
uniform sampler2D fft;

/* HINT: YOU WILL NEED A DIFFERENT SHARED VARIABLE TO COLOR ACCORDING TO POSITION OF REMOTE */
uniform vec3 controller1;
uniform vec3 controller2;

void main() {

  // "Deuxième et la troisième doit réagir aux fréquences moyennes et hautes"
  float fft_middle = texture(fft, vec2(0.25, 0.0)).x;
  float fft_treble = texture(fft, vec2(0.5, 0.0)).x;


  // "La deuxième et la troisième [perturbations] .. changer les cours du personnage"
  // "La hauteur des contrôleurs doit affecter l’intensité des perturbations sur le personnage"
  // ps: Ici, j'ai add 0.3, car quand j'ai fais d), lorsque le controller est à la position initale,y=0 -> pertu=0 aussi
  float pertu1 = (controller1.y+0.4) * fft_middle;
  float pertu2 = (controller2.y+0.4) * fft_treble;      // se remarque pas au début de la musique , attendre un peu


  // Set final rendered color according to the surface normal
  // Remarque : Ici, avec le interpolatedNormal, je pouvais modifier que le teint de la couleur et non pas la couleurs
  // elle-même. La solution que j'ai trouvée est de remplacer ce vecteur par un simple vecteur de taille 3.
  gl_FragColor = vec4(normalize(vec3(1.0, 0.0 + pertu1, 0.0 + pertu2)), 1.0);
}