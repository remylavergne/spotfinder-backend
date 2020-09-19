# spotfinder-backend

# Start project

```shell script
# Fill the `export-env-dev.sh` file

# Startup Docker containers : MongoBD + Mongo Express
chmod +x export-env-dev.sh && \
chmod +x startup.sh && \
./startup.sh

# Start server
./gradlew run
```

**Mongo Express** link => http://localhost:8081