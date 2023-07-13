// Tabta Abderrahim 20133680

// The uniform variable is set up in the javascript code and the same for all vertices
uniform vec3 remotePosition;
uniform float xoff;


void main() {


    // Suite à la lecture de TP1.js, j'ai découvert que les keys binds (q,a,w..) sont déjà définis. De plus, ici
    // La variable uniforme est remotePosition qui selon le dictionnaire dans tp1.js rassemble les trois remotePosition.
    // Donc, ici, le shader est général et permet de modifier la position dans l'espace des trois controlleurs.
    // Remarque, j'ai dû ajouter le -5.0 avec des essais erreurs avant de découvrir que dans tp1.js , il y'a le 5.0
    // qui décalait un peu trop les controlleurs sur l'axe vert

    /* HINT: WORK WITH remotePosition HERE! */
    // Multiply each vertex by the model-view matrix and the projection matrix to get final vertex position
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position + remotePosition + vec3(xoff, 0.0, -5.0), 1.0);
}
