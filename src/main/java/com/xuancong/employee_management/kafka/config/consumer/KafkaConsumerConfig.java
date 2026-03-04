package com.xuancong.employee_management.kafka.config.consumer;

import com.xuancong.employee_management.kafka.message.Event;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    // nhà máy thật sự tạo ra container listener
    // có thể có nhiều factory khác nhau cho nhiều event => name : cho biết container theo factory nào
    @Bean(name = "listenerContainerFactory")
     public  ConcurrentKafkaListenerContainerFactory<String, Event<?>> kafkaListenerContainerFactory(KafkaProperties kafkaProperties){
        ConcurrentKafkaListenerContainerFactory<String, Event<?>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.consumerFactory(String.class, Event.class, kafkaProperties));
        factory.setConcurrency(3);

        factory.setCommonErrorHandler(
                new DefaultErrorHandler(new FixedBackOff(1000L, 3))
        );

        return factory;
    }


    // cái tạo ra instant KafkaConsumer  theo config( cái chay ở tầng thấp dưới container listener , nó kết nối kafka, gọi pull để nhận message...)
    private <K, V> ConsumerFactory<K, V> consumerFactory(
            Class<K> keyClass,
            Class<V> valueClass,
            KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);

        var keyDeserializer = new ErrorHandlingDeserializer<>(getJsonDeserializer(keyClass));
        var valueDeserializer = new ErrorHandlingDeserializer<>(getJsonDeserializer(valueClass));

        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }



    private <T> JsonDeserializer<T> getJsonDeserializer(Class<T> tClass) {
        var jsonDeserializer = new JsonDeserializer<>(tClass);
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }
}

