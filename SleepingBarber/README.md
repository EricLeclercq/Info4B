Résolution du problème classique du Sleeping Barber :

- **SleepingBarber00.java :** solution approximative qui ne répond pas a l'ennoncé et n'est pas satisfaissante du point de vue de l'accès concurrent à la salle d'attente. Trouvez pourquoi ! Indice : l'état d'une ressource est souvent modifié suite a la consultation de l'état courant.
- **SleepingBarber01.java :**  avec wait et notify (déclenchés par la ressource). Que peut signifier l'exception Illegal Monitor State - Notez aussi comment wait et notify permettent de résoudre le problème d'accès concurrents du programme précédent.
