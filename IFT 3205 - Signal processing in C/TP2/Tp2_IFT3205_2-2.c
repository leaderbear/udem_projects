/*------------------------------------------------------*/
/* Prog    : Tp2_IFT3205-2-2.c                          */
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
#define NAME_IMG_IN1  "lena"
#define NAME_IMG_OUT1 "image-TpIFT3205-2-2"


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
  float** MatriceImgIn=LoadImagePgm(NAME_IMG_IN1,&length,&width);

  Theta0 = 22.5 * M_PI / 180.0;                                 // Deg -> Rad 
  Rotate(MatriceImgOut,MatriceImgIn,Theta0, length,width);

  //Sauvegarde
  SaveImagePgm(NAME_IMG_OUT1,MatriceImgOut,length,width);


  //Commande systeme: VISU
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_IN1);
  strcat(BufSystVisuImg,".pgm&");
  printf(" %s",BufSystVisuImg);
  system(BufSystVisuImg);

  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT1);
  strcat(BufSystVisuImg,".pgm&");
  printf(" %s",BufSystVisuImg);
  system(BufSystVisuImg);


  //==End=========================================================

  //Liberation memoire 
  free_fmatrix_2d(MatriceImgOut);
  free_fmatrix_2d(MatriceImgIn);

  //retour sans probleme
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}


