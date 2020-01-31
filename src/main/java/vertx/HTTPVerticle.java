package vertx;

import com.arangodb.next.communication.ArangoCommunication;
import com.arangodb.next.communication.ArangoTopology;
import com.arangodb.next.communication.CommunicationConfig;
import com.arangodb.next.connection.*;
import io.vertx.core.AbstractVerticle;
import reactor.core.publisher.Mono;

public class HTTPVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      getDbVersion().block();
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8080, listen -> {
      if (listen.succeeded()) {
        System.out.println("Server listening on http://localhost:8080/");
      } else {
        listen.cause().printStackTrace();
        System.exit(1);
      }
    });
  }

  private static Mono<ArangoResponse> getDbVersion() {
    final ArangoRequest REQUEST = ArangoRequest.builder()
      .database("_system")
      .path("/_api/version")
      .requestType(ArangoRequest.RequestType.GET)
      .putQueryParam("details", "true")
      .build();

    ArangoCommunication communication = ArangoCommunication.create(
      CommunicationConfig.builder()
        .addHosts(HostDescription.of("localhost", 8529))
        .authenticationMethod(AuthenticationMethod.ofBasic("root", "test"))
        .topology(ArangoTopology.SINGLE_SERVER)
        .acquireHostList(false)
        .protocol(ArangoProtocol.HTTP)
        .build()
    ).block();

    return communication.execute(REQUEST)
      .doOnNext(v -> System.out.println("OK"));
  }

}
