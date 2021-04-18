 ./mvnw clean package -DskipTests install
cd src/main/docker
docker-compose down -v
#docker-compose up
docker-compose up --force-recreate --build --remove-orphans --always-recreate-deps --renew-anon-volumes