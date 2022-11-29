package com.bootcamp.malfunctionmanagement.repository;

import com.bootcamp.malfunctionmanagement.model.Malfunction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MalfunctionRepository extends CrudRepository<Malfunction, String> {
    
}
