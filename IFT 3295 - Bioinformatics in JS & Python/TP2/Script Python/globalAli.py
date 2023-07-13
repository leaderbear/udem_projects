import scores

a = 10
b = 1
UP = 1
LEFT = 2
DIAG = 3

# Plutôt que de créer une seule matrice avec des tuples. Nous avons opter pour créer plusieurs matrices.
# Nous avons trouver que c'est plus simple de manipuler nos données comme suit.
# On calcule chacune de nos cases en utilisant les formules du cours/tp.
# Tout en gardant des flèches pour pouvoir refaire le chemin plus tard.

def aligGlobal(seq1, seq2):

    E = createMatrix(len(seq1)+1, len(seq2)+1, 0)
    F = createMatrix(len(seq1)+1, len(seq2)+1, 0)
    G = createMatrix(len(seq1)+1, len(seq2)+1, 0)
    V = createMatrix(len(seq1) + 1, len(seq2) + 1, 0)
    backTrack = createMatrix(len(seq1)+1, len(seq2)+1, 0)

    # initial conditions
    for i in range(1, len(seq1)+1):
        F[i][0] = - (a + i * b)
        E[i][0] = -9999999999
        G[i][0] = -9999999999
        V[i][0] = max(F[i][0], E[i][0], G[i][0])
        backTrack[i][0] = UP

    for j in range(1,len(seq2)+1):
        E[0][j] = - (a + j * b)
        F[0][j] = -9999999999
        G[0][j] = -9999999999
        V[0][j] = max(F[0][j], E[0][j], G[0][j])
        backTrack[0][j] = LEFT

    # recurrences
    for i in range(1, len(V)):
        for j in range(1, len(V[0])):

            E[i][j] = max(E[i - 1][j] - b, V[i - 1][j] - a - b)
            F[i][j] = max(F[i][j - 1] - b, V[i][j - 1] - a - b)
            G[i][j] = V[i - 1][j - 1] + scores.getScore(seq1[i - 1], seq2[j - 1])

            V[i][j] = max(G[i][j], E[i][j], F[i][j])
            if V[i][j] == G[i][j]:
                backTrack[i][j] = DIAG
            elif V[i][j] == E[i][j]:
                backTrack[i][j] = UP
            else:
                backTrack[i][j] = LEFT


    return V, backTrack

# Utilise les flèches pour imprimer les string.
def showResult(V, BT, seq1, seq2):

    i = len(V) - 1
    j = len(V[0]) - 1
    s1 = []
    s2 = []

    while (i > 0 or j > 0):
        if BT[i][j] == DIAG:
            s1.append(seq1[i-1])
            s2.append(seq2[j-1])
            i = i - 1
            j = j - 1
        elif BT[i][j] == LEFT:
            s1.append("*")
            s2.append(seq2[j-1])
            j = j - 1
        elif BT[i][j] == UP:
            s1.append(seq1[i-1])
            s2.append("*")
            i = i - 1
        elif i == 0 & j == 0:
            break

    #print("".join(s1))
    #print("".join(s2))
    return "".join(s1)[::-1], "".join(s2)[::-1] # Don't forget to reverse string




def createMatrix(m, n, value):
    mat = []
    for i in range(m):
        mat.append([value]*n)
    return mat