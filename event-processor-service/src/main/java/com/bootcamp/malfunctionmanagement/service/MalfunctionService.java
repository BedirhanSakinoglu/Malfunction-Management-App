package com.bootcamp.malfunctionmanagement.service;

import com.bootcamp.malfunctionmanagement.model.Malfunction;
import com.bootcamp.malfunctionmanagement.repository.MalfunctionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class MalfunctionService {
    private final MalfunctionRepository malfunctionRepository;

    public List<Malfunction> findAllMalfunctions(){
        return (List<Malfunction>) malfunctionRepository.findAll();
    }

    public Optional<Malfunction> findMalfunctionByMalfunctionId(String malfunctionId){
        System.out.println(malfunctionRepository.findById(malfunctionId).get().getMalfunctionId());
        return malfunctionRepository.findById(malfunctionId);
    }

    public Malfunction updateMalfunction(Malfunction malfunction){
        Malfunction oldMalfunction = findMalfunctionByMalfunctionId(malfunction.getMalfunctionId()).get();
        oldMalfunction.getStage().add(0, malfunction.getStage().get(0));
        List<String> stageList = oldMalfunction.getStage();
        malfunction.setStage(stageList);
        return malfunctionRepository.save(malfunction);
    }

    public void deleteMalfunction(String id){
        malfunctionRepository.deleteById(id);
    }

    public void deleteAllMalfunctions(){
        malfunctionRepository.deleteAll();
    }
}
