package ec.edu.espe.cosecha_agricultor.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "cosechas";
    public static final String QUEUE_INVENTARIO = "inventario.cola";
    public static final String QUEUE_FACTURACION = "cola_facturacion";  // AGREGADO
    public static final String ROUTING_KEY_NUEVA = "nueva";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue inventarioQueue() {
        return new Queue(QUEUE_INVENTARIO);
    }

    // NUEVA COLA PARA FACTURACIÓN
    @Bean
    public Queue facturacionQueue() {
        return new Queue(QUEUE_FACTURACION);
    }

    @Bean
    public Binding bindingInventario(Queue inventarioQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(inventarioQueue).to(topicExchange).with(ROUTING_KEY_NUEVA);
    }

    // NUEVO BINDING PARA FACTURACIÓN
    @Bean
    public Binding bindingFacturacion(@Qualifier("facturacionQueue") Queue facturacionQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(facturacionQueue).to(topicExchange).with(ROUTING_KEY_NUEVA);
    }
}
