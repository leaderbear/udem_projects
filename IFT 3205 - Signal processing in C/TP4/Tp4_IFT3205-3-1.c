//------------------------------------------------------
// Prog    : Tp4_IFT3205-3-1                   
// Auteur  : Tabta Abderrahim (20133680) et Maxime Ton (20143044)                                    
// Date    :                                  
// version :                                             
// langage : C                                          
// labo    : DIRO                                       
//------------------------------------------------------

//------------------------------------------------
// FICHIERS INCLUS -------------------------------
//------------------------------------------------
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "FonctionDemo4.h"

//------------------------------------------------
// DEFINITIONS -----------------------------------   
//------------------------------------------------
#define NAME_VISUALISER "display "
#define NAME_IMG_IN  "cameraman"
#define NAME_IMG_OUT1 "image-TpIFT3205-3-1"
#define NAME_IMG_OUT2 "cameraman_degraded"


//------------------------------------------------
// PROTOTYPE DE FONCTIONS  -----------------------   
//------------------------------------------------
void modSquare(float **mag, float **real, float **imag, int rows, int cols);
void multiplyMatrix(float** output_real_matrix, float** output_imaginary_matrix, 
                float** input1_real_matrix, float** input1_imaginary_matrix, 
                float** input2_real_matrix, float** input2_imaginary_matrix, 
                int length, int width);
void initializeMatrixWithZeros(float** matrix, int rows, int columns);
void copyMatrix(float **mat_destination, float **mat_toCopy, int length, int width);
void generateBlurryFilterMatrix(float** matFiltre, int length, int width, float l);
void restoreBlurredImage(float** matRestored, float** mat_b_R, float** mat_b_I, float** mat_psfRealF, float** mat_psfImaginaryF, float l, float var);
void restoreBlurredImageV2(float** matRestored, float** mat_b_R, float** mat_b_I, float** mat_psfRealF, float** mat_psfImaginaryF, float** p_Real, float** p_Imaginary, float l, float var);

//------------------------------------------------
// VARIABLES GLOBALES  ---------------------------   
//------------------------------------------------
float** mat;
float** mat_tmp1;
float** mat_tmp2;
float** mat_img;
float** mat_img_restoredI;
float** mat_restored;
float** mat_degradedImg;
float** mat_degradedImgI;
float** mat_psfRealF;
float** mat_psfImaginaryF;
float** mat_squaredModulusOfPrevious;
float** mat_squaredModulusOfPSF;

float** tmp_restored;
float** tmp_restoredI;

//>Taille Image
int length=256;
int width=256;
int size_image=256;

//>Parametre de degradation
int size=9;
float variance_noise=0.5;
int psf=1;

//>Nb d'Iterations
int nbiter=20;

//>ImprovmentSNR
float isnr;

//------------------------------------------------
//------------------------------------------------
// FONCTIONS  ------------------------------------   
//------------------------------------------------
//------------------------------------------------
void modSquare(float **mag, float **real, float **imag, int rows, int cols) {
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            // Calculate the squared magnitude of the complex number
            float real_squared = real[i][j] * real[i][j];
            float imag_squared = imag[i][j] * imag[i][j];
            float magnitude_squared = real_squared + imag_squared;
            
            // Store the result in mag
            mag[i][j] = magnitude_squared;
        }
    }
}

void multiplyMatrix(float** output_real_matrix, float** output_imaginary_matrix, 
                float** input1_real_matrix, float** input1_imaginary_matrix, 
                float** input2_real_matrix, float** input2_imaginary_matrix, 
                int length, int width) {
    // Multiply two complex matrices
    for (int i = 0; i < length; i++) {
        for (int j = 0; j < width; j++) {
            float a = input1_real_matrix[i][j];
            float b = input1_imaginary_matrix[i][j];
            float c = input2_real_matrix[i][j];
            float d = input2_imaginary_matrix[i][j];
            output_real_matrix[i][j] = a * c - b * d;
            output_imaginary_matrix[i][j] = a * d + c * b;
        }
    }
}

void initializeMatrixWithZeros(float** matrix, int rows, int columns) {
    // initialize all elements of matrix to 0.0f
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < columns; ++j) {
            matrix[i][j] = 0.0f;
        }
    }
}

void copyMatrix(float **mat_destination, float **mat_Input, int length, int width) {
    // Copy each element in mat_toCopy to mat_destination
    for (int i = 0; i < length; i++) {
        for (int j = 0; j < width; j++) {
            mat_destination[i][j] = mat_Input[i][j];
        }
    }
}

void generateBlurryFilterMatrix(float** matF, int length, int width, float l) {
  // Iterate through each element of the matFiltre array
  for (int i = 0; i < length; i++) {
    for (int j = 0; j < width; j++) {
      // Check if the element is within the blurry filter boundaries
      if ((i < l / 2 && (j < l / 2 || j >= width - l / 2)) || 
          (i >= length - l / 2 && (j < l / 2 || j >= width - l / 2))) {
        // Set the value of that element in the matFiltre array
        matF[i][j] = (length * width) / (l * l);
      } else {
        // Set the value of that element to 0
        matF[i][j] = 0;
      }
    }
  }
}


void restoreBlurredImage(float** matRestored, float** mat_b_R, float** mat_b_I, float** mat_psfRealF, float** mat_psfImaginaryF, float l, float var) {
 
  var /= length * width;  // Compute variance per pixel
  generateBlurryFilterMatrix(mat_psfRealF, length, width, l);
  FFTDD(mat_psfRealF, mat_psfImaginaryF, length, width);  
  modSquare(mat_squaredModulusOfPrevious, mat_b_R, mat_b_I, length, width);  
  modSquare(mat_squaredModulusOfPSF, mat_psfRealF, mat_psfImaginaryF, length, width);

  for (int i = 0; i < length; i++) {   // Compute right side of the equation
    for (int j = 0; j < width; j++) {
      float denom = mat_squaredModulusOfPSF[i][j] + var / mat_squaredModulusOfPrevious[i][j];
      mat_tmp1[i][j] = mat_psfRealF[i][j] / denom;
      mat_tmp2[i][j] = - mat_psfImaginaryF[i][j] / denom;   // Conjugate of mat_psfImaginaryF / denom 
    }
  }
}

void restoreBlurredImageV2(float** matRestored, float** mat_b_R, float** mat_b_I, float** mat_psfRealF,float** mat_psfImaginaryF, float** p_Real, float** p_Imaginary, float l, float var) {

  var /= length * width;  // Compute variance per pixel
  generateBlurryFilterMatrix(mat_psfRealF, length, width, l);
  FFTDD(mat_psfRealF, mat_psfImaginaryF, length, width);

  modSquare(mat_squaredModulusOfPrevious, p_Real, p_Imaginary, length, width);  
  modSquare(mat_squaredModulusOfPSF, mat_psfRealF, mat_psfImaginaryF, length, width);

  for (int i = 0; i < length; i++) {   // Compute right side of the equation
    for (int j = 0; j < width; j++) {
      float denom = mat_squaredModulusOfPSF[i][j] + var / mat_squaredModulusOfPrevious[i][j];
      mat_tmp1[i][j] = mat_psfRealF[i][j] / denom;
      mat_tmp2[i][j] = - mat_psfImaginaryF[i][j] / denom;   // Conjugate of mat_psfImaginaryF / denom 
    }
  }
  

  // Multiply G(u, v) by the right side
  multiplyMatrix(matRestored, mat_img_restoredI, mat_b_R, mat_b_I, mat_tmp1, mat_tmp2, length, width);
  IFFTDD(matRestored, mat_img_restoredI, length, width); 

  // Adjust grey values
  for(int i=0; i<length; i++) {
    for(int j=0; j<width; j++) {
        matRestored[i][j] = fmax(fmin(matRestored[i][j], 255), 0);
    }
  }
}

float calculateISNR(float** originalImg, float** degradedImg, float** restoredImg, int length, int width) {
  float numerator = 0;
  float denominator = 0;
  for (int i = 0; i < length; i++) {
    for (int j = 0; j < width; j++) {
      float diffOriginalDegraded = originalImg[i][j] - degradedImg[i][j];
      float diffOriginalRestored = originalImg[i][j] - restoredImg[i][j];
      numerator += powf(diffOriginalDegraded, 2);
      denominator += powf(diffOriginalRestored, 2);
    }
  }
  return 10 * log10f(numerator / denominator);
}


//---------------------------------------------------------
//---------------------------------------------------------
// PROGRAMME PRINCIPAL   ----------------------------------                     
//---------------------------------------------------------
//---------------------------------------------------------
int main(int argc,char **argv)
 {
  int i,j,k;
  int lgth,wdth;
  char BufSystVisuImg[100];

  //Allocation memoire matrice
  mat=fmatrix_allocate_2d(length,width);
  mat_tmp1=fmatrix_allocate_2d(length,width);
  mat_tmp2=fmatrix_allocate_2d(length,width);
  mat_restored=fmatrix_allocate_2d(length,width);
  mat_img_restoredI=fmatrix_allocate_2d(length,width);
  mat_degradedImgI=fmatrix_allocate_2d(length,width);
  mat_degradedImg=fmatrix_allocate_2d(length,width);
  mat_psfRealF=fmatrix_allocate_2d(length,width);
  mat_psfImaginaryF=fmatrix_allocate_2d(length,width);
  mat_squaredModulusOfPrevious=fmatrix_allocate_2d(length,width);
  mat_squaredModulusOfPSF=fmatrix_allocate_2d(length,width);

  tmp_restored=fmatrix_allocate_2d(length,width); 
  tmp_restoredI=fmatrix_allocate_2d(length,width);

 
  //=========================================================
  //== PROG =================================================
  //=========================================================

  //>Lecture Image
  mat_img=LoadImagePgm(NAME_IMG_IN,&lgth,&wdth);

  //--------------------------------------------------------
  //>Degradation
  //--------------------------------------------------------
  // Cette fonction ajoute un flou cr�� par une psf uniforme 
  // (fonction porte) de taille sizexsize puis ajoute sur
  // cette image rendue floue, un bruit Gaussien de variance
  // variance_noise
  //
  // Entr�e : mat_img :: image non d�grad�e
  // Sortie : mat     :: image d�grad�e
  //--------------------------------------------------------
  degradation(mat_img,mat_degradedImg,length,width,psf,size,variance_noise);
  copyMatrix(mat, mat_degradedImg, length, width);

  //============
  //WIENER
  //============
  copyMatrix(tmp_restored, mat_degradedImg, length, width);

  //>Initialisation
  initializeMatrixWithZeros(mat_degradedImgI, length, width);

  //>Compute FFTDD
  FFTDD(mat_degradedImg, mat_degradedImgI, length, width);
  
  for (int i = 0; i < nbiter; i++){   

      initializeMatrixWithZeros(mat_psfRealF, length, width);
      initializeMatrixWithZeros(mat_psfImaginaryF, length, width);
      initializeMatrixWithZeros(tmp_restoredI, length, width); 
      FFTDD(tmp_restored, tmp_restoredI, length, width);

      //>Restore image
      restoreBlurredImageV2(mat_restored, mat_degradedImg, mat_degradedImgI, mat_psfRealF, mat_psfImaginaryF, tmp_restored, tmp_restoredI, size, variance_noise);
      copyMatrix(tmp_restored, mat_restored, length, width);

      //>Print ISNR
      isnr = calculateISNR(mat_img, mat, mat_restored, length, width);
      printf("ISNR %d: %f\n", i, isnr);
  }

  //---------------------------------------------
  // SAUVEGARDE et VISU
  // -------------------
  // Le resultat de la restoration > mat_restored
  // L'image d�grad�e              > mat
  // L'image non d�grad�e          > mat_img
  //----------------------------------------------
  SaveImagePgm(NAME_IMG_OUT1,mat_restored,length,width);
  SaveImagePgm(NAME_IMG_OUT2,mat,length,width);

  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_IN);
  strcat(BufSystVisuImg,".pgm&");
  printf("\n > %s",BufSystVisuImg);
  system(BufSystVisuImg);
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT1);
  strcat(BufSystVisuImg,".pgm&");
  printf("\n > %s",BufSystVisuImg);
  system(BufSystVisuImg);
  strcpy(BufSystVisuImg,NAME_VISUALISER);
  strcat(BufSystVisuImg,NAME_IMG_OUT2);
  strcat(BufSystVisuImg,".pgm&");
  printf("\n > %s",BufSystVisuImg);
  system(BufSystVisuImg);
  

  //Liberation memoire pour les matrices
  if (mat)            free_fmatrix_2d(mat); 
  if (mat_img)        free_fmatrix_2d(mat_img); 
  if (mat_restored)       free_fmatrix_2d(mat_restored);
  if (mat_degradedImgI)  free_fmatrix_2d(mat_degradedImgI);
  if (mat_degradedImg)  free_fmatrix_2d(mat_degradedImg); 
  if (mat_psfRealF)  free_fmatrix_2d(mat_psfRealF); 
  if (mat_psfImaginaryF)  free_fmatrix_2d(mat_psfImaginaryF); 
  if (mat_squaredModulusOfPrevious)  free_fmatrix_2d(mat_squaredModulusOfPrevious);
  if (mat_squaredModulusOfPSF)  free_fmatrix_2d(mat_squaredModulusOfPSF); 
  if (mat_tmp1) free_fmatrix_2d(mat_tmp1);
  if (mat_tmp2) free_fmatrix_2d(mat_tmp2);
  if (mat_img_restoredI) free_fmatrix_2d(mat_img_restoredI);

  if (tmp_restored)  free_fmatrix_2d(tmp_restored);
  if (tmp_restoredI)  free_fmatrix_2d(tmp_restoredI); 

  //retour sans probleme 
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}