package com.udea.weatherapp.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class WeatherDto extends RepresentationModel<WeatherDto> {

    @Schema(description = "Identificador único del registro de clima", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre de la ciudad", example = "Bogota", required = true)
    @NotBlank(message = "City name is mandatory")
    private String cityName;

    @Schema(description = "Nombre del país", example = "Colombia", required = true)
    @NotBlank(message = "Country name is mandatory")
    private String countryName;

    @Schema(description = "Descripción del clima", example = "Lluvioso")
    private String description;

    @Schema(description = "Temperatura en grados Celsius", example = "14.0", required = true)
    @NotNull(message = "Temperature is mandatory")
    private Double temperature;

    @Schema(description = "Porcentaje de humedad", example = "85.5")
    private Double humidity;

    @Schema(description = "Velocidad del viento en km/h", example = "20.0")
    private Double windSpeed;

    @Schema(description = "Fecha y hora de la medición", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private java.time.LocalDateTime timestamp;
}
