// ManagerController.java
package com.csse.hospital.controller;

import com.csse.hospital.model.user.Manager;
import com.csse.hospital.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    ManagerRepository managerRepository;

    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerRepository.findAll();
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long id) {
        Optional<Manager> manager = managerRepository.findById(id);
        return manager.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        Manager savedManager = managerRepository.save(manager);
        return new ResponseEntity<>(savedManager, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable Long id, @RequestBody Manager manager) {
        Optional<Manager> existingManager = managerRepository.findById(id);
        if (existingManager.isPresent()) {
            Manager updatedManager = existingManager.get();
            updatedManager.setName(manager.getName());
            updatedManager.setEmail(manager.getEmail());
            return new ResponseEntity<>(managerRepository.save(updatedManager), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteManager(@PathVariable Long id) {
        try {
            managerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}