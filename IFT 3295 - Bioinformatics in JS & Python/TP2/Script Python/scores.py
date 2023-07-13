import re

# Cette partie du code s'occupe seulement de décoder le fichier BLOSUM.
# D'abord, on lit le fichier ligne par ligne. Ensuite on enlève les espace.
# Enfin, on joue un peu avec les strings pour tout mettre dans des listes.

# On utilise une fonction qui permet de trouver le score entre deux lettres
# En utilisant nos listes.




# open file & remove spaces from first line
blosumPath = "BLOSUM62.txt"
lines = []
with open(blosumPath) as file:
    for line in file:
        lines.append(line)
lines[0] = lines[0].replace(" ", "").replace("\n", "")

# get every char from first line
global char_list
char_list = []
for char in lines[0]:
    char_list.append(char)

# get every number from every line
global blosumList
blosumList = []
for line in lines[1::]:
    tempList = re.split(r'\s+', line[1::])
    tempList.pop(0)
    tempList.pop()
    blosumList.append(tempList)


def getScore(a, b):
    index_a = char_list.index(a)
    index_b = char_list.index(b)
    return int(blosumList[index_a][index_b])
