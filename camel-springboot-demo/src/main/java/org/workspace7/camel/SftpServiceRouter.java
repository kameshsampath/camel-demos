package org.workspace7.camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SftpServiceRouter extends RouteBuilder {

    @Value("${timer.delay}")
    String timerDelay;

    public void configure() {

        from("sftp://192.168.56.50/download?username=vagrant&password=vagrant&delay=" + timerDelay)
            .log("$simple{in.header.CamelFileName}")
            .to("file://target/outbox");

    }

}
