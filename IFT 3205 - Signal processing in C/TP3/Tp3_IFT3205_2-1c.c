/*----------------------------------------------------------------*/
/* Prog    : Tp3_IFT3205-2-1c.c                                   */
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
#define NAME_IMG_OUT "image-TpIFT3205-2-1c"

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

float getValueAt(float** matrix, int row, int col, int length, int width) {
    // If the row is negative or greater than the number of rows, wrap around to the other side
    if (row < 0 || row >= length) {
        row = (row + length) % length;
    }
    // If the column is negative or greater than the number of columns, wrap around to the other side
    if (col < 0 || col >= width) {
        col = (col + width) % width;
    }
    return matrix[row][col];
}

void resizeBilinear(float** src, float** dest, int length, int width, int scale) {
    float l_ratio = (length * scale) / (float)length;
    float w_ratio = (width * scale) / (float)width;

    // Loop through the destination matrix
    for (int i = 0; i < length * scale; i++) {
        for (int j = 0; j < width * scale; j++) {
            // Calculate the nearest neighbor coordinates in the source matrix
            float src_row = i / l_ratio;
            float src_col = j / w_ratio;

            // Get the integer and fractional parts of the row and column coordinates
            int src_row_int = (int)src_row;
            int src_col_int = (int)src_col;
            float src_row_frac = src_row - src_row_int;
            float src_col_frac = src_col - src_col_int;

            // Get the four nearest neighbors
            float top_left = getValueAt(src, src_row_int, src_col_int, length, width);
            float top_right = getValueAt(src, src_row_int, src_col_int + 1, length, width);
            float bottom_left = getValueAt(src, src_row_int + 1, src_col_int, length, width);
            float bottom_right = getValueAt(src, src_row_int + 1, src_col_int + 1, length, width);

            // Calculate the weighted sum
            float top_interp = top_left + src_col_frac * (top_right - top_left);
            float bottom_interp = bottom_left + src_col_frac * (bottom_right - bottom_left);
            float interp_value = top_interp + src_row_frac * (bottom_interp - top_interp);

            dest[i][j] = interp_value;
        }
    }
}

// Fonction TP1 
void CenterImg(float** matrix,int length, int width) {
  for(int i=0; i<length; i++) {
    for(int j=0; j<width; j++) {
      if((i+j)%2==0)
        matrix[i][j] *= -1;   // matrix containing the image
    }
  }
}

void resizeImg(float** srcImgR, float** srcImgI, int srcLength, int srcWidth, float** destImgR, float** destImgI, int destLength, int destWidth) {
  int i, j;

  for (i=0; i < srcLength; i++){
    for (j=0; j < srcWidth; j++) {
        destImgR[i + (destLength-srcLength)/2][j + (destWidth-srcWidth)/2] = srcImgR[i][j];
        destImgI[i + (destLength-srcLength)/2][j + (destWidth-srcWidth)/2] = srcImgI[i][j];
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

  int scale = 4;

  //Lecture Image 
  float** MatriceImg1R = LoadImagePgm(NAME_IMG_IN,&length,&width); // matrix containing the image
  float** MatriceImg1I = fmatrix_allocate_2d(length,width);

  float** MatriceImg2R = fmatrix_allocate_2d(length*scale,width*scale);    // matrix containing the resized img
  float** MatriceImg2I = fmatrix_allocate_2d(length*scale,width*scale);

  //Interpolation 
  CenterImg(MatriceImg1R,length,width);
  FFTDD(MatriceImg1R, MatriceImg1I, length, width);

  resizeImg(MatriceImg1R, MatriceImg1I, length, width, MatriceImg2R, MatriceImg2I, length*4, width*4);
 
  IFFTDD(MatriceImg2R, MatriceImg2I, length*scale, width*scale);
  CenterImg(MatriceImg2R, length*scale, width*scale);
  Recal(MatriceImg2R, length*scale, width*scale);


  //Sauvegarde
  SaveImagePgm(NAME_IMG_OUT,MatriceImg2R, length*4, width*4);

  //Commande systeme: VISU
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT);
  strcat(BufSystVisuImg,".pgm&");
  printf(" %s",BufSystVisuImg);
  system(BufSystVisuImg);
 


  //==End=========================================================

  //Liberation memoire 
  free_fmatrix_2d(MatriceImg1R);
  free_fmatrix_2d(MatriceImg1I);
  free_fmatrix_2d(MatriceImg2R);
  free_fmatrix_2d(MatriceImg2I);
 
  //retour sans probleme
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}


