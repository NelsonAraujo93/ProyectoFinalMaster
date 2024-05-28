package com.example.bicisapi.service;

import com.example.bicisapi.domain.AparcamientoState;
import com.example.bicisapi.exception.CustomException;
import com.example.bicisapi.repository.AparcamientoStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AparcamientoStateService {

    @Autowired
    private AparcamientoStateRepository aparcamientoStateRepository;

    public List<AparcamientoState> findByAparcamientoIdAndTimestampBetween(String aparcamientoId, Instant from, Instant to) {
      return aparcamientoStateRepository.findByAparcamientoIdAndTimestampBetween(aparcamientoId, from, to);
    }

    public AparcamientoState save(AparcamientoState aparcamientoState) {
        return aparcamientoStateRepository.save(aparcamientoState);
    }

    public AparcamientoState createEvent(String aparcamientoId, AparcamientoState newState) {
      newState.setTimestamp(Instant.now());
      List<AparcamientoState> lastState = aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);

      if (lastState.size() > 0){
          newState.setBikesAvailable(lastState.get(0).getBikesAvailable());
          newState.setFreeParkingSpots(lastState.get(0).getFreeParkingSpots());

          switch (newState.getOperation()) {
              case "rent":
                  if (lastState.get(0).getBikesAvailable() <= 0) {
                    throw new CustomException("No bikes available");
                  }
                  newState.setAmountOfBikes(1);
                  newState.setBikesAvailable(lastState.get(0).getBikesAvailable() - 1);
                  newState.setFreeParkingSpots(lastState.get(0).getFreeParkingSpots() + 1);
                  break;
              case "return":
                  if (lastState.get(0).getFreeParkingSpots() <= 0) {
                    throw new CustomException("No free parking spots available");
                  }
                  newState.setAmountOfBikes(1);
                  newState.setBikesAvailable(lastState.get(0).getBikesAvailable() + 1);
                  newState.setFreeParkingSpots(lastState.get(0).getFreeParkingSpots() - 1);
                  break;
              case "rent_multiple":
                  if (lastState.get(0).getBikesAvailable() < newState.getAmountOfBikes()) {
                    throw new CustomException("Not enough bikes available");
                  }
                  newState.setBikesAvailable(lastState.get(0).getBikesAvailable() - newState.getAmountOfBikes());
                  newState.setFreeParkingSpots(lastState.get(0).getFreeParkingSpots() + newState.getAmountOfBikes());
                  break;
              case "return_multiple":
                  if (lastState.get(0).getFreeParkingSpots() < newState.getAmountOfBikes()) {
                    throw new CustomException("Not enough free parking spots available");
                  }
                  newState.setBikesAvailable(lastState.get(0).getBikesAvailable() + newState.getAmountOfBikes());
                  newState.setFreeParkingSpots(lastState.get(0).getFreeParkingSpots() - newState.getAmountOfBikes());
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

    public List<AparcamientoState> findTopByAparcamientoIdOrderByTimestampDesc(String aparcamientoId) {
        return aparcamientoStateRepository.findTopByAparcamientoIdOrderByTimestampDesc(aparcamientoId);
    }

    public List<AparcamientoState> findTop10ByBikesAvailable() {
      return aparcamientoStateRepository.findTop10ByBikesAvailable();
  }
}
