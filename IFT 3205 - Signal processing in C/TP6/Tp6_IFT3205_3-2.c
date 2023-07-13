/*------------------------------------------------------*/
/* Prog    : Tp6_IFT3205.c                              */
/* Auteur  : Tabta Abderrahim (20133680) et Maxime Ton (20143044) */
/* Date    :                                            */
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

#include "FonctionDemo6.h"

/*------------------------------------------------*/
/* DEFINITIONS -----------------------------------*/   
/*------------------------------------------------*/
#define NAME_VISUALISER "./ViewSig.sh "

/*------------------------------------------------*/
/* PROTOTYPE DE FONCTIONS  -----------------------*/   
/*------------------------------------------------*/

// Function to find the two largest values in an array
void findMaxTwo(float *arr, int size, int *indexMaxA, int *indexMaxB) {
  float maxA = -INFINITY;
  float maxB = -INFINITY;
  for (int i = 0; i < size / 2; i++) {  // Loop through the first half of the array to find the two largest values
    if (arr[i] > maxA) {                // If the current value is greater than the current maximum value
      maxB = maxA;                      // Shift the current maximum value down to the second maximum value
      maxA = arr[i];                    // Update the current maximum value to be the current value
      *indexMaxB = *indexMaxA;          // Update the index of the maximum value
      *indexMaxA = i;
    }
    else if (arr[i] > maxB) {           // If the current value is greater than the second maximum value
      maxB = arr[i];                    // Update the second maximum value
      *indexMaxB = i;                   // Update the index of the second maximum value
    }
  }
}

void initializeMatrix(float* mat, int length) {
  for (int i = 0; i < length; i++) {
    mat[i] = 0.0;
  }
}

// Calculate the periodogram of a signal
void calculateAveragedPeriodogram(float* mat, float* mat1, float* mat1I,
                     float* matrix_avg, float* matrix_avgI, 
                     int length, int step, int samplesize)
{
    int sampleCount = length / step;
    for (int i = 0; i < length; i += step) {
        // Calculate FFT of the current sample
        for (int j = 0; j < samplesize; j++) {
            mat1[j] = mat[i + j];
            mat1I[j] = 0.0;
        }
        FFT1D(mat1, mat1I, samplesize);

        // Add the magnitude of each frequency component to the running total
        for (int j = 0; j < samplesize; j++) {
            matrix_avg[j] += fabs(mat1[j]);
            matrix_avgI[j] += fabs(mat1I[j]);
        }
    }

    // Divide by the number of samples to get the averaged periodogram
    for (int j = 0; j < samplesize; j++) {
        matrix_avg[j] /= sampleCount;
        matrix_avgI[j] /= sampleCount;
    }
}

/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
  int i,j,k;
  int length;
  char BufSystVisuSig[100];
  int sample_size = 512;
  int step = sample_size/2; 

  //===============================
  //Question 3.2.
  //===============================
  float* Sign1 = LoadSignalDat("moteur2",&length);
  float* Samples = fmatrix_allocate_1d(sample_size);
  float* SamplesI = fmatrix_allocate_1d(sample_size);
  float* SampleAverages = fmatrix_allocate_1d(sample_size);
  float* SampleAveragesI = fmatrix_allocate_1d(sample_size);
  float* SampleAveragesM = fmatrix_allocate_1d(sample_size);

  // Calculs 
  initializeMatrix(SampleAverages, sample_size);
  initializeMatrix(SampleAveragesI, sample_size);
  calculateAveragedPeriodogram(Sign1, Samples, SamplesI, SampleAverages, SampleAveragesI, length - step, step, sample_size);

  ModVct(SampleAveragesM, SampleAverages, SampleAveragesI, sample_size);
  CenterVct(SampleAveragesM, sample_size);

  // Sauvegarde
  SaveSignalDat("FFT_Moteur2_PermMoy", SampleAveragesM, sample_size);
   
  //Visu
  strcpy(BufSystVisuSig,NAME_VISUALISER);
  strcat(BufSystVisuSig,"FFT_Moteur2_PermMoy.dat&");
  printf(" %s",BufSystVisuSig);
  system(BufSystVisuSig);

  //==End=========================================================
  //==============================================================

  //retour sans probleme
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}



// Note : Pour compiler ce programme, il faut utiliser la commande suivante sur le terminal :
// sudo apt-get install -y gnuplot 
// sudo apt-get install -y gv