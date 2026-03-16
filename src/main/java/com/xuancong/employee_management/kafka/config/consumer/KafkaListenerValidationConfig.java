package com.xuancong.employee_management.kafka.config.consumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class KafkaListenerValidationConfig implements KafkaListenerConfigurer {
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    public KafkaListenerValidationConfig(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.localValidatorFactoryBean = localValidatorFactoryBean;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(localValidatorFactoryBean);
    }
}
