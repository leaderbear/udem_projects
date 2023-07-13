# abderrahim_tabta skander_ben_ahmed
# Date: 16/10/20
# But du code : résourdre un labyrinthe donné sous la forme d'un string de taille carrée (d^2) et d'Σ : {# : mur ,
# D : début, F : fin , 0 : chemin}.

class Laby(object):
    def __init__(self, lab, dim):
        self.lab = lab
        self.dim = dim
        pass

    # Explication : Dans cette fonction, il ya trois sous fonctions qui vont êtres appelées afin de résoudre le laby
    # Le corps procédurale de cette fonction se trouve à la ligne 100
    def solve(self):

        # Sous-Fonction principale récursive pour savoir si le bout du chemin qu'on est en train de parcourir est "F"
        # Return True si la suite (de la suite de la suite ..) d'un index est F .Sinon, c'est une impasse ,return False.
        # Le 3ème argument "reponse" est une pile globale de slove() dans laquelle on va empiler le chemin valide
        def trouverF(actuel, prev, reponse):

            # Variables donnant les valeurs des index possibles à suivre après l'index actuel
            droite, gauche = actuel + 1, actuel - 1
            haut = actuel - int(self.dim)
            bas = actuel + int(self.dim)

            # Cas de Base 1:  Impasse lorsque tous les chemins autour de l'index actuel ne sont pas valides à suivre
            if ((droite == prev) or (self.lab[droite] == "#")) and ((gauche == prev) or (self.lab[gauche] == "#")) and (
                    (haut == prev) or (self.lab[haut] == "#")) and ((bas == prev) or (self.lab[bas] == "#")):
                return False

            # Cas de Base 2 : Solution (F) du labyrinthe trouvée.
            else:
                if self.lab[droite] == "F":
                    reponse.push(droite)
                    reponse.push(actuel)
                    return True

                if self.lab[gauche] == "F":
                    reponse.push(gauche)
                    reponse.push(actuel)
                    return True

                if self.lab[haut] == "F":
                    reponse.push(haut)
                    reponse.push(actuel)
                    return True

                if self.lab[bas] == "F":
                    reponse.push(bas)
                    reponse.push(actuel)
                    return True

                # Étape récursive : Ici, si la suite (de la suite de la suite ..) d'un chemin à partir de l'index actuel mène
                # mène à F, alors ajouter à la Pile cet index et la suite. Sinon regarder les autres chemins possibles
                else:
                    if (self.lab[droite] == "0") and (droite != prev) and trouverF(droite, actuel, reponse) is True:
                        reponse.push(actuel)
                        return True

                    if (self.lab[gauche] == "0") and (gauche != prev) and (trouverF(gauche, actuel, reponse)) is True:
                        reponse.push(actuel)
                        return True

                    if (self.lab[haut] == "0") and (haut != prev) and (trouverF(haut, actuel, reponse)) is True:
                        reponse.push(actuel)
                        return True

                    if (self.lab[bas] == "0") and (bas != prev) and (trouverF(bas, actuel, reponse)) is True:
                        reponse.push(actuel)
                        return True

                    # Si la suite de tous les chemins possibles à partir de l'index actuel sont des impasses. Alors cet
                    # index lui-même est considéré comme une impasse. (si pas de chemin entre D et F , return false)
                    else:
                        return False

        # Fonction qui converti l'index d'un string à ses coords x,y dans un tableau de tableau d par d (d = dimension)
        def indexToCoords(index):

            ligne = 0
            if index < self.dim:  # Si un index est sur la ligne 0, alors c'est un lab invalide. Mais on généralise qdmm
                return (index, 0)
            else:
                while index > self.dim:
                    index = index - self.dim
                    ligne = ligne + 1
                colonne = index
                return colonne, ligne  # ici lignes sont les y , et colonne sont les x

        # Fonction qui prend la pile (chemin) retournée par trouverF, et retourne une liste de tuples de coordonnés x,y
        def convertPileToList(pile):

            list = []
            while not pile.isEmpty():
                index = pile.pop()
                tuple = indexToCoords(index)  # Chaque index de la pile est transformé en tuple (x,y)
                list.append(tuple)  # Chaque tuple (x,y) est ajouté à la liste
            return list

        # RÉSOLUTION DU LABYRINTHE (en appelant les autres fonctions)

        # Initialisation des variables de bases utiles à résoudres le labyrinthe
        pileChemin = Stack()
        debut, fin = self.lab.find("D"), self.lab.find("F")

        #S'assurer de la validité du labyrinthe (input) avant d'essayer de le résoudre
        if (debut != -1) and (fin != -1) and len(self.lab) == (self.dim * self.dim):

            # S'il existe un chemin valide entre D et F , alors return(True, Chemin),Chemin :[(Dx, Dy) (x,y) ..(Fx, Fy)]
            if trouverF(debut, debut, pileChemin) is True:
                list = convertPileToList(pileChemin)
                return (True, list)

            # Sinon Return False
            else:
                return (False, [])

        # Si l'input ne respecte pas les conditions minimales on retourne False
        else:
            return (False, [])
            pass


# Puisque ce n'est pas précisé dans l'énoncé et il n'y a pas de réponse sur Studuim. Je me suis permis d'utiliser le
# code 2-Types-abstraits/demo1/Stack.py après l'avoir modifié et simplifié pour l'implémenter dans mon code peu exigeant
class Stack():

    def __init__(self):
        self.data = []
        self.size = 0

    def push(self, x):
        self.data.append(x)
        self.size += 1

    def pop(self):
        if (self.isEmpty()):
            return
        self.size -= 1
        return self.data.pop()

    def isEmpty(self):
        return self.size == 0

    def __str__(self):
        return str(self.data)