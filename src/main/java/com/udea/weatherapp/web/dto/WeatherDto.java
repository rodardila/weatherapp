package com.udea.weatherapp.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class WeatherDto extends RepresentationModel<WeatherDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "City name is mandatory")
    private String cityName;

    @NotBlank(message = "Country name is mandatory")
    private String countryName;

    private String description;

    @NotNull(message = "Temperature is mandatory")
    private Double temperature;

    private Double humidity;
    private Double windSpeed;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private java.time.LocalDateTime timestamp;
}
