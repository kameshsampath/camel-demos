package org.workspace7.camel.greeter.client;

import feign.RequestLine;

/**
 * @author kameshs
 */
public interface GreeterService {

    @RequestLine("GET /hello")
    String longHello();
}
