// Tabta Abderrahim 20133680

uniform int tvChannel;
uniform vec3 remotePosition;

void main() {

	// Ici, le vecteur contrôle la couleur (rgb). Il suffit alors de lui ajouter un autre vecteur pour modifier la
	// couleur. Le vecteur qu'on va ajouter aura la variable remotePosition.y pour que la couleur dépense de la hauteurs
	gl_FragColor = vec4(1, 0, 0, 1) + vec4(-(remotePosition.y / 4.04),0.0, (remotePosition.y / 4.04),0.0) ;
}