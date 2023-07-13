/*----------------------------------------------------------------*/
/* Prog    : Tp3_IFT3205-2-1b.c                                   */
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
#define NAME_IMG_OUT "image-TpIFT3205-2-1b"

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
  float** MatriceImg1=LoadImagePgm(NAME_IMG_IN,&length,&width);
  float** MatriceImg2=fmatrix_allocate_2d(length*scale,width*scale);

  //Interpolation 
  resizeBilinear(MatriceImg1,MatriceImg2,length,width,scale);
 
  //Sauvegarde
  SaveImagePgm(NAME_IMG_OUT,MatriceImg2,length*scale,width*scale);

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


