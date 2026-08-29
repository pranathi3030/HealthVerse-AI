package com.healthverse.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name("healthverse.notifications")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic remindersTopic() {
        return TopicBuilder.name("healthverse.reminders")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic alertsTopic() {
        return TopicBuilder.name("healthverse.alerts")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
