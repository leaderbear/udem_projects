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

void canal(float* input, float* output, int length){
  float _const = 7.0/3.0;
  output[0] = input[0];
  output[1] = input[1]-_const*input[0];
  for(int i = 2; i<length; i++){
    output[i] = input[i]- _const * input[i-1] + (2.0/3.0) * input[i-2];
    }
}

float calculateRSB(float* sign, float* can, int length) {
    float num = 0.0f;
    float denum = 0.0f;

    for (int i = 0; i < length; ++i) {
        num += sign[i] * sign[i];
        float diff = sign[i] - can[i];
        denum += diff * diff; 
    }

    float rms_orig = sqrtf(num / length);
    float rms_diff = sqrtf(denum / length);

    return 20.0f * log10f(rms_orig / rms_diff);
}

void canal_restore(float* inputSignal, float* outputSignal, int length) {
    const float a = 7.0/3.0;
    const float b = 2.0/3.0;
    const float c = 17.0/6.0;
    const float d = 11.0/6.0;
    const float e = 1.0/3.0;

    outputSignal[0] = -inputSignal[0]/2.0;
    outputSignal[1] = (-inputSignal[1] + a*inputSignal[0])/2.0;
    outputSignal[2] = (-inputSignal[2] + a*inputSignal[1] - b*inputSignal[0])/2.0;

    for (int i = 3; i < length; i++) {
        outputSignal[i] = (-inputSignal[i] + a*inputSignal[i-1] - b*inputSignal[i-2]
                          + c*outputSignal[i-1] - d*outputSignal[i-2] + e*outputSignal[i-3])/2.0;                          
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
  //Question 3-4
  //===============================

  // Load the signal
  float*  Signal = LoadSignalDat("SoundFileInCanal",&length);
  float* SignalRestored=fmatrix_allocate_1d(length);
 
  // Calculate
  canal_restore(Signal, SignalRestored, length);
  float result = calculateRSB(Signal, SignalRestored, length);

  // Show the results
  printf("\n----------------\nRSB = %f\n----------------", result);

  // Save the restored signal
  SaveSignalDatWav("SoundFileInCanalRestor",SignalRestored,length, 11025.0);
  SaveSignalDat("SoundFileInCanalRestor",SignalRestored,length);

  //Visu Ecran
  strcpy(BufSystVisuSig,NAME_VISUALISER);
  strcat(BufSystVisuSig,"SoundFileInCanalRestor.dat&");
  printf(" %s",BufSystVisuSig);
  system(BufSystVisuSig);
  //==End====================

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


