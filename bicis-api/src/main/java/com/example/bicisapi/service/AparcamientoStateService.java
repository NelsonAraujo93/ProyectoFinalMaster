package com.example.bicisapi.service;

import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.exception.CustomException;
import com.example.bicisapi.repository.AparcamientoStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AparcamientoStateService {

    @Autowired
    private AparcamientoStateRepository aparcamientoStateRepository;

    public Optional<AparcamientoState> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant start, Instant end) {
        return aparcamientoStateRepository.findByAparcamientoIdAndTimestampBetween(aparcamientoId, start, end);
    }

    public AparcamientoState save(AparcamientoState aparcamientoState) {
        return aparcamientoStateRepository.save(aparcamientoState);
    }

    public AparcamientoState createEvent(String aparcamientoId, AparcamientoState newState) {
      newState.setTimestamp(Instant.now());
      Optional<AparcamientoState> lastState = aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);

      if (lastState.isPresent()) {
          newState.setBikesAvailable(lastState.get().getBikesAvailable());
          newState.setFreeParkingSpots(lastState.get().getFreeParkingSpots());

          switch (newState.getOperation()) {
              case "rent":
                  if (lastState.get().getBikesAvailable() <= 0) {
                    throw new CustomException("No bikes available");
                  }
                  newState.setAmountOfBikes(1);
                  newState.setBikesAvailable(lastState.get().getBikesAvailable() - 1);
                  newState.setFreeParkingSpots(lastState.get().getFreeParkingSpots() + 1);
                  break;
              case "return":
                  if (lastState.get().getFreeParkingSpots() <= 0) {
                    throw new CustomException("No free parking spots available");
                  }
                  newState.setAmountOfBikes(1);
                  newState.setBikesAvailable(lastState.get().getBikesAvailable() + 1);
                  newState.setFreeParkingSpots(lastState.get().getFreeParkingSpots() - 1);
                  break;
              case "rent_multiple":
                  if (lastState.get().getBikesAvailable() < newState.getAmountOfBikes()) {
                    throw new CustomException("Not enough bikes available");
                  }
                  newState.setBikesAvailable(lastState.get().getBikesAvailable() - newState.getAmountOfBikes());
                  newState.setFreeParkingSpots(lastState.get().getFreeParkingSpots() + newState.getAmountOfBikes());
                  break;
              case "return_multiple":
                  if (lastState.get().getFreeParkingSpots() < newState.getAmountOfBikes()) {
                    throw new CustomException("Not enough free parking spots available");
                  }
                  newState.setBikesAvailable(lastState.get().getBikesAvailable() + newState.getAmountOfBikes());
                  newState.setFreeParkingSpots(lastState.get().getFreeParkingSpots() - newState.getAmountOfBikes());
                  break;
              default:
                  throw new IllegalStateException("Unexpected value: " + newState.getOperation());
          }
      } else {
          if ("open".equals(newState.getOperation())) {
              newState.setBikesAvailable(newState.getBikesAvailable());
              newState.setFreeParkingSpots(newState.getFreeParkingSpots());
          } else {
              throw new IllegalStateException("No initial state found for aparcamiento: " + aparcamientoId);
          }
      }

      return aparcamientoStateRepository.save(newState);
  }

    public List<AparcamientoState> findByAparcamientoId(String aparcamientoId) {
        return aparcamientoStateRepository.findByAparcamientoId(aparcamientoId);
    }

    public List<AparcamientoState> findTop10ByTimeStamp() {
        return aparcamientoStateRepository.findTop10ByTimestamp();
    }

    public Optional<AparcamientoState> findTopByAparcamientoIdOrderByTimestampDesc(String aparcamientoId) {
        return aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);
    }

    public List<AparcamientoState> findTop10ByBikesAvailable() {
      return aparcamientoStateRepository.findTop10ByBikesAvailable();
  }
}
