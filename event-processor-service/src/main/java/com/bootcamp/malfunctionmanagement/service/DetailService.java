package com.bootcamp.malfunctionmanagement.service;

import com.bootcamp.malfunctionmanagement.model.Detail;
import com.bootcamp.malfunctionmanagement.repository.DetailRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class DetailService {
    private DetailRepository detailRepository;

    public Detail findDetailById(String id){
        return detailRepository.findById(id).get();
    }

    public Detail updateDetail(Detail detail){
        return detailRepository.save(detail);
    }
}