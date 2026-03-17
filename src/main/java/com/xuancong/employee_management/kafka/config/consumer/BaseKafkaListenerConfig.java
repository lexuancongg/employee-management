package com.xuancong.employee_management.kafka.config.consumer;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
public abstract class BaseKafkaListenerConfig <K,V>{
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final KafkaProperties kafkaProperties;


    public BaseKafkaListenerConfig(Class<K> keyType, Class<V> valueType, KafkaProperties kafkaProperties) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.kafkaProperties = kafkaProperties;
    }

    public abstract ConcurrentKafkaListenerContainerFactory<K,V> kafkaListenerContainerFactory();


    public ConcurrentKafkaListenerContainerFactory<K,V> listenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<K,V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.typeConsumerFactory(keyType, valueType));
        factory.setCommonErrorHandler(new DefaultErrorHandler((record, exception) -> {
            System.out.println(" ERROR consuming message: " + exception.getMessage());
            System.out.println("Record value: " + record.value());
            System.out.println("Cause: " + exception.getCause());
        }));
        return factory;

    }

    private ConsumerFactory<K, V> typeConsumerFactory(Class<K> keyClazz, Class<V> valueClazz) {
        ErrorHandlingDeserializer<K> keyDeserializer = new ErrorHandlingDeserializer<>(
                getJsonDeserializer(this.keyType)
        );
        keyDeserializer.setFailedDeserializationFunction(info -> {
            System.out.println(" Key deserialize failed: " + new String(info.getData()));
            System.out.println(" Exception: " + info.getException().getMessage());
            return null;
        });


        ErrorHandlingDeserializer<V> valueDeserializer = new ErrorHandlingDeserializer<>(
                getJsonDeserializer(this.valueType)
        );

        valueDeserializer.setFailedDeserializationFunction(info -> {
            System.out.println(" Value deserialize failed: " + new String(info.getData()));
            System.out.println(" Exception: " + info.getException().getMessage());
            return null;
        });
        Map<String,Object> consumerProps = this.kafkaProperties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(consumerProps,keyDeserializer,valueDeserializer);
    }

    private static <T> JsonDeserializer<T> getJsonDeserializer(Class<T> clazz) {
        var jsonDeserializer = new JsonDeserializer<>(clazz);
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }
}
