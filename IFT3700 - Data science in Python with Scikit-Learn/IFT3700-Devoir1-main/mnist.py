import algorithmes
import math
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import csv

from sklearn import preprocessing
from sklearn.model_selection import train_test_split


# Fonction de similarité : Retourne similarité (0-1) entre x et y 
def fonction_similarite_mnist(x, y):

    # 0. Calculer le nombre de pixels dans chq quadrants  et le ratio perimeter/pixels total de x et y
    infosX = globalCounter(x)
    infosY = globalCounter(y)

    # 1.  Score 4 quadrants q's : 1 - abs{(q1) - (q2) / 196 pixels du quadrant} , Score assuré 0-1 (1 étant + similaire)
    lenQ = len(x) / 4
    scoreQ1 = 1 - (abs(infosX[0] - infosY[0]) / lenQ)
    scoreQ2 = 1 - (abs(infosX[1] - infosY[1]) / lenQ)
    scoreQ3 = 1 - (abs(infosX[2] - infosY[2]) / lenQ)
    scoreQ4 = 1 - (abs(infosX[3] - infosY[3]) / lenQ)
    scoreQ  = scoreQ1/4 + scoreQ2/4 + scoreQ3/4 + scoreQ4/4

    # 2. Score permietre :  1 - abs[{peri_1 / pixelsTotal} - {peri_2 / pixelTotal}], 0-1 encore
    scoreP = 1 - abs((infosX[4] / infosX[5]) - (infosY[4]) / infosY[5])


    # 3. Distance hamming entre x et y (combien de pixels faut changer pour passer de x à y)
    hamDist = 0
    for i in range(len(x)):
        if x[i] != y[i]:
            hamDist+=1
    hamDist = 1 - hamDist / len(x)  # score assuré entre 0-1

    # distance_euclid(quadrants_x quadrants_y) + distance_euclid(perim_x , perim_y) + distance_hamming(x,y)
    # On donne un poids plus important aux scores qui varient le plus.
    return (scoreQ * 0.15 + scoreP * 0.25 + hamDist * 0.60)


# Fonction de dissimilarité
def fonction_dissimilarite_mnist(x, y):
    return 1 - fonction_similarite_mnist(x, y)

# Fonction pour obtenir une matrice de similarite (manuellement) 
def get_fonction_dissimilarite_mnist_matrix(X, Y=None):
    Y = X if Y is None else Y
    dissim_Array = []

    for colI in range(len(X)):
        #print("loading %",int(colI/len(X) * 100))  # Too long? Use loading bar
        dissim_Col = []
        for ligneJ in range(len(Y)):
            dissim_Col.append(fonction_dissimilarite_mnist(X[colI], Y[ligneJ]))

        dissim_Array.append(dissim_Col)

    return np.array(dissim_Array)


# Pixels  : Split tab en 4 sous-tab et compter # pixels blancs (1) ds chq ,
# appel aussi perimetre counter
def globalCounter(array):
    lenght_4 = int(len(array) / 4)
    q1 = pixelCounter(array[lenght_4*0:lenght_4*1-1])  # 1st quarter (0:25%)
    q2 = pixelCounter(array[lenght_4*1:lenght_4*2-1])  # second (25%:50%)
    q3 = pixelCounter(array[lenght_4*2:lenght_4*3-1])  # third  (50%:75%)
    q4 = pixelCounter(array[lenght_4*3:lenght_4*4-1])  # forth  (75%:100%)

    total = q1 + q2 + q3 + q4
    perimeter = perimCounter(array)

    return q1, q2, q3, q4, perimeter, total

# Retourne Périmètre : Chq pixels blanc(1) vérifer s'il est sur frontière (au moins un pixel noir adjacent)
def perimCounter(fullArray):
    linelenght = int(math.sqrt(len(fullArray)))
    perim = 0

    for pixel in range(len(fullArray)):
        if (fullArray[pixel] == 1):
            # Verifier chq pixel adjacent, if we meet a 0 --> sur frontiere 
            # !Attention aux pixel trop proche d'extérimités (out of bounds)!
            if pixel<(len(fullArray)-1) and (fullArray[pixel + 1] == 0):
                perim += 1
            elif (fullArray[pixel - 1] == 0):
                perim += 1
            elif (fullArray[pixel - linelenght] == 0):
                perim += 1
            elif pixel<(len(fullArray)-linelenght) and (fullArray[pixel + linelenght] == 0):
                perim += 1
    return perim


# Retourne nbr pixels noirs ds fracArray
def pixelCounter(fracArray):
    count = 0
    for pixel in range(len(fracArray)):
        if fracArray[pixel] == 1:
            count+=1
    return count


# Jeu de données 

# 1. Importer
data = open('data/mnist.csv')
csv_file = csv.reader(data)
data_points = []

for row in csv_file:
    data_points.append(row)

data_points.pop(0)
data.close()

# 2. Caster
for i in range(len(data_points)):
    for j in range(0,785):
            data_points[i][j] = int(data_points[i][j])

# 3. Avec Étiquettes
y_train = []
for row in data_points:
    y_train.append(row[0])

# 4. Sans étiquettes
x_train = []
for row in data_points:
    x_train.append(row[1:785])

# 5. Convertir pixels en blanc & noir
for i in range(len(x_train)):
    for j in range(len(x_train[0])):
        if x_train[i][j] != 0 :
            x_train[i][j] = round(int(x_train[i][j]) / 255.0)


# Division des données
X_train, X_test, Y_train, Y_test = train_test_split(x_train, y_train,
                                                    train_size=0.025,test_size=0.008, stratify=y_train, random_state=0)

# Creation des matrices de dissimilarité (attendre environ 1-2 minutes selon puissance pc)
train_matrice_dissimilarite = get_fonction_dissimilarite_mnist_matrix(X_train)
test_matrice_dissimilarite = get_fonction_dissimilarite_mnist_matrix(X_test, X_train)


# Fonction qui lance les algorithmes avec le dataset MNIST
def run_mnist():
    print('Lancement des algorithmes avec le dataset MNIST')

    """kmd(nbClusters, train_matrice, test_matrice)"""
    algorithmes.kmd(5, train_matrice_dissimilarite, test_matrice_dissimilarite)

    """isomap(voisins, train_matrice, test_matrice)"""
    algorithmes.isomap(4, train_matrice_dissimilarite, test_matrice_dissimilarite)

    """pcoa(voisins, train_matrice, test_matrice)"""
    algorithmes.pcoa(4, train_matrice_dissimilarite, test_matrice_dissimilarite)

    """hierarchique(clusters, train_matrice, test_matrice)"""
    algorithmes.hierarchique(4, train_matrice_dissimilarite, test_matrice_dissimilarite)

    """knn(voisins, train_matrice, test_matrice, y_train, y_test)"""
    algorithmes.knn(4, train_matrice_dissimilarite, test_matrice_dissimilarite, Y_train, Y_test)

    print('Fin du roulement des algorithmes avec le dataset mnist')