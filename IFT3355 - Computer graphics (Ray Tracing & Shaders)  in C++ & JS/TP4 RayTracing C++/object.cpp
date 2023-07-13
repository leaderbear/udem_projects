#include "object.hpp"

#include <cmath>
#include <cfloat>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <iostream>


bool Object::intersect(Ray ray, Intersection& hit) const
{
	// Assure une valeur correcte pour la coordonnée W de l'origine et de la direction
	// Vous pouvez commentez ces lignes si vous faites très attention à la façon de construire vos rayons.
	ray.origin[3] = 1;
	ray.direction[3] = 0;

	Ray local_ray(i_transform * ray.origin, i_transform * ray.direction);
	//!!! NOTE UTILE : pour calculer la profondeur dans localIntersect(), si l'intersection se passe à
	// ray.origin + ray.direction * t, alors t est la profondeur
	//!!! NOTE UTILE : ici, la direction peut êytre mise à l'échelle, alors vous devez la renormaliser
	// dans localIntersect(), ou vous aurez une profondeur dans le système de coordonnées local, qui
	// ne pourra pas être comparée aux intersection avec les autres objets.
	if (localIntersect(local_ray, hit))
	{
		// Assure la valeur correcte de W.
		hit.position[3] = 1;
		hit.normal[3] = 0;

		// Transforme les coordonnées de l'intersection dans le repère global.
		hit.position = transform * hit.position;
		hit.normal = (n_transform * hit.normal).normalized();

		return true;
	}

	return false;
}


bool Sphere::localIntersect(Ray const& ray, Intersection& hit) const
{
	// @@@@@@ VOTRE CODE ICI
	// Vous pourriez aussi utiliser des relations géométriques pures plutôt que les
	// outils analytiques présentés dans les slides.
	// Ici, dans le système de coordonées local, la sphère est centrée en (0, 0, 0)
	//
	// NOTE : hit.depth est la profondeur de l'intersection actuellement la plus proche,
	// donc n'acceptez pas les intersections qui occurent plus loin que cette valeur.

	
	Vector raydirection = ray.direction;

	// Méthode démo 8 

	// 4. Simplifier partout où y'a un dot avec center(0,0,0).
	bool validInter = false;
	float t = 1;


	// Calculer le discriminant d
	float A = raydirection.dot(raydirection);
	float B = ray.origin.dot(raydirection) * 2;
	float C = ray.origin.dot(ray.origin) - (radius * radius);
	float D = (B * B) - (4 * A * C);


	// si D == 0 : t ne va prendre qu'une valeur (trivial)
	if (D < 0.001f) {                               // Backup : before its was == 0
		t = ((-1 * B) + sqrt(D)) / (2 * A);  
		//validInter = true;						// rend la scène plus sombre :( 	
	}

	//  S'il y'a deux intersections, garder la plus proche
	else if (D > 0) {

		float t1 = ((-1 * B) + sqrt(D)) / (2 * A);
		float t2 = ((-1 * B) - sqrt(D)) / (2 * A);


		// Signes : Positif , Positif 
		if (t1 > 0.01f && t2 > 0.01f) {
			t =std::min(t1, t2); // plus petit
			validInter = true;
		}

		// Signes : Positif , Négatif 
		else if (t1 > 0.01f && t2 < 0.01f) {
			t = t1;//le positif
			validInter = true;
		}


		// Signes : Négatif , Positif
		else if (t1 < 0.01f && t2 > 0.01f) {
			t = t2;
			validInter = true;
		}

		// Signes : Négatif , Négatif
		else if (t1 < 0.01f && t2 < 0.01f) {
			// do nothing
		}
	}

	// S'il y'a une intersection alors: 
	if (D == 0 || D > 0) {

		if (t < hit.depth && t > 0.01f && validInter) {    
			hit.depth = t;
			hit.normal = -1* (ray.origin + (t * raydirection)).normalized(); // todo : -1 hard code, find issue
			hit.position = ray.origin + (t * raydirection);
			return true;

		}
	}

	/* Back-up method :  sphère plus loin qu'elle ne deverait être :( 
	// distance sur notre rayon plus proche de sphère
	float s = (Vector(0, 0, 0) - ray.origin).dot(raydirection);

	// p sur ray et est le plus proche du centre de la sphère 
	Vector p = ray.origin + raydirection * s;

	// distance entre p et centre sphère 
	float d = (Vector(0, 0, 0) - p).length();

	// Intersection lorsque d plus petite ou égale au rayon de la sphère 
	if (d < radius) {

		// Maintenant calculer l'intersection (à une distance x entre p et intersection utilisant la trigo où △ sphere.center , p , intersection )  
		float x = sqrt((radius * radius) - (d * d));

		// Intersection
		Vector vt1 = s - x;
		Vector vt2 = s + x;
		Vector vt = s - x;

		// Depth between camera and intersection 
		float t1 = (vt1 - ray.origin).length();
		float t2 = (vt2 - ray.origin).length();
		float t;

		if (t1 < t2) {
			vt = vt2;
			t = t2;
		}
		else {
			vt = vt1;
			t = t1;
		}


		if (t < hit.depth) {
			hit.depth = t;
			hit.normal = vt.normalized();
			hit.position = vt;
			return true;

		}
	}

	else if (d == radius) {
		Vector vt = s;
		float t = (vt - ray.origin).length();
		if (t < hit.depth) {
			hit.depth = t;
			hit.normal = vt.normalized();
			hit.position = vt;
			return true;
		}
	}*/
	
	return false;
}


bool Plane::localIntersect(Ray const& ray, Intersection& hit) const
{
	// J'ai enlevé la normalisation , car ca marche mieux.
	Vector raydirection = ray.direction;

	// Plan est 0x + 0y + 1z = 0 , soit z=0.  Donc , vecteur normal est :
	Vector normal = Vector(0.0, 0.0, 1.0);

	// Si ray orthogonal à n : pas d'intersection (soit normal.ray = 0)  //Enlevé car ca fait bug
	double e = normal.dot(ray.direction);
	

	if (true) {  // Avant je vérifais si e =! 0 , mais bug :(  

		// Remplacer dans l'équation du plan : Puisque plan 0x + 0y + z = 0. 
		// Nous allons seulement substituer z de ray
		float t = (-1 * ray.origin[2]) / ray.direction[2];
		Vector interPos = ray.origin + (t * ray.direction);

		if (hit.depth >= t && t > 0.0001f) { //switch later
			hit.depth = t;
			hit.normal = normal.normalized() * -1; // -1 is a quick fix
			hit.position = interPos;
			return true;
		}

	}
	return false;
}


bool Conic::localIntersect(Ray const& ray, Intersection& hit) const {
	// @@@@@@ VOTRE CODE ICI (licence créative)
	// Piste : juste avec un cone ? 
	// Piste : plane-cone ?
	
	//Plane plane;
	//if (plane.intersect(new ray? , cone )){ ... }

	return false;
}


// Intersections !
bool Mesh::localIntersect(Ray const& ray, Intersection& hit) const
{
	// Test de la boite englobante
	double tNear = -DBL_MAX, tFar = DBL_MAX;
	for (int i = 0; i < 3; i++) {
		if (ray.direction[i] == 0.0) {
			if (ray.origin[i] < bboxMin[i] || ray.origin[i] > bboxMax[i]) {
				// Rayon parallèle à un plan de la boite englobante et en dehors de la boite
				return false;
			}
			// Rayon parallèle à un plan de la boite et dans la boite: on continue
		}
		else {
			double t1 = (bboxMin[i] - ray.origin[i]) / ray.direction[i];
			double t2 = (bboxMax[i] - ray.origin[i]) / ray.direction[i];
			if (t1 > t2) std::swap(t1, t2); // Assure t1 <= t2

			if (t1 > tNear) tNear = t1; // On veut le plus lointain tNear.
			if (t2 < tFar) tFar = t2; // On veut le plus proche tFar.

			if (tNear > tFar) return false; // Le rayon rate la boite englobante.
			if (tFar < 0) return false; // La boite englobante est derrière le rayon.
		}
	}
	// Si on arrive jusqu'ici, c'est que le rayon a intersecté la boite englobante.

	// Le rayon interesecte la boite englobante, donc on teste chaque triangle.
	bool isHit = false;
	for (size_t tri_i = 0; tri_i < triangles.size(); tri_i++) {
		Triangle const& tri = triangles[tri_i];

		if (intersectTriangle(ray, tri, hit)) {
			isHit = true;
		}
	}
	return isHit;
}

double Mesh::implicitLineEquation(double p_x, double p_y,
	double e1_x, double e1_y,
	double e2_x, double e2_y) const
{
	return (e2_y - e1_y) * (p_x - e1_x) - (e2_x - e1_x) * (p_y - e1_y);
}

bool Mesh::intersectTriangle(Ray const& ray,
	Triangle const& tri,
	Intersection& hit) const
	
{
	// Extrait chaque position de sommet des données du maillage.
	Vector const& p0 = positions[tri[0].pi];
	Vector const& p1 = positions[tri[1].pi];
	Vector const& p2 = positions[tri[2].pi];

	// @@@@@@ VOTRE CODE ICI
	// Décidez si le rayon intersecte le triangle (p0,p1,p2).
	// Si c'est le cas, remplissez la structure hit avec les informations
	// de l'intersection et renvoyez true.
	// Vous pourriez trouver utile d'utiliser la routine implicitLineEquation		()
	// pour calculer le résultat de l'équation de ligne implicite en 2D.
	//
	// NOTE : hit.depth est la profondeur de l'intersection actuellement la plus proche,
	// donc n'acceptez pas les intersections qui occurent plus loin que cette valeur.
	//!!! NOTE UTILE : pour le point d'intersection, 


	// { DÉMO 8 MÉTHODE } 
	
	// M´ethode de la main droite (direction des normales) 
	Vector raydirection = ray.direction; // mieux sans normalisation


	// Clculer normal △ plan  
	Vector p01 = p1 - p0;
	Vector p02 = p2 - p0;
	Vector normal = ((p2 - p0).cross(p1 - p0)).normalized(); // Normaliser ?


	float D = -(normal.dot(p0));												  
	//float D = -1 * (normal[0] * p0[0] + normal[1] * p0[1] + normal[2] * p0[2]);  // Version non-simplifiée

	// Calculer t ( Démo 8 )  
	float t = ((-D - normal.dot(ray.origin))) / (normal.dot(ray.direction));                               //Version simplifiée  

	//float t = (- 1 * (normal[0] * ray.origin[0] + normal[1] * ray.origin[1] + normal[2] * ray.origin[2] + D)) / 
	// (normal[0] * ray.direction[0] + normal[1] * ray.direction[1] + normal[2] * ray.direction[2]);

	// S'assurer que t ne soit pas en arrière 
	if (t <= 0.00f) {
		return false;
	}

	// step b & c 
	Vector intersection = ray.origin + t * ray.direction;
	Vector a0 = ((p1 - p0).cross(intersection - p0));
	Vector a1 = ((p2 - p1).cross(intersection - p1));
	Vector a2 = ((p0 - p2).cross(intersection - p2));

	// calculs pour step d 
	float a01 = a0.dot(a1);
	float a02 = a0.dot(a2);
	float a12 = a1.dot(a2);


	// step d : Condition à vérifier Si intersection à l'intérieur du triangle. Mêmes signes , 2 cas : 

	// Cas 1  : Tous un signe négatif 
	if ((a01 < 0 && a02 < 0 && a12 < 0)) {

		if (t <= hit.depth) {
			hit.depth = t;
			hit.normal = normal.normalized();
			hit.position = intersection;
			return true;
		}
	}

	// Cas 2 : Tous un signe positif
	if ((a01 >= 0 && a02 >= 0 && a12 >= 0)) {

		if (t <= hit.depth) {
			hit.depth = t;
			hit.normal = normal.normalized();
			hit.position = intersection;
			return true;
		}
	}


	return false;
}