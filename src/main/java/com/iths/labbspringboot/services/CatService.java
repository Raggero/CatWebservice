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
    private final CatRepository catRepository;

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
        if(checkNotEmpty(catDto)){
            return catMapper.mapp(catRepository.save(catMapper.mapp(catDto)));
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid input. All attributes of the cat must be sent");
        }
    }

    public boolean deleteCat(int id) {
        if(catRepository.findById(id).isPresent()){
            catRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CatDto replaceCat(int id, CatDto catDto) {
        Optional<Cat> cat = catRepository.findById(id);
        if (cat.isPresent() && checkNotEmpty(catDto)) {
                Cat updatedCat = cat.get();
                updatedCat.setName(catDto.getName());
                updatedCat.setType(catDto.getType());
                updatedCat.setGender(catDto.getGender());
                updatedCat.setWeight(catDto.getWeight());
                return catMapper.mapp(catRepository.save(updatedCat));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found or invalid input.");
            }
    }

    private boolean checkNotEmpty(CatDto catDto) {
        if(catDto.getName() == null || catDto.getGender() == null || catDto.getType() == null || (catDto.getWeight() < 0.01))
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found");
        }
    }

    public List<CatDto> getByGender(String gender) {
        return catMapper.mapp(catRepository.findAllByGender(gender));
    }

    public List<CatDto> getByName(String name) {
        return catMapper.mapp(catRepository.findAllByName(name));
    }
}
