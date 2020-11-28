# spotfinder-backend

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
> use admin
switched to db admin
> db.auth("root", passwordPrompt())
Enter password:
> use spotfinder
> db.spots.createIndex({"location":"2dsphere"})
```

## Indexs nécessaires

### Users

```javascript
{
    "username": "text"
}
```

### Spots

```javascript
{
    "location": "2dsphere",
    "address": "text",
    "name": "text"
}
```

# API definition

In progress...

**Mongo Express** link => http://localhost:8081