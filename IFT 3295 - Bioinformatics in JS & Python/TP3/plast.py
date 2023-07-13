import argparse
import numpy as np
import math
from FastaFile import FastaFile

# Nous utilisons cette fonction pour imprimer la sortie du programme dans la console (Comme montré dans le tp)
# Argument: | header : en-tête dans tRNAs.fasta | result : meilleur hsp retenu | input : séquence recherché |tRNAs.fasta
def prettyPrinter(header, result , input, fasta):
    print(header)
    print("# Best HSP score:", result[6], ", bitscore: ", bitScore(result[6]), ", evalue: ",
          eValue(len(fasta.getSequence(result[1])),len(input),bitScore(result[6])))
    print(result[2], input[result[2]:(result[3]) + 1], result[3])
    print(result[4], fasta.getSequence(result[1])[result[4]:(result[5]) + 1], result[5],"\n")



#  1.6
#
# Statistique sur les HSPs et cutoff ( Implémentation simple des formules bitScore et eValue)
def bitScore(score):
    return round((0.192 * score - math.log(0.176)) / math.log(2))

def eValue(m, n, b):
    return m * n * (2 ** -b)



# Cette fonction parcourt toutes nos sous-listes et retourne le hsp avec le meilleure score brute possible.
# Puisque nous n'avons malheureusement pas eu le temps de programmer merge, nous devons parcourir plusieurs sous-listes
# imbriquées. Les sous-listes représentent toutes les extensions possibles de chaque kmer dans chaque séquence rna.fasta
def getBestHsp(hspList):
    if (len(hspList) == 0):
        return None
    bestScore = -1
    bestHsp = []
    for hsp in hspList:
        for item in hsp:
            for i in item:
                if (len(i) > 0):
                    if i[6] > bestScore:
                        bestHsp = i
                        bestScore = i[6]
    return bestHsp



# 1.5
#
# Cette fonction est supposé prendre en argument une liste d'hsp et les fusionner avant de les retourner. TODO
def mergeHsp(hspList, inputWord, fastaWord):
    i = 0
    j = 0 
    while (i < len(hspList)):
        break
        while (j < len(hspList)):
            if (i != j):
                Fasta1Start = hspList[i][4]
                Fasta1End = hspList[i][5]
                Fasta2Start = hspList[j][4]
                Fasta2End = hspList[j][5]
                Input1Start = hspList[i][2]
                Input1End = hspList[i][3]
                Input2Start = hspList[j][2]
                Input2End = hspList[j][3]
                # TODO CONDITIONS
    return hspList


# 1.4
#
#   Heuristique gloutonne pour l'extension des HSPs
#   Cette fonction est un peu longue, donc prenons le temps de l'expliquer en détails.
#
#   * Argument :
#       kmerInput s'agit d'un kmer généré à partir de la séquence recherchée | kmerIndex : l'index de ce kmer dans seq
#       input : s'agit de la séquence recherchée    |  word s'agit d'une séquence dans tRNAs.fasta |
#       wordIndex : index de la séquence dans tRNAs.fasta | seed : "11111111111" | e : 4
#
#   Logique : 1.  Trouver des similarités entre le kmerInput dans la séquence de tRNAs.fasta.
#             2.  Parcourir ces occurrences afin d'effecuter les extensions
#
#             Extension : Avant d'extendre, il faut d'abord vérifier à gauche et à droite de ne pas dépasser.
#                         Et si on ne dépasse pas, il faut s'assurer de ne pas déscendre le score en bas du seuil voulu.
#
#             On s'arret lorsqu'on peut plus faire d'extension à droite ET à gauche.
#             Sinon on fait l'extension. et on continue.
#
def extHsp(kmerInput, kmerIndex, input, word, wordIndex, seed, e):

    match = 5
    missmatch = -4
    seedLen = seed.count('1')
    indexList = findWordIndexBrut(kmerInput, word, seed)  # Trouver kmer (généré par input) dans une séquence RNAs.fasta
    result = []


    for i in range(len(indexList)):                       # parcourir chacunes des occurrences

        inputStart = kmerIndex
        inputEnd = kmerIndex + len(kmerInput) - 1         # début et fin initiales de l'input.

        wordStart = indexList[i]
        wordEnd = indexList[i] + len(kmerInput) - 1       # début et fin initiales de la séquence RNAs.fasta

        scoreMax = seedLen * match                        # définir le score actuel et max avec score_match * len(seed)
        score = scoreMax

        done = False                                      # On s'arrêt lorsqu'on ne peut plus extendre à gauche ET droit
        while not done:

            # ************************** PARTIE DROITE **********************************
            scoreRight = -float("Inf")
            if (inputEnd + 1 != len(input) and wordEnd + 1 != len(word)):  # On s'assure de ne pas dépasser à droite
                if input[inputEnd + 1] == word[wordEnd + 1]:
                    scoreRight = match
                else:
                    scoreRight = missmatch
            if not (score + scoreRight - scoreMax > -e):                  # On s'assure de respecter le seuil
                scoreRight = -float("Inf")

            # ************************** PARTIE GAUCHE **********************************
            scoreLeft = -float("Inf")                                      # On s'assure de ne pas dépasser à gauche
            if (inputStart != 0 and wordStart != 0):
                if (input[inputStart - 1] == word[wordStart - 1]):
                    scoreLeft = match
                else:
                    scoreLeft = missmatch
            if not (score + scoreLeft - scoreMax > -e):                    # On s'assure de respecter le seuil
                scoreLeft = -float("Inf")

            sumScoreLR = scoreLeft + scoreRight                             # Score de droite + gauche.

            #  On s'arrêt si on ne peut plus extendre à gauche ET à droite.
            if (scoreLeft == -float("Inf") and scoreRight == -float("Inf")):
                result.append([kmerIndex, wordIndex, inputStart, inputEnd, wordStart, wordEnd, score])
                done = True
                break

            # Sinon on continue et on extend soit à gauche, soit à droite, soit les deux.
            else:
                scores = np.array([scoreLeft, scoreRight, sumScoreLR])
                maxS = np.amax(scores)
                maxIndex = np.argmax(scores)

                score+= maxS
                scoreMax = max(scoreMax, score)        # Mise à jour du score

                if (maxIndex == 0):                    # Mise à jour de début/fin DROITE
                    inputStart-= 1
                    wordStart-= 1

                elif (maxIndex == 1):                  # Mise à jour de début/fin GAUCHE
                    inputEnd+= 1
                    wordEnd+= 1

                else:                                  # Mise à jour de début/fin DES DEUX
                    inputEnd+= 1
                    wordEnd+= 1
                    inputStart-=1
                    wordStart-= 1
    return result




# 1.3
#
# Algorithme naïf qui prend en argument le kmer shouaité, la séquence dans laquelle on veut chercher le kmer et
# seed voulu. En tenant en compte le seed, on parcours la séquence au complet à la recherche des occurences du kmer.
def findWordIndexBrut(kmers, word, seed):
    result = []
    if seed.count('1') == len(kmers):
        for i in range(len(word) - len(kmers) + 1):
            found = True
            for j in range(len(kmers)):
                if (word[i+j] != kmers[j]) and (seed[j] == '1'):
                    found = False
                    break
            if found:
                result.append(i)
    return result



# 1.2
#
# Génère simplement tous les kmers possible en pacourant la séquence voulu.
def getKmers(sequence, seed):
    kmers = []
    for i in range(0, len(sequence) - len(seed) + 1):
        kmers.append(sequence[i:i + len(seed)])
    return kmers


# 1.1
#
# Logique principale du programme.
if __name__ == "__main__":
    # Pycharm : Add {-i "CGTAGTCGGCTAACGCATACGCTTGATAAGCGTAAGAGCC" -db "tRNAs.fasta"} to parameters in Run Config.
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', help="Input sequence", type=str, required=True)
    parser.add_argument('-db', help="Fasta Path", type=str, required=True)
    parser.add_argument('-E', help="Threshold test score", type=float, default=4, required=False)
    parser.add_argument('-ss', help="...", type=float, required=False)
    parser.add_argument('-seed', help="String of 0 & 1", type=str, required=False, default="11111111111")

    args = parser.parse_args()
    SEQUENCE = args.i
    fasta = FastaFile(args.db)
    SEUIL1 = args.E
    SEUIL2 = args.ss
    SEED = args.seed

    kmers = getKmers(SEQUENCE, SEED)
    hspList = []
    for i in range(len(fasta)):
        temp = []
        for j in range(len(kmers)):
            result = extHsp(kmers[j], j, SEQUENCE, fasta.getSequence(i), i, SEED, SEUIL1)
            if len(result) > 0:
                temp.append(result)

                for l in range(len(result)):
                    pass
        hspList.append(temp)
    #bestHsp = mergeHsp(hspList,SEQUENCE,fasta)
    bestHsp = getBestHsp(hspList)
    if bestHsp:
        prettyPrinter(fasta.getHeader(bestHsp[1]), bestHsp, SEQUENCE, fasta)
        print("----------------------------------------")
        print("Total : 1")                                      # Sorry we couldn't make merge work :(
    else:
        print("Total 0")




    ######################################################{ TEST }######################################################

     # fasta tests
    debug = False                                               #  TODO SET TO TRUE TO DEBUG !!!
    if (debug):
        print("\n------------------------------ TEST 1.1")
        print("Ouverture du fichier fasta et affichage de la première séquence : ")
        print(fasta.getHeader(0))
        print(fasta.getSequence(0))

        # kmers test
        print("\n------------------------------ TEST 1.2")
        print("Génération de kmers de seed de taille 11 de cette première séquence: ")
        print(getKmers(fasta.getSequence(0), SEED))


        # test research
        print("\n------------------------------ TEST 1.3")
        print("Recherche de la séquence (input) dans le fichier fasta:")
        kmers = getKmers(SEQUENCE, SEED)
        for seq in fasta.getSequences():
            for kmer in kmers:
                r = findWordIndexBrut(kmer, seq, SEED)
                if r:
                    print("Input:", SEQUENCE)
                    print("Kmer: ", kmer)
                    print("Séquence fasta: ",seq)
                    print("Indexe de kmer dans fasta: ",r)

        # test extension
        print("\n------------------------------ TEST 1.4")
        for i in range(len(fasta)):
            for j in range(len(kmers)):
                result = extHsp(kmers[j], j, SEQUENCE, fasta.getSequence(i), i, SEED, SEUIL1)
                if len(result) > 0:
                   print("Extension de la séquence voulue dans une séquence fasta: ")
                   print("([kmerIndex, wordIndex, wordOfKmerStart, wordOfKmerEnd, wordStart, wordEnd])")
                   print(result)

                   print("kmer: ", kmers[result[0][0]])
                   print("full kmer: ", SEQUENCE)
                   print("fasta seq: ", fasta.getSequence(result[0][1]))
                   print("-------")
                   print("Score: ", result[0][6])
                   print(fasta.getHeader(i))
                   print(result[0][2],SEQUENCE[result[0][2]:(result[0][3])+1],result[0][3])
                   print(result[0][4],fasta.getSequence(result[0][1])[result[0][4]:(result[0][5])+1],result[0][5])

        # test de bitscore et evalue sur toutes les séquences
        print("\n------------------------------ TEST 1.5")
        print("Algorithme complet sur toutes les séquences dans unknown.fasta:")
        inputfasta = FastaFile("unknown.fasta")
        for INPSEQ in inputfasta.getSequences():
            kmers = getKmers(INPSEQ, SEED)
            hspList = []
            for i in range(len(fasta)):
                temp = []
                for j in range(len(kmers)):
                    result = extHsp(kmers[j], j, INPSEQ, fasta.getSequence(i), i, SEED, SEUIL1)
                    if len(result) > 0:
                        temp.append(result)

                        for l in range(len(result)):
                            pass
                hspList.append(temp)
            bestHsp = getBestHsp(hspList)
            prettyPrinter(fasta.getHeader(bestHsp[1]),bestHsp,INPSEQ, fasta)
    ####################################################################################################################