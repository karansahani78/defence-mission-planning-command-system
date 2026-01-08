package com.karan.mission_planning_system.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String TELEMETRY_TOPIC = "command.telemetry";

    @Bean
    public NewTopic telemetryTopic() {
        return new NewTopic(TELEMETRY_TOPIC, 3, (short) 1);
    }
}
