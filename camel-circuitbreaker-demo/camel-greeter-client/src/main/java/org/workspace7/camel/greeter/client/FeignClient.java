package org.workspace7.camel.greeter.client;

import feign.Feign;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kameshs
 */
@Slf4j
public class FeignClient {

    public void fire() {
        GreeterService greeterService = Feign.builder()
            .target(GreeterService.class, "http://localhost:8282");
        String response = greeterService.longHello();
        log.info("Response : {}", response);
    }
}
