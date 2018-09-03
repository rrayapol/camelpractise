package com.camel.cameldemo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Service;

@Service
public class CamelService {

    public void moveFile(String source) throws Exception {

        CamelContext context = new DefaultCamelContext();

        ConnectionFactory connFactory = new ActiveMQConnectionFactory();

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connFactory));

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                
                if(source.equalsIgnoreCase("queue")) {

                from("file:/Users/nisum/Documents/camelpractise/Dest1?noop=true").to("jms:inputQueue");
                
                }else if(source.equalsIgnoreCase("folder")){
                    from("jms:inputQueue").to("file:/Users/nisum/Documents/camelpractise/Dest2");
                }

               /* from("jms:incomingOrders").choice().when(header("CamelFileName").endsWith("log")).to("jms:logOrders")
                        .when(header("CamelFileName").endsWith("png")).to("jms:pngOrders");*/

            }
        });

        context.start();

        Thread.sleep(10000);

        context.stop();

    }

}
