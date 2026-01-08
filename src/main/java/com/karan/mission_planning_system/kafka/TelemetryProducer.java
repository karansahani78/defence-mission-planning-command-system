package com.karan.mission_planning_system.kafka;
import com.karan.mission_planning_system.telemetry.TelemetryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelemetryProducer {

    private final KafkaTemplate<String, TelemetryEvent> kafkaTemplate;

    @Value("${telemetry.kafka.topic}")
    private String topic;

    public void publish(TelemetryEvent event) {

        kafkaTemplate.send(topic, event.getEventId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error(
                                "❌ Telemetry publish failed | event={} | reason={}",
                                event.getEventType(),
                                ex.getMessage(),
                                ex
                        );
                    } else {
                        log.debug(
                                "✅ Telemetry published | topic={} | partition={} | offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });
    }
}
