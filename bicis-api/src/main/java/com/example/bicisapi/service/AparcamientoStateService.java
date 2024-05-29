package com.example.bicisapi.service;

import com.example.bicisapi.domain.Aparcamiento;
import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.exception.CustomException;
import com.example.bicisapi.repository.AparcamientoStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AparcamientoStateService {

    @Autowired
    private AparcamientoStateRepository aparcamientoStateRepository;

    @Autowired
    private AparcamientoService aparcamientoService;

    public List<AparcamientoState> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant from, Instant to) {
      return aparcamientoStateRepository.findByAparcamientoIdAndTimestampBetween(aparcamientoId, from, to);
    }

    public AparcamientoState save(AparcamientoState aparcamientoState) {
        return aparcamientoStateRepository.save(aparcamientoState);
    }

    public AparcamientoState createEvent(String aparcamientoId, AparcamientoState newState) {
        Logger logger = Logger.getLogger(AparcamientoStateService.class.getName());
    
        newState.setTimestamp(Instant.now());
        List<AparcamientoState> lastState = aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);
    
        logger.info("Creating event for aparcamientoId: " + aparcamientoId);

        logger.info("Last state found: " + lastState.size());
        if (!lastState.isEmpty()) {
            AparcamientoState previousState = lastState.get(0);
            newState.setBikesAvailable(previousState.getBikesAvailable());
            newState.setFreeParkingSpots(previousState.getFreeParkingSpots());
    
            logger.info("Previous state found: " + previousState.getAmountOfBikes());
            switch (newState.getOperation()) {
                case "rent":
                    if (previousState.getBikesAvailable() <= 0) {
                        logger.warning("No bikes available to rent.");
                        throw new CustomException("No bikes available");
                    }
                    newState.setAmountOfBikes(1);
                    newState.setBikesAvailable(previousState.getBikesAvailable() - 1);
                    newState.setFreeParkingSpots(previousState.getFreeParkingSpots() + 1);
                    logger.info("Rent operation: Bikes available after operation: " + newState.getBikesAvailable());
                    break;
                case "return":
                    if (previousState.getFreeParkingSpots() <= 0) {
                        logger.warning("No free parking spots available for return.");
                        throw new CustomException("No free parking spots available");
                    }
                    newState.setAmountOfBikes(1);
                    newState.setBikesAvailable(previousState.getBikesAvailable() + 1);
                    newState.setFreeParkingSpots(previousState.getFreeParkingSpots() - 1);
                    logger.info("Return operation: Bikes available after operation: " + newState.getBikesAvailable());
                    break;
                case "rent_multiple":
                    if (previousState.getBikesAvailable() < newState.getAmountOfBikes()) {
                        logger.warning("Not enough bikes available to rent multiple.");
                        throw new CustomException("Not enough bikes available");
                    }
                    newState.setBikesAvailable(previousState.getBikesAvailable() - newState.getAmountOfBikes());
                    newState.setFreeParkingSpots(previousState.getFreeParkingSpots() + newState.getAmountOfBikes());
                    logger.info("Rent multiple operation: Bikes available after operation: " + newState.getBikesAvailable());
                    break;
                case "return_multiple":
                    if (previousState.getFreeParkingSpots() < newState.getAmountOfBikes()) {
                        logger.warning("Not enough free parking spots available to return multiple.");
                        throw new CustomException("Not enough free parking spots available");
                    }
                    newState.setBikesAvailable(previousState.getBikesAvailable() + newState.getAmountOfBikes());
                    newState.setFreeParkingSpots(previousState.getFreeParkingSpots() - newState.getAmountOfBikes());
                    logger.info("Return multiple operation: Bikes available after operation: " + newState.getBikesAvailable());
                    break;
                default:
                    logger.severe("Unexpected operation: " + newState.getOperation());
                    throw new CustomException("Unexpected operation: " + newState.getOperation());
            }
        } else {
            Aparcamiento aparcamiento = aparcamientoService.findById(aparcamientoId).orElseThrow(() -> new CustomException("Aparcamiento not found"));
            newState.setBikesAvailable(aparcamiento.getBikesCapacity());
            newState.setFreeParkingSpots(0);
    
            logger.info("No previous state found. Initializing new state for aparcamiento with capacity: " + aparcamiento.getBikesCapacity());
            switch (newState.getOperation()) {
                case "rent":
                    if (aparcamiento.getBikesCapacity() <= 0) {
                        logger.warning("No bikes available to rent.");
                        throw new CustomException("No bikes available");
                    }
                    newState.setAmountOfBikes(1);
                    newState.setBikesAvailable(aparcamiento.getBikesCapacity() - 1);
                    newState.setFreeParkingSpots(1);
                    logger.info("Rent operation: Bikes available after operation: " + newState.getBikesAvailable());
                    break;
                case "return":
                    logger.warning("No free parking spots available for return.");
                    throw new CustomException("No free parking spots available");
                case "rent_multiple":
                    if (aparcamiento.getBikesCapacity() < newState.getAmountOfBikes()) {
                        logger.warning("Not enough bikes available to rent multiple." + aparcamiento.getBikesCapacity() + " " + newState.getAmountOfBikes());
                        throw new CustomException("Not enough bikes available");
                    }
                    newState.setBikesAvailable(aparcamiento.getBikesCapacity() - newState.getAmountOfBikes());
                    newState.setFreeParkingSpots(newState.getAmountOfBikes());
                    logger.info("Rent multiple operation: Bikes available after operation: " + newState.getBikesAvailable());
                    break;
                case "return_multiple":
                    logger.warning("Not enough free parking spots available for return multiple.");
                    throw new CustomException("Not enough free parking spots available");
                default:
                    logger.severe("Unexpected operation: " + newState.getOperation());
                    throw new CustomException("Unexpected operation: " + newState.getOperation());
            }
        }
    
        logger.info("New state created: " + newState);
        return aparcamientoStateRepository.save(newState);
    }
    

    public List<AparcamientoState> findByAparcamientoId(String aparcamientoId) {
        return aparcamientoStateRepository.findByAparcamientoId(aparcamientoId);
    }

    public List<AparcamientoState> findTop10ByTimeStamp() {
        return aparcamientoStateRepository.findTop10ByTimestamp();
    }

    public List<AparcamientoState> findTopByAparcamientoIdOrderByTimestampDesc(String aparcamientoId) {
        return aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);
    }

    public List<AparcamientoState> findTop10ByBikesAvailable() {
      return aparcamientoStateRepository.findTop10ByBikesAvailable();
  }
}
