package demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.SynchronizationAdapter;

/**
 * @author kameshs
 */
@Slf4j
public class LongHelloCallback extends SynchronizationAdapter {

    @Override
    public void onComplete(Exchange exchange) {
        log.trace("Response Received");
        String bodyOut = exchange.getOut().getBody(String.class);
        log.trace("{}", bodyOut);
        ProducerTemplate template = exchange.getContext().createProducerTemplate();
        template.sendBody("direct:async-response", bodyOut);
    }

    @Override
    public void onFailure(Exchange exchange) {
        log.error("Error processing...", exchange.getException());
        //TODO add error processing
    }
}
