package ec.edu.espe.cosecha_agricultor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "cosechas";
    public static final String QUEUE_INVENTARIO = "inventario.cola";
    public static final String QUEUE_FACTURACION = "cola_facturacion";
    public static final String ROUTING_KEY_NUEVA = "nueva";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queueInventario() {
        return new Queue(QUEUE_INVENTARIO, true);
    }

    @Bean
    public Queue queueFacturacion() {
        return new Queue(QUEUE_FACTURACION, true);
    }

    @Bean
    public Binding bindingInventario(Queue queueInventario, TopicExchange exchange) {
        return BindingBuilder
                .bind(queueInventario)
                .to(exchange)
                .with(ROUTING_KEY_NUEVA);
    }

    @Bean
    public Binding bindingFacturacion(Queue queueFacturacion, TopicExchange exchange) {
        return BindingBuilder
                .bind(queueFacturacion)
                .to(exchange)
                .with(ROUTING_KEY_NUEVA);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
