//------------------------------------------------------
// Prog    : Tp5_IFT3205_2-3
// Auteur  : Tabta Abderrahim (20133680) et Maxime Ton (20143044)
// Date    :
// version :
// langage : C
// labo    : DIRO
//------------------------------------------------------

//------------------------------------------------
// FICHIERS INCLUS -------------------------------
//------------------------------------------------
#include "FonctionDemo5.h"

//------------------------------------------------
// DEFINITIONS -----------------------------------
//------------------------------------------------
#define NAME_VISUALISER "display "
#define NAME_IMG_IN "lena512"
#define NAME_IMG_OUT "image-TpIFT3205-2-3"
#define NAME_IMG_DEG "lena512_Degraded"

//------------------------------------------------
// PROTOTYPE DE FONCTIONS  -----------------------
//------------------------------------------------
//----------------------------------------------------------
// copy matrix
//----------------------------------------------------------
void copy_matrix(float **mat1, float **mat2, int lgth, int wdth)
{
  int i, j;

  for (i = 0; i < lgth; i++)
    for (j = 0; j < wdth; j++)
      mat1[i][j] = mat2[i][j];
}

//------------------------------------------------
// CONSTANTES ------------------------------------
//------------------------------------------------
#define SIGMA_NOISE 30

const int blockSize = 8;
const int num_shifts = 64;

//------------------------------------------------
//------------------------------------------------
// FONCTIONS  ------------------------------------
//------------------------------------------------
//------------------------------------------------

// Legacy code from Tp4 that was used to initialize a matrix with 0's
void initializeMatrixWithZeros(float **matrix, int rows, int columns)
{
  // initialize all elements of matrix to 0.0
  for (int i = 0; i < rows; ++i)
  {
    for (int j = 0; j < columns; ++j)
    {
      matrix[i][j] = 0.0f;
    }
  }
}

// Copy a section of a matrix to another matrix
void copySectionMatrix(float **sourceMatrix, float **targetMatrix, int option, int startRow, int startCol, int numRows, int numCols)
{
  int i, j;
  for (i = 0; i < blockSize; i++)
  {
    for (j = 0; j < blockSize; j++)
    {
      if (option)
      {
        targetMatrix[i][j] = sourceMatrix[(i + startRow) % numRows][(j + startCol) % numCols];
      }
      else
      {
        targetMatrix[(i + startRow) % numRows][(j + startCol) % numCols] = sourceMatrix[i][j];
      }
    }
  }
}

// Cancel the coefficients of a matrix
void cancelCoefficients_N(float **matrix, int threshold){
  int count = 0;
  int direction = 0;

  // Loop over the upper triangle of the matrix
  for (int i = 0; i < blockSize; i++)
  {
    for (int j = i; j >= 0; j--)
    {
      if (count >= threshold)
      {
        if (direction % 2 != 0)
        {
          // Zero out the coefficient in the anti-diagonal direction
          matrix[i - j][j] = 0;
        }
        else
        {
          // Zero out the coefficient in the diagonal direction
          matrix[j][i - j] = 0;
        }
      }
      count++;
    }
    direction++;
  }

  // Loop over the lower triangle of the matrix
  for (int i = 1; i < blockSize; i++)
  {
    for (int j = i, j2 = blockSize - 1; j < blockSize; j++, j2--)
    {
      if (count >= threshold)
      {
        if (direction % 2 == 0)
        {
          // Zero out the coefficient in the diagonal direction
          matrix[j2][j] = 0;
        }
        else
        {
          // Zero out the coefficient in the anti-diagonal direction
          matrix[j][j2] = 0;
        }
      }
      count++;
    }
    direction++;
  }
}

// Cancel the coefficients of a matrix that are less than or equal to a given threshold value.
void cancelCoefficients_T(float **matrix, int threshold)
{
  for (int i = 0; i < blockSize; i++)
  {
    for (int j = 0; j < blockSize; j++)
    {
      if (abs(matrix[i][j]) <= threshold)
      {
        matrix[i][j] = 0;
      }
    }
  }
}

// Denoise an input matrix by applying a DCT transformation to 8x8 blocks of the matrix, cancelling coefficients less than
// or equal to a given threshold, and applying an inverse DCT transformation to the resulting matrix.
void denoise(float **target_matrix, float **matrix, float **bloc_8x8, int length, int width, int n, int offset_i, int offset_j, int option)
{
  int iter = 8;
  for (int i = offset_i; i < length; i += iter)
  {
    for (int j = offset_j; j < width; j += iter)
    {
      copySectionMatrix(matrix, bloc_8x8, 1, i, j, length, width);
      ddct8x8s(-1, bloc_8x8);
      if (option){cancelCoefficients_N(bloc_8x8, n);} // For 2-2
      else { cancelCoefficients_T(bloc_8x8, n); } // For 2-4 and 2-5
      ddct8x8s(1, bloc_8x8);
      copySectionMatrix(bloc_8x8, target_matrix, 0, i, j, length, width);
    }
  }
}

int findbestThreshold_T(float **result, float **mat, float **img, float **bloc_8x8, int length, int width, int offset_i, int offset_j, int max_iter)
{
  int best_treshold = 0;
  float min_mse = INFINITY;
  float mse;
  int t;

  // Look for the best possible treshold
  for (t = 0; t < max_iter; t++)
  {

    // denoise the image with the current treshold
    denoise(result, mat, bloc_8x8, length, width, t, offset_i, offset_j, 0);

    // compare and keep the best treshold
    mse = computeMMSE(result, img, length);
    if (mse < min_mse)
    {
      min_mse = mse;
      best_treshold = t;
    }
  }

  // Denoise the image with the best treshold
  denoise(result, mat, bloc_8x8, length, width, best_treshold, offset_i, offset_j, 0);
  return best_treshold;
}

//---------------------------------------------------------
//---------------------------------------------------------
// PROGRAMME PRINCIPAL   ----------------------------------
//---------------------------------------------------------
//---------------------------------------------------------
int main(int argc, char **argv)
{
  int length, width;
  char BufSystVisuImg[NBCHAR];

  //>Lecture Image
  float **Img = LoadImagePgm(NAME_IMG_IN, &length, &width);

  //>Allocation memory
  float **ImgDegraded = fmatrix_allocate_2d(length, width);
  float **ImgDenoised = fmatrix_allocate_2d(length, width);
  float **bloc_8x8 = fmatrix_allocate_2d(8, 8);

  initializeMatrixWithZeros(ImgDenoised, length, width);
  initializeMatrixWithZeros(bloc_8x8, 8, 8);

  //>Degradation
  copy_matrix(ImgDegraded, Img, length, width);
  add_gaussian_noise(ImgDegraded, length, width, SIGMA_NOISE * SIGMA_NOISE);


  printf("\n\n  Info Noise");
  printf("\n  ------------");
  printf("\n    > MSE = [%.2f]", computeMMSE(ImgDegraded, Img, length));

  //=========================================================
  //== PROG =================================================
  //=========================================================

  // .....
  int best_t = findbestThreshold_T(ImgDenoised, ImgDegraded, Img, bloc_8x8, length, width, 0, 0, 350);
  printf("\n------------\n> Best T = %d\n------------",best_t);

  //---------------------------------------------
  // SAUVEGARDE
  // -------------------
  // L'image d�grad�e             > ImgDegraded
  // Le resultat du debruitage    > ImgFiltered
  //----------------------------------------------
  SaveImagePgm(NAME_IMG_DEG, ImgDegraded, length, width);
  SaveImagePgm(NAME_IMG_OUT, ImgDenoised, length, width);

  //>Visu Img
  strcpy(BufSystVisuImg, NAME_VISUALISER);
  strcat(BufSystVisuImg, NAME_IMG_IN);
  strcat(BufSystVisuImg, ".pgm&");
  printf("\n > %s", BufSystVisuImg);
  system(BufSystVisuImg);

  // Visu ImgDegraded
  strcpy(BufSystVisuImg, NAME_VISUALISER);
  strcat(BufSystVisuImg, NAME_IMG_DEG);
  strcat(BufSystVisuImg, ".pgm&");
  printf("\n > %s", BufSystVisuImg);
  system(BufSystVisuImg);

  // Visu ImgFiltered
  strcpy(BufSystVisuImg, NAME_VISUALISER);
  strcat(BufSystVisuImg, NAME_IMG_OUT);
  strcat(BufSystVisuImg, ".pgm&");
  printf("\n > %s", BufSystVisuImg);
  system(BufSystVisuImg);

  //--------------- End -------------------------------------
  //----------------------------------------------------------

  // Liberation memoire pour les matrices
  if (Img)
    free_fmatrix_2d(Img);
  if (ImgDegraded)
    free_fmatrix_2d(ImgDegraded);
  if (ImgDenoised)
    free_fmatrix_2d(ImgDenoised);

  // Return
  printf("\n C'est fini... \n");
  ;
  return 0;
}

//----------------------------------------------------------
// Allocation matrix 3d float
// --------------------------
//
// float*** fmatrix_allocate_3d(int dsize,int vsize,int hsize)
// > Alloue dynamiquement un tableau 3D [dsize][vsize][hsize]
//
//-----------------------------------------------------------

//----------------------------------------------------------
//  ddct8x8s(int isgn, float** tab)
// ---------------------------------
//
// Fait la TCD (sgn=-1) sur un tableau 2D tab[8][8]
// Fait la TCD inverse (sgn=1) sur un tableau 2D tab[8][8]
//
//-----------------------------------------------------------
