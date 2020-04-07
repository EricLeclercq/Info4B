# Operating Systems Concepts
Resources for operating system courses. Program examples in Java, C and Scala.

----

Exemples utilisés ou cours magistral ou pour illustrer une notion du cours :
- Processus en C
- Threads avec Python
- Acteurs en Scala
- Algorithmes de synchronisation sans recours aux primitives : Dijkstra, Dekker, Peterson


Exemples développés, travaux dirigés et travaux pratiques, pour chaque exercice vous avez la notion abordée :
- [Course de Threads](ThreadRace) : création de thread, lancement, arrêt
- Thread et priorités
- [Calcul répartis de nombres premiers](PrimeNmbers) : comment équilibrer la charge d'une tâche sur plusieurs threads
- [Simulation distributeurs et banque](BankSimulation) : gestion de la concurrence
- [Sleeping Barber](SleepingBarber) : gestion de la concurrence, synchronisation
- [Simulation de Parking](ParkingSimulation) : gestion de la concurrence et ressource composite
- [Tramway](TramwaySimulation) : accès à une ressource partagée et synchronisation
- [Fichier et sérialisation](FileAndSerialization) : stockage d'objets dans un fichier, exemple de l'annuaire
- [Socket TCP](Chat) : réalisation d'un chat mono et multi clients
- [Socket UDP serveur et client](TimeServerUDP) : exemple de serveur de temps
- [Mini serveur web](WebServer) : rassemble beaucoup de notions de programmation réseau et synthétise les notions de SE vues en cours

Attention : les exemples ne sont pas des corrections d'exercices, il s'agit de supports pédagogiques qui s'inscrivent dans une progression visant à permettre aux étudiants de :
 - de caractériser les problèmes de concurrence d'accès aux ressources partagées
 - de savoir identifier une section critique
 - de mettre en oeuvre les mécanismes réalisant l'exlcusion mutelle (moniteurs, sémaphores, verrous)
 - de caractériser les situation pouvant counduire à des interblocages, de résoudre ou détecter ces interblocages
 - plus généralement de développer un programme multi-thread correspondant à une situation/simulation donnée.
