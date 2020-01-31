# arango-vertx-native-experiment

## start db

```shell script
docker run -e ARANGO_ROOT_PASSWORD=test -p 8529:8529 --rm arangodb:3.6
```

## run

```shell script
mvn package && ./target/hello_native run vertx.HTTPVerticle
```

```shell script
curl http://localhost:8080
```
