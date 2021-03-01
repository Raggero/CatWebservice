package com.iths.labbspringboot.repositories;

import com.iths.labbspringboot.entities.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat, Integer> {

    List<Cat> findAllByGender(String gender);
    List<Cat> findAllByName(String name);

}
