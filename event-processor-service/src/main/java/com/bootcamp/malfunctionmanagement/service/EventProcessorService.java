package com.bootcamp.malfunctionmanagement.service;

import com.bootcamp.malfunctionmanagement.model.Event;
import com.bootcamp.malfunctionmanagement.model.Malfunction;
import com.bootcamp.malfunctionmanagement.repository.DetailRepository;
import com.bootcamp.malfunctionmanagement.repository.MalfunctionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EventProcessorService {
    private final MalfunctionRepository malfunctionRepository;
    private final DetailRepository detailRepository;
    private final MalfunctionService malfunctionService;

    public EventProcessorService(MalfunctionRepository malfunctionRepository, DetailRepository detailRepository, MalfunctionService malfunctionService) {
        this.malfunctionRepository = malfunctionRepository;
        this.detailRepository = detailRepository;
        this.malfunctionService = malfunctionService;
    }

    public void constructMalfunction(Event event){
        //Construction of malfunctions
        event.getDetails().setDetailId(event.getMalfunction_id());
        detailRepository.save(event.getDetails());

        if(!malfunctionRepository.existsById(event.getMalfunction_id())){
            List<String> stage = new ArrayList<String>();
            stage.add(event.getStage());
            Malfunction malfunction = new Malfunction(
                    event.getMalfunction_id(),
                    event.getTimestamp(),
                    event.getTimestamp(),
                    stage,
                    event.getDetails());

            malfunctionRepository.save(malfunction);
        }
        else{
            Malfunction malfunction = malfunctionService.findMalfunctionByMalfunctionId(event.getMalfunction_id()).get();

            if(!malfunction.getStage().get(0).equals(event.getStage())) {
                malfunction.getStage().add(0, event.getStage());
            }
            //-----
            if(malfunction.getStartTimestamp() <= event.getTimestamp()){
                malfunction.setStartTimestamp(malfunction.getStartTimestamp());
            }
            else if(malfunction.getStartTimestamp() > event.getTimestamp()){
                malfunction.setStartTimestamp(event.getTimestamp());
            }

            if(malfunction.getEndTimestamp() >= event.getTimestamp()){
                malfunction.setEndTimestamp(malfunction.getEndTimestamp());
            }
            else if(malfunction.getEndTimestamp() < event.getTimestamp()){
                malfunction.setEndTimestamp(event.getTimestamp());
            }
            //-----

            malfunction.setDetails(event.getDetails());
            malfunctionRepository.save(malfunction);
        }
    }
}