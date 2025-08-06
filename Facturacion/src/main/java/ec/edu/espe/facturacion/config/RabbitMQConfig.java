package ec.edu.espe.facturacion.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_COSECHAS = "cosechas";
    public static final String QUEUE_FACTURACION = "cola_facturacion";
    public static final String ROUTING_KEY_NUEVA_COSECHA = "nueva";

    @Bean
    public TopicExchange cosechasExchange() {
        return new TopicExchange(EXCHANGE_COSECHAS);
    }

    @Bean
    public Queue facturacionQueue() {
        return QueueBuilder.durable(QUEUE_FACTURACION).build();
    }

    @Bean
    public Binding facturacionBinding() {
        return BindingBuilder
                .bind(facturacionQueue())
                .to(cosechasExchange())
                .with(ROUTING_KEY_NUEVA_COSECHA);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
