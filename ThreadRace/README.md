Cours de thread.
- chaque thread a son arrivée communique avec le classement qui le positionne à la nouvelle place.
- le classement est une ressource partagée dont les méthodes qui modifient son état sont contrôlées par un moniteur (synchronized)

Pour avoir l'intégralité de l'affichage, rediriger la sortie vers un fichier :
 java ThreadRace > trace.txt
 more trace.txt
