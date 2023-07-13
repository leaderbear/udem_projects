/*
Uniforms already defined by THREE.js
------------------------------------------------------
uniform mat4 modelMatrix; = object.matrixWorld
uniform mat4 modelViewMatrix; = camera.matrixWorldInverse * object.matrixWorld
uniform mat4 projectionMatrix; = camera.projectionMatrix
uniform mat4 viewMatrix; = camera.matrixWorldInverse
uniform mat3 normalMatrix; = inverse transpose of modelViewMatrix
uniform vec3 cameraPosition; = camera position in world space
attribute vec3 position; = position of vertex in local space
attribute vec3 normal; = direction of normal in local space
attribute vec2 uv; = uv coordinates of current vertex relative to texture coordinates
------------------------------------------------------
*/

//Custom defined Uniforms for TP3
uniform sampler2D textureMask; //Texture mask, color is different depending on whether this mask is white or black.
uniform sampler2D textureNumberMask; //Texture containing the billard ball's number, the final color should be black when this mask is black.
uniform vec3 maskLightColor; //Ambient/Diffuse/Specular Color when textureMask is white
uniform vec3 materialDiffuseColor; //Diffuse color when textureMask is black (You can assume this is the default color when you are not using textures)
uniform vec3 materialSpecularColor; //Specular color when textureMask is black (You can assume this is the default color when you are not using textures)
uniform vec3 materialAmbientColor; //Ambient color when textureMask is black (You can assume this is the default color when you are not using textures)
uniform float shininess; //Shininess factor

uniform vec3 lightDirection; //Direction of directional light in world space
uniform vec3 lightColor; //Color of directional light
uniform vec3 ambientLightColor; //Color of ambient light , note : c'est l’équation de l’éclairage ambiant: I = Ia Ka

out vec3 result;  // We compute everything in vertex shader then we pass it to fragemet shader
out vec2 uv_;

void main() {

    uv_ = uv;


	//Before applying textures, assume that materialDiffuseColor/materialSpecularColor/materialAmbientColor are the default diffuse/specular/ambient color.
	//For textures, you can first use texture2D(textureMask, uv) as the billard balls' color to verify correctness, then use mix(...) to re-introduce color.
	//Finally, mix textureNumberMask too so numbers appear on the billard balls and are black.
    //texture2D(textureMask, uv);
    // Multiply each vertex by the model-view matrix and the projection matrix to get final vertex position
	vec4 relativeVertexPosition = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * relativeVertexPosition;


    // Avant de compléter la formule dans 13_Lighting_Fr.pdf, nous avons besoin de :
    // Note : tout normaliser (p.14)

    vec4 lightDirectionVue = viewMatrix * vec4(lightDirection, 0.0);   // transformer lightDirection ds coord vue

    vec3 l = normalize(-lightDirection);                               // inverser le vecteur lightDirection
    vec3 n = normalize(normalMatrix * normal);                         // Vecteur normale à surface (p.57)
    vec3 r = normalize(reflect(-l,n));                                 // La loi de Snell-Descartes (p.17)
    vec3 v = normalize(-relativeVertexPosition.xyz);                   // Direction de la vue



    // Rappel : Phong est une combinaison de ambient, diffuse et specular lighting.

    // 1. Compute Ambiant  : p.12
    vec3 Ia = ambientLightColor * materialAmbientColor;

    // 2. Compute  Diffuse : p.14
    vec3 Id = lightColor * materialDiffuseColor * max(dot(n, l), 0.0); // if angle between r&v>90°-> negative dot (p.15)

    // 3. Compute Specular : p.20
    vec3 Is = lightColor * materialSpecularColor * pow(max(0.0,dot(r,v)),shininess); // voir commentaire ci- haut ^

    // 4 . Sum everything  : p.24
    result = vec3(Ia + Id + Is);
}
