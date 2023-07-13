# On a crée cette classe pour simplifier l'ouverture des fichiers fasta
# Cela dit, nous fenaisons pas la gestion d'erreurs puisque ce n'est pas
# demandé dans l'énoncé. Donc, il faut s'assurer que les fichiers soient
# dans le bon emplacment.

class FastaFile:
    def __init__(self, path):
        self.__path = path
        self.__headers = []
        self.__sequences = []


        file = open(self.__path, "r")
        for line in file:
            if (line[0] == ">"):
                self.__headers.append(line[1:-1])
            else:
                self.__sequences.append(line[:-1])

        file.close()

    def getSequences(self):
        return self.__sequences

    def getSequence(self, i):
        return self.__sequences[i]

    def getHeader(self, i):
        return self.__headers[i]


    def __len__(self):
        return len(self.__headers)