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

    // Compute the remaining output samples using a recursive formula
    for (int i = 2; i < signalLength; i++) {
        float currentInputSample = inputSignal[i];
        float newOutputSample = currentInputSample - prevInputSample + 2 * filterCoefficient * cos(filterAngle) * prevOutputSample - filterCoefficient * filterCoefficient * outputSignal[i-2];
        outputSignal[i] = newOutputSample;
        prevInputSample = currentInputSample;
        prevOutputSample = newOutputSample;
    }
}

// Function that adds noise to a signal
void degradeSignalWithNoise(float* inputSignal, float rate, float noiseCoefficient, int signalLength) {
    float constante = noiseCoefficient/rate * 2 * PI;
    for (int i = 0; i < signalLength; i++) {
         inputSignal[i] = inputSignal[i] + sin(constante * i);
    }
}

// Function that restores a signal
void restore(float* restore, float* degrad, int length, float rho, float theta) {
  // First two elements of the restore array are calculated using the first two elements of the degrad array
  restore[0] = degrad[0];
  restore[1] = degrad[1] - 2 * cos(theta) * degrad[0] + 2 * rho * cos(theta) * restore[0];

  // Iterate through the remaining elements of the restore array
  for(int i = 2; i < length; i++) {
    float prevRestore = restore[i-1];
    float prevPrevRestore = restore[i-2];
    float prevDegrad = degrad[i-1];
    float prevPrevDegrad = degrad[i-2];

    // Calculate the current element of the restore array using the previous two elements of the restore and degrad arrays
    restore[i] = degrad[i]- 2 * cos(theta) * prevDegrad + prevPrevDegrad 
                + 2 * rho * cos(theta) * prevRestore - (rho * rho) * prevPrevRestore;
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
  //Question 2-3
  //===============================
   float* Signal=LoadSignalDat("SoundFileDeg",&length);
   float* SignalRestored=fmatrix_allocate_1d(length);

  
  //restoreSignal(Signal, SignalRestored, length);
  float t = 2 * PI * (500.0/11025.0);
  restore(SignalRestored, Signal, length, 0.99, t);

   //Sauvegarde en fichier .dat
   SaveSignalDatWav("SoundFileRest",SignalRestored,length, 11025.0);
   SaveSignalDat("SoundFileRest",SignalRestored,length);

   //Visu Ecran
   strcpy(BufSystVisuSig,NAME_VISUALISER);
   strcat(BufSystVisuSig,"SoundFileRest.dat&");
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


