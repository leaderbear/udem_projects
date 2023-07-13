/*------------------------------------------------------*/
/* Prog    : Tp1_IFT3205-1-1.c                          */
/* Auteur  :                                            */
/* Date    : 18/05/2010                                 */
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

#include "FonctionDemo1.h"

/*------------------------------------------------*/
/* DEFINITIONS -----------------------------------*/   
/*------------------------------------------------*/
#define NAME_VISUALISER "display "
#define NAME_IMG_IN  "photograph"
#define NAME_IMG_OUT "image-Tp1_IFT3205-2-1b"


/*------------------------------------------------*/
/* PROTOTYPE DE FONCTIONS  -----------------------*/   
/*------------------------------------------------*/
void CenterImg(float** ,int, int);
void VisualizeImg(float**, int, int);
void DeleteHarmonics(float**, int, int, int);
double Distance(int, int, int, int);

void CenterImg(float** matrix,int length, int width) {
  for(int i=0; i<length; i++) {
    for(int j=0; j<width; j++) {
      if((i+j)%2==0)
        matrix[i][j] *= -1;   // matrix containing the image
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

void DeleteHarmonics(float** matrix, int length, int width, int radius) {
  for(int i=length/2-radius; i<=length/2+radius; i++) {
    for(int j=width/2-radius; j<=width/2+radius; j++) {
      matrix[i][j] = 0.0;
    }
  }
}

void DeleteHarmonicsB(float** matrix, int length, int width) {
  int radius = 7;
  for(int i=0; i<length; i++) {
    for(int j=0; j<width; j++) {
      //if (sqrt(pow((i - length/2), 2) + pow((j - width/2), 2)) > radius) { //old version
      if ( Distance(i, j, length, width) > radius) {
        matrix[i][j]=0.0;
      }
    }
  }
}

double Distance(int x, int y, int length, int width){
  // Find the distance from the center of the screen to the point (x,y).
  // The center of the screen is at (length/2, width/2).
  // The distance formula is: sqrt((x2-x1)^2 + (y2-y1)^2).
  double distance = sqrt(pow((x - length/2), 2) + pow((y - width/2), 2));

  if (x < 0 || y < 0 || length < 0 || width < 0) {
    // If any of the parameters are negative, return -1.
    return -1;
  }

  return distance;
}

/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
  int i,j,k;
  int length,width;
  char BufSystVisuImg[100];
  float** MatriceImgR;
  float** MatriceImgI;
  float** MatriceImgM;

  /*Allocation memoire pour la matrice image*/
  MatriceImgR=LoadImagePgm(NAME_IMG_IN,&length,&width);
  MatriceImgI=fmatrix_allocate_2d(length,width);
  MatriceImgM=fmatrix_allocate_2d(length,width);

  /*Initialisation a zero de toutes les matrices */
  for(i=0;i<length;i++) 
    for(j=0;j<width;j++) 
      {
        MatriceImgI[i][j]=0.0;
        MatriceImgM[i][j]=0.0;
      }
  
  /*Centrer image*/
  CenterImg(MatriceImgR, length, width);

  /*FFT*/
  FFTDD(MatriceImgR,MatriceImgI,length,width);

  DeleteHarmonicsB(MatriceImgI, length, width);
  DeleteHarmonicsB(MatriceImgR, length, width);

  /*Module*/
  //Mod(MatriceImgM,MatriceImgR,MatriceImgI,length,width);

   IFFTDD(MatriceImgR, MatriceImgI, length, width);
   CenterImg(MatriceImgR, length, width);

  /*Pour visu*/
  //VisualizeImg(MatriceImgM,length,width);
  //Mult(MatriceImgM,100.0,length,width);    
  Recal(MatriceImgR,length,width);                   
  
  /*Sauvegarde de MatriceImgM sous forme d'image pgm*/
  SaveImagePgm(NAME_IMG_OUT,MatriceImgR,length,width);

  /*Liberation memoire pour les matrices*/
  free_fmatrix_2d(MatriceImgR);
  free_fmatrix_2d(MatriceImgI); 
  free_fmatrix_2d(MatriceImgM);    

  /*Commande systeme: visualisation de Ingout.pgm*/
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT);
  strcat(BufSystVisuImg,".pgm");
  printf(" %s",BufSystVisuImg);
  system(BufSystVisuImg);

  /*retour sans probleme*/ 
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}


