On développe un exemple de serveur Web fonctionnel qui accepte un navigateur comme client. La construction est progressive, elle suit la numérotation des fichier (cf exercice du polycopié de TP).  
- ```SimpleWebServer00.java``` : trace de la mise en place d'une connexion à executer avec : ```java SimpleWebServeur 8080 test.html``` puis utiliser un navigateur et taper l'URL : ```http://localhost:8080``` ou utiliser ```telnet localhost 8080```
- ```SimpleWebServer01.java``` : multi-clients avec début de décodage de l'en-tête envoyée par le client
- ```SimpleWebServer02.java``` : avec en plus l'envoi du fichier passé en paramètre
- ```SimpleWebServer03.java``` : version 3 réachitecturée
- ```SimpleWebServer04.java``` : version fonctionnelle
- ```SimpleWebServer04bis.java``` : version fonctionnelle simplifiée avec le pattern singleton
- ```SimpleWebServer05.java``` : threads avec équilibrage de charge
- ```SimpleWebServerCache.java``` : version avec cache 


