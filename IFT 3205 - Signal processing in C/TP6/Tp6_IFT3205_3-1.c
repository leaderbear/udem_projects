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


/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
  int i,j,k;
  int length;
  char BufSystVisuSig[100];

  //===============================
  //Question 3.1.
  //===============================
  float*  Sign1=LoadSignalDat("moteur2",&length);
  float*  Sign1I=fmatrix_allocate_1d(length);
  float*  Sign1M=fmatrix_allocate_1d(length); 
  FFT1D(Sign1,Sign1I,length);
  ModVct(Sign1M,Sign1,Sign1I,length);
  CenterVct(Sign1M,length);

  //Sauvegarde
  SaveSignalDat("FFT_Moteur2",Sign1M,length);  
   
  //Visu
  strcpy(BufSystVisuSig,NAME_VISUALISER);
  strcat(BufSystVisuSig,"FFT_Moteur2.dat&");
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