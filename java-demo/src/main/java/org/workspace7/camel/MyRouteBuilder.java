package org.workspace7.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyRouteBuilder.class);

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        from("jetty:http://0.0.0.0:9090/greeting")
            .process(exchange -> {
                HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
                String param1 = request.getParameter("param1");
                if (param1 == null) {
                    LOGGER.info("Param1 is null sending empty body");
                    exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 204);
                } else {
                    LOGGER.info("Param1 is available sending greeting");
                    exchange.getOut().setBody("Hello " + request.getParameter("param1") + "!");
                }
            }); // what to do here will stop makes sense here ??

    }

}
