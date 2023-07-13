# 1.1.1 
## Un aéroport est identifié par trois lettres uniques à chaque aéroport
context Aéroport
inv: self.identifiant.matches('^[a-zA-Z]{3}$')
inv: Aéroport.allInstances() -> forAll(a1, a2 | a1 <> a2 
implies a1.identifiant <> a2.identifiant)


# 1.1.3
## La partie alphabétique de l'ID d'un vol est unique à chaque compagnie et la partie numérique est unique à chaque vol au sein de la même compagnie
context VoyageAérien
inv: VoyageAérien.allInstances -> forAll(v1, v2 | v1.CompagnieAérienne <> v2.CompagnieAérienne
implies v1.identifiant.substring(1,2) <> v2.identifiant.substring(1,2))
inv: VoyageAérien.allInstances -> forAll(v1, v2 | v1 <> v2 and v1.CompagnieAérienne = v2.CompagnieAérienne
implies v1.identifiant.substring(3, v1.identifiant.size()) <> v2.identifiant.substring(3, v2.identifiant.size()))

## L'aéroport de départ et d'arrivée d'un vol doit être différent
context VoyageAérien
inv: self.lieu_départ <> self.lieu_arrivée


# 1.1.5
## Tous les sièges d'une même section ont le même prix
context SiègeAvion
inv: SiègeAvion.AllInstances() -> forAll(s1, s2 | s1.SectionAvion = s2.SectionAvion
implies s1.SectionAvion.prix = s2.SectionAvion.prix)


# 1.2.1
## Un port est identié par trois lettres uniques à chaque port
context Port
inv: self.identifiant.matches('^[a-zA-Z]{3}$')
inv: Port.allInstances() -> forAll(p1, p2 | p1 <> p2 
implies p1.identifiant <> p2.identifiant)


# 1.2.3
## Un itinéraire ne peut pas durer plus de 21 jours
Context VoyageNaval
inv: self.date_arrivée - self.date_départ <= 21

## Le port de départ et d'arrivée doit être le même
Context VoyageNaval
inv: self.lieu_départ = self.lieu_arrivée

## Un paquebot peut être assigné à plusieurs itinéraires tant qu'ils ne se chevauchent pas
context Paquebot
inv: self.VoyageNaval -> forAll(v1, v2 | v1.date_départ < v2.date_départ
implies v1.date_arrivée < v2.date_départ)


# 1.2.6
## Toutes les cabines d'une même section ont le même prix
context Cabine
inv: Cabine.AllInstances() -> forAll(c1, c2 | c1.SectionPaquebot = c2.SectionPaquebot
implies c1.SectionPaquebot.prix = c2.SectionPaquebot.prix)


# 1.3.1
## Une gare est identifiée par trois lettres uniques à chaque gare
context Gare
inv: self.identifiant.matches('^[a-zA-Z]{3}$')
inv: Gare.allInstances() -> forAll(g1, g2 | g1 <> g2 
implies g1.identifiant <> g2.identifiant)


# 2.1.2
## Le client peut réserver un siège disponible dans un vol (ligne) donné
context Client::réserver(siège: Place)
pre: siège.Reservation.isEmpty() = True


# 2.1.3
## Un siège réservé devient assigné à un passager une fois payé: le siège est donc confirmé
context Client::payer(numéro : Integer)
post: result = System.Réservation -> select(numéro = numéro_réservation) ->
place.occupant-> notEmpty()


# 2.2.2
## Le client peut réserver une cabine disponible pour un itinéraire donné
context Client::réserver(cabine: Cabine)
pre: cabine.Reservation.isEmpty() = True