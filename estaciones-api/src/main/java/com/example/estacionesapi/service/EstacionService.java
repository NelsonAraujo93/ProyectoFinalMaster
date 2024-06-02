package com.example.estacionesapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.estacionesapi.dao.AirQualityDTO;
import com.example.estacionesapi.domain.Estacion;
import com.example.estacionesapi.domain.EstacionLog;
import com.example.estacionesapi.repository.EstacionLogRepository;
import com.example.estacionesapi.repository.EstacionRepository;

@Service
public class EstacionService {
  
  @Autowired
  private EstacionRepository estacionRepository;

  @Autowired
  private EstacionLogRepository estacionLogRepository;

  public List<Estacion> findAll() {
    return estacionRepository.findAll();
  }

  public Optional<Estacion> findById(String id) {
    return estacionRepository.findById(id);
  }

  public Estacion save(Estacion estacion) {
    return estacionRepository.save(estacion);
  }

  public void deleteById(Long id) {
    estacionRepository.deleteById(id);
  }

  public AirQualityDTO getAverageContaminants(double lat, double lon) {
    Estacion closestEstacion = findClosestEstacion(lat, lon);
    List<EstacionLog> logs = estacionLogRepository.findByEstacionId(closestEstacion.getId().toString());

    AirQualityDTO airQualityDTO = new AirQualityDTO();
    airQualityDTO.setAvgNitricOxides(logs.stream().mapToDouble(EstacionLog::getNitricOxides).average().orElse(0.0));
    airQualityDTO.setAvgNitrogenDioxides(logs.stream().mapToDouble(EstacionLog::getNitrogenDioxides).average().orElse(0.0));
    airQualityDTO.setAvgVOCs_NMHC(logs.stream().mapToDouble(EstacionLog::getVOCs_NMHC).average().orElse(0.0));
    airQualityDTO.setAvgPM2_5(logs.stream().mapToDouble(EstacionLog::getPM2_5).average().orElse(0.0));

    return airQualityDTO;
  }

  private Estacion findClosestEstacion(double lat, double lon) {
    List<Estacion> estaciones = estacionRepository.findAll();
    return estaciones.stream().min((e1, e2) -> Double.compare(
            distance(lat, lon, e1.getLatitud(), e1.getLongitud()),
            distance(lat, lon, e2.getLatitud(), e2.getLongitud())
    )).orElseThrow(() -> new RuntimeException("No stations found"));
  }

  private double distance(double lat1, double lon1, double lat2, double lon2) {
    double earthRadius = 6371; // km
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadius * c;
  }
}
