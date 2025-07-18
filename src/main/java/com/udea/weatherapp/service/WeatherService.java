package com.udea.weatherapp.service;

import com.udea.weatherapp.domain.model.City;
import com.udea.weatherapp.domain.model.Weather;
import com.udea.weatherapp.domain.repository.CityRepository;
import com.udea.weatherapp.domain.repository.WeatherRepository;
import com.udea.weatherapp.web.dto.WeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;

    public WeatherService(WeatherRepository weatherRepository, CityRepository cityRepository) {
        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Optional<WeatherDto> getWeather(String cityName, String countryName) {
        return cityRepository.findByNameAndCountry(cityName, countryName)
                .flatMap(city -> weatherRepository.findFirstByCityIdOrderByTimestampDesc(city.getId()))
                .map(this::toDto);
    }

    @Transactional
    public WeatherDto addWeather(WeatherDto weatherDto) {
        City city = cityRepository.findByNameAndCountry(weatherDto.getCityName(), weatherDto.getCountryName())
                .orElseGet(() -> {
                    City newCity = new City();
                    newCity.setName(weatherDto.getCityName());
                    newCity.setCountry(weatherDto.getCountryName());
                    return cityRepository.save(newCity);
                });

        Weather weather = new Weather();
        weather.setCity(city);
        weather.setDescription(weatherDto.getDescription());
        weather.setTemperature(weatherDto.getTemperature());
        weather.setHumidity(weatherDto.getHumidity());
        weather.setWindSpeed(weatherDto.getWindSpeed());
        weather.setTimestamp(LocalDateTime.now());

        Weather savedWeather = weatherRepository.save(weather);
        return toDto(savedWeather);
    }

    private WeatherDto toDto(Weather weather) {
        WeatherDto dto = new WeatherDto();
        dto.setId(weather.getId());
        dto.setCityName(weather.getCity().getName());
        dto.setCountryName(weather.getCity().getCountry());
        dto.setDescription(weather.getDescription());
        dto.setTemperature(weather.getTemperature());
        dto.setHumidity(weather.getHumidity());
        dto.setWindSpeed(weather.getWindSpeed());
        dto.setTimestamp(weather.getTimestamp());
        return dto;
    }
}
