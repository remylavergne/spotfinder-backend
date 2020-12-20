# spotfinder-backend

# Deployer l'environnement

- Modifier les variables d'environnement dans le fichier `spotfinder-backend.env`

  Script d'initialisation :

- Builder une version du backend via la tâche Gradle `shadowJar` dans le `build.gradle`
- Créer un dossier `pictures` à l'endroit où est déployé le serveur
- Créer un dossier `mongo` à l'endroit où est déployé le serveur
- Créer un dossier `monitoring` à l'endroit où est déployé le serveur
- Créer un dossier `monitoring/grafana` à l'endroit où est déployé le serveur
- Le dossier `grafana` doit avoir les droits `472:472`

```shell
# GF_PATHS_DATA='/var/lib/grafana' is not writable.
# You may have issues with file permissions, more information here: http://docs.grafana.org/installation/docker/#migration-from-a-previous-version-of-the-docker-container-to-5-1-or-later
# mkdir: cannot create directory '/var/lib/grafana/plugins': Permission denied
sudo chown -R 472:472 grafana
```

- Créer le dossier `libs`
- Déplacer le fichier buildé dans le bon dossier `libs`
- Mettre la configuration `prometheus.yml` dans le dossier `monitoring`
- Mettre la configuration `grafana-dashboard.json` dans le dossier `monitoring`
- Déployer `docker-compose up -d`
- Taguer la version ??

# Start project

```bash
# Build backend JAR
./gradlew build
# Start all containers
docker-compose up -d
```

### Create index for 2dsphere

Création d'un index dans mong-express.
Ce placer dans la collection `Spots`.

```javascript
{
    "location": "2dsphere"
}
```

Ou via le docker :

```
# Se connecter au docker
docker exec -it mongo-spotfinder bash
# Commande MongoDB
$ mongo
> use admin
switched to db admin
> db.auth("root", passwordPrompt())
Enter password:
> use spotfinder
```

## Indexs nécessaires

### Users

```javascript
{
    "username": "text"
}
```

```
db.users.createIndex({"username": "text"})
```

### Spots

```javascript
{
    "location": "2dsphere",
    "address": "text",
    "name": "text"
}
```

```
db.spots.createIndex({"location": "2dsphere","address": "text","name": "text"})
```

# API definition

In progress...

**Mongo Express** link => http://localhost:8081
