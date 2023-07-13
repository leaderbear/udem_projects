/*----------------------------------------------------------------*/
/* Prog    : Tp3_IFT3205-2-1a.c                                   */
/* Auteur  : Tabta Abderrahim (20133680) et Maxime Ton (20143044) */
/* Date    : 11/03/2023                                           */
/* version :                                                      */ 
/* langage : C                                                    */
/* labo    : DIRO                                                 */
/*----------------------------------------------------------------*/

/*------------------------------------------------*/
/* FICHIERS INCLUS -------------------------------*/
/*------------------------------------------------*/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "FonctionDemo3.h"

/*------------------------------------------------*/
/* DEFINITIONS -----------------------------------*/   
/*------------------------------------------------*/
#define NAME_VISUALISER "display "
#define NAME_IMG_IN  "lena128"
#define NAME_IMG_OUT "image-TpIFT3205-2-1a"

/*------------------------------------------------*/
/* PROTOTYPE DE FONCTIONS  -----------------------*/   
/*------------------------------------------------*/

//  Resizes a 2D float array using nearest-neighbor interpolation 
void resizeNearestNeighbor(float** src, float** dest, int length, int width, int scale){
  
    float l_ratio = (length*scale)/(float)length;
    float w_ratio = (width*scale)/(float)width;

    // Loop through the destination matrix
    for(int i=0; i<(length*scale); i++) {
      for(int j=0; j<(width*scale); j++) {

        // nearest neighbor in the source matrix
        int ii = floor(j/w_ratio);
        int jj = floor(i/l_ratio);
        dest[i][j] = src[jj][ii];
      }
    }
}

/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
  int i,j;
  int length,width;
  char BufSystVisuImg[100];


  int scale = 4; // here we can change the scale

  //Lecture Image 
  float** MatriceImg1=LoadImagePgm(NAME_IMG_IN,&length,&width);
  float** MatriceImg2=fmatrix_allocate_2d(length*scale,width*scale);

  //Interpolation 
  resizeNearestNeighbor(MatriceImg1,MatriceImg2,length,width,scale);
 
  //Sauvegarde
  SaveImagePgm(NAME_IMG_OUT,MatriceImg2,length*4,width*scale);

  //Commande systeme: VISU
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT);
  strcat(BufSystVisuImg,".pgm&");
  printf(" %s",BufSystVisuImg);
  system(BufSystVisuImg);
 

  //==End=========================================================

  //Liberation memoire 
  free_fmatrix_2d(MatriceImg1);
  free_fmatrix_2d(MatriceImg2);  
 
  //retour sans probleme
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}


