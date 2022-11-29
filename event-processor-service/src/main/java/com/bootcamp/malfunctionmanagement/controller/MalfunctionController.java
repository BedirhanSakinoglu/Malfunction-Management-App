package com.bootcamp.malfunctionmanagement.controller;

import com.bootcamp.malfunctionmanagement.model.Detail;
import com.bootcamp.malfunctionmanagement.model.Malfunction;
import com.bootcamp.malfunctionmanagement.service.DetailService;
import com.bootcamp.malfunctionmanagement.service.MalfunctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping
public class MalfunctionController {
    private final MalfunctionService malfunctionService;
    private final DetailService detailService;

    MalfunctionController(MalfunctionService malfunctionService, DetailService detailService){
        this.malfunctionService = malfunctionService;
        this.detailService = detailService;
    }

    @GetMapping("/malfunctions")
    public List<Malfunction> getAllMalfunctions(){
        return malfunctionService.findAllMalfunctions();
    }

    @GetMapping("/malfunctions/{id}")
    public Optional<Malfunction> getMalfunction(@PathVariable String id){
        return malfunctionService.findMalfunctionByMalfunctionId(id);
    }

    @GetMapping("/malfunctions/details/{id}")
    public Detail getMalfunctionDetails(@PathVariable String id){
        return detailService.findDetailById(id);
    }

    @DeleteMapping("/malfunctions/{id}")
    public ResponseEntity<Void> deleteMalfunction(@PathVariable String id){
        malfunctionService.deleteMalfunction(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/malfunctions/delete")
    public ResponseEntity<Void> deleteAll(){
        malfunctionService.deleteAllMalfunctions();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/malfunctions/{id}")
    public ResponseEntity<Malfunction> updateMalfunction(@PathVariable String id, @RequestBody Malfunction malfunction){
        detailService.updateDetail(malfunction.getDetails());
        malfunctionService.updateMalfunction(malfunction);
        return new ResponseEntity<Malfunction>(malfunction, HttpStatus.OK);
    }

    @PutMapping("/details/{id}")
    public ResponseEntity<Detail> updateDetail(@PathVariable Long id, @RequestBody Detail details){
        detailService.updateDetail(details);
        return new ResponseEntity<Detail>(details, HttpStatus.OK);
    }
}
