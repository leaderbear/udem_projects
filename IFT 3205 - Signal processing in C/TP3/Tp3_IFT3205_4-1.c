/*----------------------------------------------------------------*/
/* Prog    : Tp3_IFT3205-4-1.c                                    */
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
#define NAME_IMG_IN  "ParcMontRoyal"
#define NAME_IMG_OUT "image-TpIFT3205-4-1"

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

/* Hard thresholding interpolation */
void applyFilter(float** img_i, float** img_o,
                 int length, int width,
                 int offset_j, int offset_i,
                 int dft_size_x, int dft_size_y, float threshold) {

    // Allocate memory for temporary arrays
    float** temp_real = fmatrix_allocate_2d(dft_size_y, dft_size_x);
    float** temp_img = fmatrix_allocate_2d(dft_size_y, dft_size_x);
    float** temp_mod = fmatrix_allocate_2d(dft_size_y, dft_size_x);

    // Copy the input image to temporary array
    for (int i = 0; i < dft_size_y; i++) {
        for (int j = 0; j < dft_size_x; j++) {
            temp_real[i][j] = img_o[offset_i + i][offset_j + j];
            temp_img[i][j] = 0.0;
        }
    }

    // Apply DFT to the temporary array
    DFT(temp_real, temp_img, dft_size_y, dft_size_x);

    // Compute the modulus of the DFT coefficients
    Mod(temp_mod, temp_real, temp_img, dft_size_y, dft_size_x);

    // Apply hard thresholding to the DFT coefficients
    float max = fabs(temp_real[0][0]);
    for (int i = 0; i < dft_size_y; i++) {
        for (int j = 0; j < dft_size_x; j++) {
            if (temp_mod[i][j] < threshold * max) {
                temp_real[i][j] = 0.0;
                temp_img[i][j] = 0.0;
            }
        }
    }

    // Apply inverse DFT to the temp coefficients
    IDFT(temp_real, temp_img, dft_size_y, dft_size_x);

    // Copy the temp result back to the output image
    for (int i = offset_i; i < offset_i + dft_size_y; i++) {
        for (int j = offset_j; j < offset_j + dft_size_x; j++) {
            if (img_i[i][j] == 255) {
                img_o[i][j] = temp_real[i - offset_i][j - offset_j];
            }
        }
    }

    // Normalize the output image
    Mult(img_o, 1.0, length, width);

    // Free memory
    free_fmatrix_2d(temp_real);
    free_fmatrix_2d(temp_img);
    free_fmatrix_2d(temp_mod);
}


void saveImage(float** img, int length, int width, char* iter, int visu) {
  // Save image (Name + iteration)
  char savename[21] = NAME_IMG_OUT;
  strcat(savename,iter);
  SaveImagePgm(savename,img,length,width);

  // Commande systeme: VISU
  if (visu){
    char BufSystVisuImg[100];
    strcpy(BufSystVisuImg,NAME_VISUALISER);
    strcat(BufSystVisuImg,savename);
    strcat(BufSystVisuImg,".pgm&");
    printf(" %s",BufSystVisuImg);
    system(BufSystVisuImg);
  }
}

//  Computes the average value 
float computeAverage(float** img, int col, int len, int row) {
    float sum = 0; 
    for (int i = 0; i < row; i++) {
        sum += img[i][col];
    }
    return sum/row;
}


/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
    int i, j;
    int length, width;
    int extra_height = 30;

    //Lecture Image 
    float** MatriceImg1 = LoadImagePgm(NAME_IMG_IN,&length,&width);
    float** MatriceImg2 = fmatrix_allocate_2d(length + extra_height, width);
    float** MatriceImg3 = fmatrix_allocate_2d(length + extra_height, width);

    // Prétraitement 

      // Copy + extra_height
    for(i=0; i<length; i++) {
        for(j=0; j<width; j++) {
            MatriceImg3[i + extra_height][j] = MatriceImg1[i][j];
        }
    }

      // Copy + average
    for(i=0; i<extra_height; i++){
      for(j=0; j<width; j++){
        MatriceImg3[i][j] = computeAverage(MatriceImg1, j, length, extra_height);
        MatriceImg2[i][j] = 255;
      }
    }
    
    //Interpolation
    for(i=0; i<width; i++) {
        float th = 0.4;
        for(j=0; j<10; j++) {
            applyFilter(MatriceImg2, MatriceImg3, length + extra_height, width, i * 4, 0, fmin(8, width - i), 90, th);
            th /= 2;
        }
    }

  // Sauvegarder l'image
  SaveImagePgm(NAME_IMG_OUT, MatriceImg3, length + extra_height, width);

  //Commande systeme: VISU
  char BufSystVisuImg[100];
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT);
  strcat(BufSystVisuImg,".pgm&");
  printf(" %s",BufSystVisuImg);
  system(BufSystVisuImg);
}