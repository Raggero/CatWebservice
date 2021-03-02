package com.iths.labbspringboot.services;

import com.iths.labbspringboot.dtos.CatDto;
import com.iths.labbspringboot.dtos.CatWeight;
import com.iths.labbspringboot.entities.Cat;
import com.iths.labbspringboot.mappers.CatMapper;
import com.iths.labbspringboot.repositories.CatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CatService  {

    private final CatMapper catMapper;
    private CatRepository catRepository;

    public CatService(CatRepository catRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
    }

    public List<CatDto> getAllCats(){
        return catMapper.mapp(catRepository.findAll());
    }


    public Optional<CatDto> getOneById(int id){
        return catMapper.mapp(catRepository.findById(id));
    }

    public CatDto createCat(CatDto catDto){
        //Validate here
        return catMapper.mapp(catRepository.save(catMapper.mapp(catDto)));
    }

    public boolean deleteCat(int id) {
        if(catRepository.findById(id).isPresent()){
            catRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CatDto replaceCat(int id, CatDto catDto) {
        //validera så att man inte försöker skicka in nullparametrar
        Optional<Cat> cat = catRepository.findById(id);
        if(checkNotEmpty(catDto)) {
            if (cat.isPresent()) {
                Cat updatedCat = cat.get();
                updatedCat.setName(catDto.getName());
                updatedCat.setType(catDto.getType());
                updatedCat.setGender(catDto.getGender());
                updatedCat.setWeight(catDto.getWeight());
                return catMapper.mapp(catRepository.save(updatedCat));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not process request");
        }
    }

    private boolean checkNotEmpty(CatDto catDto) {
        if(catDto.getName().isEmpty() || catDto.getGender().isEmpty() || catDto.getType().isEmpty() || (catDto.getWeight() < 0.0))
            return false;
        return true;
    }

    public CatDto updateCat(int id, CatWeight catWeight) {
        Optional<Cat> cat = catRepository.findById(id);
        if(cat.isPresent()){
            Cat updatedCat = cat.get();
            if(catWeight.weight > 0.0)
                updatedCat.setWeight(catWeight.weight);
            return catMapper.mapp(catRepository.save(updatedCat));
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    public List<CatDto> getByGender(String gender) {
        return catMapper.mapp(catRepository.findAllByGender(gender));
    }

    public List<CatDto> getByName(String name) {
        return catMapper.mapp(catRepository.findAllByName(name));
    }
}
