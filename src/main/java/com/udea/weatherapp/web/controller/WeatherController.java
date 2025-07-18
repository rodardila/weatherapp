package com.udea.weatherapp.web.controller;

import com.udea.weatherapp.service.WeatherService;
import com.udea.weatherapp.web.dto.WeatherDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(summary = "Consultar el clima actual", description = "Obtiene el registro meteorológico más reciente para una ciudad y país específicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clima encontrado exitosamente", content = @Content(schema = @Schema(implementation = WeatherDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró el clima para la ciudad especificada")
    })
    @GetMapping
    public ResponseEntity<WeatherDto> getWeatherByCity(
            @Parameter(description = "Nombre de la ciudad. Ejemplo: Medellin", required = true) @RequestParam String city,
            @Parameter(description = "Nombre del país. Ejemplo: Colombia", required = true) @RequestParam String country) {
        return weatherService.getWeather(city, country)
                .map(weatherDto -> {
                    // Add HATEOAS self link
                    weatherDto.add(linkTo(methodOn(WeatherController.class).getWeatherByCity(city, country)).withSelfRel());
                    return ResponseEntity.ok(weatherDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo dato de clima", description = "Crea un nuevo registro de clima para una ciudad. Si la ciudad no existe, se crea automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dato de clima registrado exitosamente", content = @Content(schema = @Schema(implementation = WeatherDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<WeatherDto> addWeather(@Valid @RequestBody WeatherDto weatherDto) {
        WeatherDto savedDto = weatherService.addWeather(weatherDto);

        // Add HATEOAS self link
        savedDto.add(linkTo(methodOn(WeatherController.class).getWeatherByCity(savedDto.getCityName(), savedDto.getCountryName())).withSelfRel());

        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(methodOn(WeatherController.class)
                .getWeatherByCity(savedDto.getCityName(), savedDto.getCountryName())).toUri()).body(savedDto);
    }
}
