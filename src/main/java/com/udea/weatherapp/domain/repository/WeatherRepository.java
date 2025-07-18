package com.udea.weatherapp.domain.repository;

import com.udea.weatherapp.domain.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findFirstByCityIdOrderByTimestampDesc(Long cityId);
}
