/*------------------------------------------------------*/
/* Prog    : Tp2_IFT3205-2-3.c                          */
/* Auteur  :  Abderrahim Tabta & Maxime Ton             */
/* Date    : --/--/2010                                 */
/* version :                                            */ 
/* langage : C                                          */
/* labo    : DIRO                                       */
/*------------------------------------------------------*/

/*------------------------------------------------*/
/* FICHIERS INCLUS -------------------------------*/
/*------------------------------------------------*/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "FonctionDemo2.h"

/*------------------------------------------------*/
/* DEFINITIONS -----------------------------------*/   
/*------------------------------------------------*/
#define NAME_VISUALISER "display "
#define NAME_IMG_IN1  "UdM_1"
#define NAME_IMG_IN2  "UdM_2"

/*------------------------------------------------*/
/* PROTOTYPE DE FONCTIONS  -----------------------*/   
/*------------------------------------------------*/

void VisualizeImg(float**, int, int);               // source : TP1  

// function that multiply each value by (-1)^i+j
void MyProcessImg(float** matrix, int length, int width){
  for(int i=0; i<length; i++) {
    for(int j=0; j<width; j++) {
      matrix[i][j]*= pow(-1, i+j);
    }
  }
}

void VisualizeImg(float** matrix,int length, int width) {
for(int i=0; i<length; i++) {
    for(int j=0; j<width; j++) {
      matrix[i][j] = log(1+matrix[i][j]);
    }
  }
}

void Rotate(float** matriceOut, float** matriceIn, float theta, int length, int width) { // TODO FIX : something wrong here when trying to find best angle
    // Calculate sine and cosine of the rotation angle
    const float cosTheta = cos(theta);
    const float sinTheta = sin(theta);

    // Center of the matrix to be rotated
    int centerY = width / 2 - ((width/2) * cosTheta - (length/2) * sinTheta);
    int centerX = length / 2 - ((width/2) * sinTheta +( length/2) * cosTheta);

    // Calculate the maximum i and j indices for the matrix
    int maxI = length - 1;
    int maxJ = width - 1;

    // Iterate over each pixel in the matrix
    for (int i = 0; i < length; i++) {
        for (int j = 0; j < width; j++) {

            // Apply the rotation transformation to the pixel coordinates (Counter-clockwise)
            float x = i ; 
            float y = j ; 
            float xPrime = x * cosTheta + y * sinTheta + centerX;       
            float yPrime = -x * sinTheta + y * cosTheta + centerY;
        
            // If the transformed coordinates are outside the bounds of the matrix, set the pixel value to 0
            if (xPrime < 0 || xPrime >= maxI || yPrime < 0 || yPrime >= maxJ) {
                matriceOut[i][j] = 0.0;
                
            // If the transformed coordinates correspond exactly to a pixel in the input matrix, set the pixel value to that of the input pixel
            } else if (ceilf(xPrime) == xPrime && ceilf(yPrime) == yPrime) {
                matriceOut[i][j] = matriceIn[(int)xPrime][(int)yPrime];

            // Otherwise, use bilinear interpolation to calculate the pixel value based on the values of the surrounding pixels
            } else {
                int xFloor = floorf(xPrime);
                int yFloor = floorf(yPrime);
                int xCeil = xFloor + 1;
                int yCeil = yFloor + 1;

                float f1 = matriceIn[xFloor][yFloor] + (xCeil - xPrime) * (matriceIn[xFloor][yCeil] - matriceIn[xFloor][yFloor]);
                float f2 = matriceIn[xCeil][yFloor] + (xPrime - xFloor) * (matriceIn[xCeil][yCeil] - matriceIn[xCeil][yFloor]);
                float f3 = f1 + (yCeil - yPrime) * (f2 - f1);
                matriceOut[i][j] = f3;
            }
        }
    }
}


float CalculateError(float** matriceM, float** matriceR, int length, int width){
  float error = 0.0;
  for(int i=0; i<length; i++){
    for(int j=0; j<width; j++){
      error += abs(abs(matriceR[i][j])-abs(matriceM[i][j]));
    }
  }
  return error;
}

float FindAngle(float** MatriceImgR2, float** MatriceImgM2, float** MatriceImgM1, float length, float width, float start, float end, float step){
  float errorMin = INFINITY; // Set the minimum error to infinity, so that the first error calculated will always be smaller
  float bestAngle = 0.0; // Set the best angle to zero
  float current; // Set the current resemblance error to zero
  
  for(float theta = start; theta<=end; theta+=step) { // For each possible value of theta (angle) between -pi/16 and pi/16, with a step of 0.005
    Rotate(MatriceImgR2, MatriceImgM2, theta, length, width); // Rotate the image by theta
    current = CalculateError(MatriceImgM1, MatriceImgR2, length, width); // Calculate the error between the two images
    printf("[%f::%i]>",theta,(int)current); // Print the current theta and the current resemblance error
    
    if(current < errorMin ){ // If the current resemblance error is smaller than the minimum error
      errorMin = current; // Set the minimum error to the current resemblance error
      bestAngle = theta; // Set the best angle to the current theta
    }
  }
  return bestAngle;
}
/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
  int i,j,k;
  int length,width;
  float Theta0;
  int x0,y0;
  char BufSystVisuImg[100];

  //Constante
  length=512;
  width=512;
  

  //Allocation Memoire 
  float** MatriceImgI1=fmatrix_allocate_2d(length,width);
  float** MatriceImgM1=fmatrix_allocate_2d(length,width);
  float** MatriceImgR1=fmatrix_allocate_2d(length,width);
  float** MatriceImgI2=fmatrix_allocate_2d(length,width);
  float** MatriceImgM2=fmatrix_allocate_2d(length,width);
  float** MatriceImgR2=fmatrix_allocate_2d(length,width);
  float** MatriceImgI3=fmatrix_allocate_2d(length,width);
  float** MatriceImgM3=fmatrix_allocate_2d(length,width);
  float** MatriceImgR3=fmatrix_allocate_2d(length,width);
  float** MatriceImg3=fmatrix_allocate_2d(length,width);

  float** MatriceImgOut=fmatrix_allocate_2d(length,width);        // Ajouté +++ 

  //Lecture Image 
  float** MatriceImg1=LoadImagePgm(NAME_IMG_IN1,&length,&width);
  float** MatriceImg2=LoadImagePgm(NAME_IMG_IN2,&length,&width);

  MyProcessImg(MatriceImg1,length,width);
  MyProcessImg(MatriceImg2,length,width);

  FFTDD(MatriceImg1,MatriceImgI1,length,width);
  FFTDD(MatriceImg2,MatriceImgI2,length,width);

  Mod(MatriceImgM1,MatriceImg1,MatriceImgI1,length,width);
  Mod(MatriceImgM2,MatriceImg2,MatriceImgI2,length,width);

  float result = FindAngle(MatriceImgR2, MatriceImgM2, MatriceImgM1, length, width, -M_PI/16, M_PI/16, 0.005);


  //Theta0 = 22.5 * M_PI / 180.0;                                 // Deg -> Rad 
  //Rotate(MatriceImgOut,MatriceImgIn,Theta0, length,width);
  //float bestAngle = 0.0;
  
  //Commande systeme: VISU
  printf("\n Angle >>> [%f]",result);


  //==End=========================================================

  //Liberation memoire 
  free_fmatrix_2d(MatriceImgR1);
  free_fmatrix_2d(MatriceImgI1);
  free_fmatrix_2d(MatriceImgM1);
  free_fmatrix_2d(MatriceImgR2);
  free_fmatrix_2d(MatriceImgI2);
  free_fmatrix_2d(MatriceImgM2);
  free_fmatrix_2d(MatriceImgR3);
  free_fmatrix_2d(MatriceImgI3);
  free_fmatrix_2d(MatriceImgM3);
  free_fmatrix_2d(MatriceImg1);
  free_fmatrix_2d(MatriceImg2);  
  free_fmatrix_2d(MatriceImg3);

  //retour sans probleme
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}


