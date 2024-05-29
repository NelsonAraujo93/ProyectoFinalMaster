package com.example.bicisapi.service;

import com.example.bicisapi.dao.AparcamientoFreeParkingSpotsDTO;
import com.example.bicisapi.dao.AparcamientoStateDTO;
import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.repository.AparcamientoRepository;
import com.example.bicisapi.repository.AparcamientoStateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AparcamientoService {

    @Autowired
    private AparcamientoRepository aparcamientoRepository;
    
    @Autowired
    private AparcamientoStateRepository aparcamientoStateRepository;


    private final Logger logger = Logger.getLogger(AparcamientoService.class.getName());

    public List<Aparcamiento> findAll() {
        return aparcamientoRepository.findAll();
    }

    public Optional<Aparcamiento> findById(String id) {
        return aparcamientoRepository.findById(id);
    }

    public Aparcamiento save(Aparcamiento aparcamiento) {
        return aparcamientoRepository.save(aparcamiento);
    }

    public void deleteById(Long id) {
        aparcamientoRepository.deleteById(id);
    }

    public List<Aparcamiento> findNearestAparcamientos(double lat, double lon) {
        logger.info("Finding nearest aparcamientos");
        List<Aparcamiento> nearestAparcamientos = aparcamientoRepository.findNearestAparcamientos(lat, lon);
        logger.info("Found " + nearestAparcamientos.size() + " nearest aparcamientos");
        return nearestAparcamientos;
    }

    public Optional<AparcamientoState> findLatestAparcamientoStateById(String aparcamientoId) {
        logger.info("Finding latest availability of bikes in aparcamiento: " + aparcamientoId);
        List<AparcamientoState> aparcamientoStates = aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);
        if (aparcamientoStates.isEmpty()) {
            logger.info("No state found for aparcamiento: " + aparcamientoId);
            return Optional.empty();
        } else {
            logger.info("Found state for aparcamiento: " + aparcamientoId);
            logger.info("Bikes available: " + aparcamientoStates.get(0).getBikesAvailable());
            return Optional.of(aparcamientoStates.get(0));
        }
    }

    public AparcamientoStateDTO findNearestAparcamientoWithBikes(double lat, double lon) {
        List<Aparcamiento> nearestAparcamientos = findNearestAparcamientos(lat, lon);
        for (Aparcamiento aparcamiento : nearestAparcamientos) {
            Optional<AparcamientoState> aparcamientoState = findLatestAparcamientoStateById(aparcamiento.getId().toString());
            if (aparcamientoState.isPresent() && aparcamientoState.get().getBikesAvailable() > 0) {
                logger.info("Found aparcamiento with available bikes: " + aparcamiento.getId());

                AparcamientoStateDTO aparcamientoDTO = new AparcamientoStateDTO();
                aparcamientoDTO.setId(aparcamiento.getId());
                aparcamientoDTO.setDirection(aparcamiento.getDirection());
                aparcamientoDTO.setBikesCapacity(aparcamiento.getBikesCapacity());
                aparcamientoDTO.setLatitude(aparcamiento.getLatitud());
                aparcamientoDTO.setLongitude(aparcamiento.getLongitud());
                aparcamientoDTO.setCurrentBikeAvailability(aparcamientoState.get().getBikesAvailable());
                aparcamientoDTO.setFreeParkingSpots(aparcamientoState.get().getFreeParkingSpots());
                return aparcamientoDTO;
            }
        }
        throw new RuntimeException("No available bikes found in nearest aparcamientos");
    }

     public List<AparcamientoFreeParkingSpotsDTO> calculateAverageFreeParkingSpots() {
        List<Aparcamiento> aparcamientos = aparcamientoRepository.findAll();
        List<AparcamientoFreeParkingSpotsDTO> result = new ArrayList<>();

        for (Aparcamiento aparcamiento : aparcamientos) {
            List<AparcamientoState> states = aparcamientoStateRepository.findByAparcamientoId(aparcamiento.getId().toString());
            double averageFreeSpots = states.stream()
                                            .mapToInt(AparcamientoState::getBikesAvailable)
                                            .average()
                                            .orElse(0);
            AparcamientoFreeParkingSpotsDTO dto = new AparcamientoFreeParkingSpotsDTO();
            dto.setAparcamientoId(aparcamiento.getId());
            dto.setAverageBikesAvailable(averageFreeSpots);
            dto.setLatitud(aparcamiento.getLatitud());
            dto.setLongitud(aparcamiento.getLongitud());
            result.add(dto);
        }

        return result;
    }
}
