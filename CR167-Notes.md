# CR 167 VORVON VORBIS

- EINGANG: VORBIS = aktueller Zuglaufpunkt BSC
- AUSGANG: VORVON = aktueller Zuglaufpunkt BSC

## EINGANG: VORVON

| BSC_TRASSEN_PRUEFUNG | VORBEREITET_VON_BIS |
| -------------------- | ------------------- |
| Lfg                  | Sb G                |
| Eh                   | Gro                 |
| Gh                   | Gh                  |

ZLP 1 = Mu
ZLP 2 = Mfl
ZLP 3 = Lfg = Beginn Betriebsstelle
ZLP 4 ...

Mue -> Kein match in GRENZPUNKT_MAPPING Tabelle
Mfl -> Kein match in GRENZPUNKT_MAPPING Tabelle
Lfg -> Match: VORBEREITET_VON_BIS = Sb G

##

List<BSC> -> BSC => Set<BSC> -> BSC

For each ZLP get all matches => ZLP -> List<Match<GrenzpunktMapping>>
Find first where list > 0, if > 1 -> error

Ausland, Grenze, BSC AT, relevanter bsc

## TODO

### Ausgang

current ZLP == last Behandlungspunkt before endBst
use all ZLP from current to look for BSC mapping match

### Eingang

current ZLP == first Behandlungspunkt after beginBst
use all ZLP from start to current ZLP to look for BSC mapping match



# Wird Grenzpunkt mapping auf Behandlungspunkt in der Mitte verwendet?