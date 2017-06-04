package org.workspace7.camel;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class GreetingRouter extends RouteBuilder {


    @Value("${rest.uri}")
    String restUri;

    @BeanInject("greetingBean")
    GreetingBean greetingBean;

    public void configure() {

        from(restUri)
            .threads(2, 5).id("RestPool")
            .process(exchange -> {
                HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
                String param1 = request.getParameter("param1");
                if (param1 == null) {
                    log.info("Param1 is null sending empty body");
                    exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 204);
                } else {
                    log.info("Param1 is available sending greeting");
                    exchange.getOut().setBody(greetingBean.sayHello(param1));
                }
            });
    }

}
