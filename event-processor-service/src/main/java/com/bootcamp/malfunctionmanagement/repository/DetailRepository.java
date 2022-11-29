package com.bootcamp.malfunctionmanagement.repository;

import com.bootcamp.malfunctionmanagement.model.Detail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends CrudRepository<Detail, String> {
}
