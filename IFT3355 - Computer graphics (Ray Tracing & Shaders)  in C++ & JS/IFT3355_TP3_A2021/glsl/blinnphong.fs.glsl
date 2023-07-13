/*
Uniforms already defined by THREE.js
------------------------------------------------------
uniform mat4 viewMatrix; = camera.matrixWorldInverse
uniform vec3 cameraPosition; = camera position in world space
------------------------------------------------------
*/

uniform sampler2D textureMask; //Texture mask, color is different depending on whether this mask is white or black.
uniform sampler2D textureNumberMask; //Texture containing the billard ball's number, the final color should be black when this mask is black.
uniform vec3 maskLightColor; //Ambient/Diffuse/Specular Color when textureMask is white
uniform vec3 materialDiffuseColor; //Diffuse color when textureMask is black (You can assume this is the default color when you are not using textures)
uniform vec3 materialSpecularColor; //Specular color when textureMask is black (You can assume this is the default color when you are not using textures)
uniform vec3 materialAmbientColor; //Ambient color when textureMask is black (You can assume this is the default color when you are not using textures)
uniform float shininess; //Shininess factor

uniform vec3 lightDirection; //Direction of directional light in world space
uniform vec3 lightColor; //Color of directional light
uniform vec3 ambientLightColor; //Color of ambient light

in vec3 v;
in vec3 n;

void main() {
	//TODO: BLINN-PHONG SHADING
	//Use Blinn-Phong reflection model
	//Hint: Similar to Phong shader, but use halfway vector instead.
	
	//Before applying textures, assume that materialDiffuseColor/materialSpecularColor/materialAmbientColor are the default diffuse/specular/ambient color.
	//For textures, you can first use texture2D(textureMask, uv) as the billard balls' color to verify correctness, then use mix(...) to re-introduce color.
	//Finally, mix textureNumberMask too so numbers appear on the billard balls and are black.

	// Avant de compléter la formule dans 13_Lighting_Fr.pdf, nous avons besoin de :
	// Note : tout normaliser (p14)

	vec4 lightDirectionVue = viewMatrix * vec4(lightDirection, 0.0);   // transformer lightDirection ds coord vue

	vec3 l = normalize(-lightDirection);                               // inverser le vecteur lightDirection
	vec3 r = normalize(reflect(-l,n));                                 // La loi de Snell-Descartes (p.17)
	vec3 h = normalize(( l + v ) / 2.0 );                              // Vecteur à mi-chemin (p. 22)               *new


	// Rappel : Phong est une combinaison de ambient, diffuse et specular lighting.

	// 1. Compute Ambiant:
	vec3 Ia = ambientLightColor * materialAmbientColor;

	// 2. Compute  Diffuse:
	vec3 Id = lightColor * materialDiffuseColor * max(dot(n, l), 0.0); // if angle between r&v>90°-> negative dot (p15)

	// 3. Compute Specular re-calculé : p.22                                                                        *new
	vec3 Is = lightColor * materialSpecularColor * pow(max(0.0,dot(h,n)),shininess);

	// 4 . Sum everything :
	vec3 result = vec3(Ia + Id + Is);

	//Placeholder color
	gl_FragColor = vec4(result, 1.0);
}