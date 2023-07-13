# abderrahim_tabta skander_ben_ahmed
# Date: 16/10/20
# But du code : reçoit une String composée d’opérateurs et d’opérandes en ordre postfixée et retourne l’expression
# équivalente en ordre infixée

def postfixToInfix(exp):
    stack = []
    string = ""
    i = 0

    # Une boucle while qui fait le tour de tout les elements contenue dans l'expression en ordre postfixée.
    while i < len(exp):

        # Verifie si l'élément actuel est un chiffre ou lettre et verifie ensuite si l'élément qui suit est
        # un espace et l'ajoute dans la pile.
        if exp[i].isdigit() or exp[i].isalpha():

            if exp[i + 1].isspace():
                stack.append(exp[i])
                i += 1

            # Si l'élément qui suit est un chiffre, alors lié les 2 chiffres et les ajouter dans la pile
            elif exp[i + 1].isdigit():
                c = "" + str(exp[i]) + str(exp[i + 1])
                stack.append(c)
                i += 2

        # Verifie si l'élément actuel est un espace et cherche le prochain
        elif exp[i].isspace():
            i += 1

        else:

            # Si l'élément actuel est un opérateur unaires √ ou !, appliquer le bon ordre
            if str(exp[i]) == "!":
                op1 = stack.pop()
                stack.append(op1 + exp[i])

            elif str(exp[i]) == "\u221A":
                op1 = stack.pop()
                stack.append("\u221A" + op1)

            # Si l'élément actuel est un opérateur binaire alors le placer entre les opérandes
            else:
                op1 = stack.pop()
                op2 = stack.pop()

                # ne pas mettre les parentheses si l'opérateur est le dernier élément.
                if i == len(exp) - 1:
                    stack.append(op2 + exp[i] + op1)

                else:
                    stack.append("(" + op2 + exp[i] + op1 + ")")

            i += 1

    # Transformer la pile en string
    for c in stack:
        string += c

    return string

#print(postfixToInfix("15 7 1 1 + - / 3 * 2 1 1 + + -"))