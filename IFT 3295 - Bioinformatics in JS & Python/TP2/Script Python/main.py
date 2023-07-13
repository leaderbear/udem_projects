import seq
import globalAli


# D'abord ouvrir seq.fasta et ajouter tous les séquences dans une liste.
# Ensuite on fait l'alignement entre les séquences.
seqList = seq.getSeq()
q1 = globalAli.createMatrix(len(seqList), len(seqList), 0)
for i in range(len(q1)):
    for j in range(len(q1[0])):
        if i != j:
            V, BT = globalAli.aligGlobal(seqList[i], seqList[j])
            q1[i][j] = V[-1][-1]


print("La réponse à la question 2.1 :")
for l in q1:
    print(l)
print("-"*20)

# On calcule la somme de tout et on garde la plus grande.
rowsSum = []
for row in q1:
    rowsSum.append(sum(row))

rowPos = rowsSum.index(max(rowsSum))
rowSum = max(rowsSum)

print("La réponse à la question 2.2 :")
print("La séquence centrale S* est la séquence", rowPos + 1, "avec la plus grande somme de" , rowSum)
print(seqList[rowPos])
print("-"*20)





print("La réponse à la question 2.3 :")


for s in range(1):
    if(s != rowPos):                # don't compare S* to itself.
        V, BT = globalAli.aligGlobal(seqList[rowPos], seqList[s])      # compare S* with other S'
        s1, s2 = globalAli.showResult(V,BT, seqList[rowPos], seqList[s])
        #print("\nOn compare S*",rowPos ,"avec S'", s)
        print("\nS'", s, ":", s2)
        print("S*", rowPos, ":", s1)






