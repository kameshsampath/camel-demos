package demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static java.util.concurrent.TimeUnit.SECONDS;


/**
 * @author kameshs
 */
@Slf4j
public class Greeter extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) throws Exception {

        Router router = Router.router(vertx);
        //Some routes
        router.route("/").handler(rc -> {

            HttpServerResponse response = rc.response();
            response
                .putHeader("content-type", "text/html")
                .end("<html><body>" +
                    "<h1>Hello from vert.x!</h1>" +
                    "<p>version = " + rc.request().version() + "</p>" +
                    "</body></html>");

        });

        router.route("/api/hello").handler(this::hello);
        router.route("/api/longhello").blockingHandler(this::longHello);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(9090, result -> {
                if (result.succeeded()) {
                    log.info("Greeter Server Started Successfully");
                    fut.complete();
                } else {
                    log.error("Failed to start greeter server", result.cause());
                    fut.fail(result.cause());
                }
            });
    }


    private void hello(RoutingContext routingContext) {
        log.info("Greeter::Hello");

        routingContext.response()
            .setStatusCode(200)
            .end("Hello World!");

    }

    private void longHello(RoutingContext routingContext) {

        log.info("Received request on Thread: " + Thread.currentThread().getName());

        //Pi.computePi(200000);

        try {
            SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Back from the nap:" + Thread.currentThread().getName());

        routingContext.response()
            .setStatusCode(200)
            .end("Hello World after " + new Date().toString());
    }
}
