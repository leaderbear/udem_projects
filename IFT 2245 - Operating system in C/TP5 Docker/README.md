# Introduction

Comme vous l’avez vu en cours, Docker est un puissant système de
conteneurisation. Lors de ce TP, vous aurez la chance de vous familiariser avec Docker.

Vous allez conteneuriser le TP0 de ce cours.

# Votre tâche

Le code à exécuter se trouve dans ce repo GitHub:
https://github.com/IFT2245/TP0-docker.

Vous devriez déjà être familiers avec ce que ce code fait. Vous devriez savoir, bien sûr, que l'exécutable
tiré de ce TP accepte un fichier et un input, et crache le résultat obtenu lorsqu'on roule
la machine de Turing décrite dans le fichier sur ledit input. Rien de très sorcier.

## 1.1 Compilation

Commencez par modifier le Dockerfile pour que le TP0 soit compilé lors de `docker build`. Vous aurez besoin de CMake,
make, gcc, etc. 

**NOTEZ QUE VOUS DEVEZ UTILISER `git clone` POUR OBTENIR LE FICHIER! VOUS NE POUVEZ SUPPOSER LA PRÉSENCE D'AUCUN
FICHIER LOCAL, SAUF CEUX FOURNIS DANS CET ÉNONCÉ!** Autrement dit, lorsque nous allons vous corriger, les seuls fichiers disponibles
seront `Dockerfile`, `Makefile`, et `machine.turing`.

## 1.2 Exécution

Remarquez que dans le repo TP0-docker, aucun fichier de machine de Turing n'est fourni. Donc même si vous compilez le TP0 à partir 
des fichiers obtenus avec `git clone`, il n'y a pas d'input à lui passer. 

Placez le fichier de machine de Turing fourni dans cet énoncé (`machine.turing`) dans votre image Docker, de façon à ce que le TP0 puisse voir
le fichier. Autrement dit, ce fichier doit être *"dans le docker"*.

Ensuite, faites en sorte qu'appeler `docker run` sur votre image exécute la machine de Turing de `machine.turing` sur l'input `0000`.

## 1.3 STDIN

Devoir toujours exécuter sur l'input `0000` rend votre Docker un peu inutile. Faites en sorte que l'usager puisse indiquer l'argument au moment d'appeler `docker run`, comme suit:

    docker build -t tag_pour_votre_image .
    docker run tag_pour_votre_image <input_custom>

## 1.4 Machine de Turing variable 

Devoir toujours exécuter la même machine de Turing est un peu énervant. Si jamais l'usager voulait vraiment rouler votre Docker "en production",
il devrait construire une image pour chaque machine de Turing qu'il voudrait rouler.
(Faisons semblant qu'une machine de Turing est autre chose qu'un jouet intéressant pour les programmeurs novices, et qu'on veut vraiment pouvoir utiliser
votre image.)

Trouvez une commande telle que l'usager puisse changer la description de la machine lors de `docker run`, et non lors de `docker build`.

## 1.5 Makefile

Les points 1.2, 1.3, et 1.4 sont bien beaux, mais on remarque très vite qu'utiliser votre Docker est un processus très verbeux.
Personnellement, je n'aimerais pas avoir à écrire ~7 commandes bash pour rouler une simple machine de Turing. 

Éditez le Makefile fourni. On veut avoir ces fonctionnalités:

1. `make build IMAGE=<img>`: ceci appelle `docker build` et tag l'image résultante `<img>`. Par exemple, `make build IMAGE=bloop` nommerais votre image `bloop`.
2. `make run IMAGE=<img> INPUT=<input>`: ceci appelle `docker run` sur `<img>` avec un input particulier.
3. `make run IMAGE=<img> TURING=<local turing machine path> INPUT=<input>`: ceci passe une machine de Turing locale au Docker, comme au point 1.4, et la roule sur un input particulier. **NOTEZ QU'ICI VOUS DEVREZ UTILISER UNE COMMANDE COMME `$PWD` POUR TROUVER LE PATH LOCAL DE L'USAGER!** (mais probablement pas exactement `$PWD`...)

Dans ces 3 options, chaque paramètre doit pouvoir être omis. Si une alternative n'est pas spécifiée, on doit avoir ces valeurs:

    IMAGE: tp0_img
    INPUT: 0000
    TURING:    # if not specified, runs machine.turing that's already inside the Docker image

Autrement dit, `make run` (sans aucun argument) roule `0000` sur la machine `machine.turing` dans l'image `tp0_img`.

Indice: il est possible de traiter ces cas autant dans le Makefile que dans le Dockerfile. À vous de décider.

## 1.6 Déploiement

Pour la dernière étape, on vous demande de téléverser votre image vers Dockerhub.

Dockerhub permet à ses usagers d'y placer leurs images Docker. De cette façon, il est possible de télécharger votre
image à partir de n'importe quel environnement Docker avec une simple commande `git pull`.

Téléversez votre image vers Dockerhub, et ensuite placez le nom de l'image sur Dockerhub dans le `Makefile`, dans la variable `hubimg`.

L'objectif, est que lorsqu'on appellera `make pull`, on obtiendra votre image.

# Remise et Correction

Ce TP est corrigé en "tout ou rien". Autrement dit, seul les points 1.5 et 1.6 sont corrigés (puisqu'ils vérifient
implicitement tous les autres points), 
et vous aurez les points seulement si toutes les commandes demandées du Makefile fonctionnent.

- Les barèmes standards du [TPX](https://github.com/IFT2245/TPX) s'appliquent (fuites mémoires, accès illégaux, etc), s'ils peuvent s'appliquer.
