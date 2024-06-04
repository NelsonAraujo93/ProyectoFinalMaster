docker pull --quiet mongo:latest
echo "Mongo image downloaded"
docker pull --quiet mysql:latest
echo "MySQL image downloaded"

# Wait until the images are downloaded
while [[ -z $(docker images -q mongo:latest) || -z $(docker images -q mysql:latest) ]]; do
  sleep 1
done

docker-compose up -d
container_names=$(docker-compose ps --format "{{.Names}}")
mongo_name=$(echo $container_names | grep mongo)
mysql_name=$(echo $container_names | grep mysql)    


echo "Mongo container name: $mongo_name"
echo "MySQL container name: $mysql_name"

echo "Docker containers started"

