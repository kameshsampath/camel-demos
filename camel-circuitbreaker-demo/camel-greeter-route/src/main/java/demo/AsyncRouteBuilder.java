/*
 * Copyright 2017 Kamesh Sampath<kamesh.sampath@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

import java.util.concurrent.Future;

/**
 * @author kameshs
 */
@Slf4j
public class AsyncRouteBuilder extends RouteBuilder {

    @PropertyInject("{{jetty.pool.min}}")
    int jettyPoolMinSize;

    @PropertyInject("{{jetty.pool.max}}")
    int jettyPoolMaxSize;


    @PropertyInject("{{async.pool.min}}")
    int asyncPoolMinSize;

    @PropertyInject("{{async.pool.max}}")
    int asyncPoolMaxSize;

    @Override
    public void configure() throws Exception {

        LongHelloCallback callback = new LongHelloCallback();

        from("jetty:http://0.0.0.0:8282/hello").routeId("longhello-route")
            .threads(jettyPoolMinSize, jettyPoolMaxSize, "jetty-pool")
            .to("direct:lgreeter-fire-and-forget");

        //this is all async - the end client will not wait for response - may be some other push mechanism could be used to push back
        // e.g. websockets
        from("direct:lgreeter-fire-and-forget").routeId("async-route-ff")
            .threads(asyncPoolMinSize, asyncPoolMaxSize, "async-pool-ff")
            .process(exchange -> {
                log.info("Starting async processing ...");

                //Create and Send request to long running process
                ProducerTemplate template = getContext().createProducerTemplate();
                template.asyncCallback("jetty:http://localhost:9090/api/longhello?bridgeEndpoint=true",
                    exchange, callback);
            }).end();

        //This could be used for all synchronous clients - where the processing is done async but response is sync
        from("direct:lgreeter-wait").routeId("async-route-w")
            .threads(asyncPoolMinSize, asyncPoolMaxSize, "async-pool-w")
            .process(exchange -> {

                log.info("Starting async processing ...");

                //Create and Send request to long running process
                ProducerTemplate template = getContext().createProducerTemplate();
                Future<Exchange> future = template.asyncSend("jetty:http://localhost:9090/api/longhello?bridgeEndpoint=true",
                    exchange);

                Exchange respEx = future.get(); // this is blocking call so potential choking point

                String bodyOut = respEx.getOut().getBody(String.class);
                log.info("{}", bodyOut);

                //set the response so that its sent back to the caller
                exchange.getOut().setBody(bodyOut);

            }).end();

        from("direct:async-response").routeId("async-response-handler")
            .transform(simple("${body}"))
            .log("${body}");

    }
}
