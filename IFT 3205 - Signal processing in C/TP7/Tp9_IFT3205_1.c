//------------------------------------------------------
// Prog    : Tp4_IFT3205-7-1                   
// Auteur  : Tabta Abderrahim (20133680) et Maxime Ton (20143044)                                    
// Date    :                                  
// version :                                             
// langage : C                                          
// labo    : DIRO                                       
//------------------------------------------------------

/*------------------------------------------------*/
/* FICHIERS INCLUS -------------------------------*/
/*------------------------------------------------*/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "FonctionDemo9.h"

/*------------------------------------------------*/
/* DEFINITIONS -----------------------------------*/   
/*------------------------------------------------*/
#define NAME_VISUALISER_IMG "./display "
#define NAME_VISUALISER     "./ViewSig.sh "

/*------------------------------------------------*/
/* PROTOTYPE DE FONCTIONS  -----------------------*/   
/*------------------------------------------------*/

// Function to compute the output of an IIR filter
void IRFilter(float* inputSignal, float* outputSignal, int signalLength, float filterCoefficient, float filterAngle) {
    // Initialize the first output sample to be equal to the first input sample
    outputSignal[0] = inputSignal[0];

     // Compute the second output sample
    float prevInputSample = inputSignal[0];
    float prevOutputSample = outputSignal[0];
    outputSignal[1] = inputSignal[1] - prevInputSample + 2 * filterCoefficient * cos(filterAngle) * prevOutputSample;
    prevInputSample = inputSignal[1];
    prevOutputSample = outputSignal[1];

    // Compute the remaining output samples
    for (int i = 2; i < signalLength; i++) {
        float currentInputSample = inputSignal[i];
        float newOutputSample = currentInputSample - prevInputSample + 2 * filterCoefficient * cos(filterAngle) * prevOutputSample - filterCoefficient * filterCoefficient * outputSignal[i-2];
        outputSignal[i] = newOutputSample;
        prevInputSample = currentInputSample;
        prevOutputSample = newOutputSample;
    }
}

/*------------------------------------------------*/
/* PROGRAMME PRINCIPAL   -------------------------*/                     
/*------------------------------------------------*/
int main(int argc,char **argv)
 {
  int i,j;
  char BufSystVisuSig[100];
  int length;

  //===============================
  //Question 1
  //===============================

   length=256;

   float*  SignX=fmatrix_allocate_1d(length); 
   float*  SignY=fmatrix_allocate_1d(length);
   
   //Signal d'entr�  x(n) al�atoire compris entre [0::200]
   for(i=0;i<length;i++) SignX[i]=(int)(((float)rand()/RAND_MAX)*200.0);

   // Calcul du signal de sortie y(n) à partir de x(n)
   IRFilter(SignX, SignY, length, 0.99, PI/8);
  
   //Sauvegarde en fichier .dat
   SaveSignalDat("SignalY", SignY, length); 

   //Visu Ecran
   strcpy(BufSystVisuSig,NAME_VISUALISER);
   strcat(BufSystVisuSig,"SignalY.dat&");
   printf(" %s",BufSystVisuSig);
   system(BufSystVisuSig);


   //---------------------------------------------
   //Sauvegarde de SignZ (30000 echantillons 
   //al�atoire entre [0::200]) dans un 
   //fichier .wav avec une periode 
   //d'echantillonnage de 10000: Nb echant/secondes
   //(pour 3 secondes d'�coute)
   //----------------------------------------------
   //----------------------------------------------
   if (1)
      {
       length=30000;
       float*  SignZ=fmatrix_allocate_1d(length);
       for(i=0;i<length;i++) SignZ[i]=(int)(((float)rand()/RAND_MAX)*200.0);
       SaveSignalDatWav("SignalZ",SignZ,length,10000);
      }
     

     //Rappel -Pour Lecture/Sauvegarde/Visu [Fichier Son]
     //=================================================
     //float*  SignIn=LoadSignalDat("NameSignalIn",&length);
     //SaveSignalDat("NameSignalOut",SignIn,length); 
     //strcpy(BufSystVisuSig,NAME_VISUALISER);
     //strcat(BufSystVisuSig,"NameSignalOut.dat&");
     //printf(" %s",BufSystVisuSig);
     //system(BufSystVisuSig);
  
 
  //==End=========================================================

  //retour sans probleme
  printf("\n C'est fini ... \n\n");
  return 0; 	 
}


