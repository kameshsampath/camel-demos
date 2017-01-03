package org.workspace7.camel;

import org.springframework.stereotype.Component;

/**
 * @author kameshs
 */
@Component("greetingBean")
public class GreetingBean {

    public String sayHello(String user) {
        return "Hello " + user + "!";
    }
}
