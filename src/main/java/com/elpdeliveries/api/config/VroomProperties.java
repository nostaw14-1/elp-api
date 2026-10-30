package com.elpdeliveries.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "routing")
public class VroomProperties {
    private String url;
    private Double latitude;
    private Double longitude;
}
