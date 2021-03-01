package com.iths.labbspringboot.controllers;

import com.iths.labbspringboot.dtos.CatDto;
import com.iths.labbspringboot.dtos.CatWeight;
import com.iths.labbspringboot.services.CatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CatController {

    private CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping("/cats")
    public List<CatDto> getAllCats(){
        return catService.getAllCats();
    }

    @GetMapping("/cats/{id}")
    public CatDto getOneById(@PathVariable int id){
        return catService.getOneById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @GetMapping(value = "/cats/search", params = "gender")
    public List<CatDto> getCatsByGender(@RequestParam String gender){
        return catService.getByGender(gender);
    }

    @GetMapping(value = "/cats/search", params = "name")
    public List<CatDto> getCatsByName(@RequestParam String name ){
        return catService.getByName(name);
    }

    @PostMapping("/cats")
    @ResponseStatus(HttpStatus.CREATED)
    public CatDto create(@RequestBody CatDto cat ){
        return catService.createCat(cat);
    }

    @DeleteMapping("/cats/{id}")
    public void delete(@PathVariable int id){
        if(!catService.deleteCat(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found");
    }

    @PutMapping("/cats/{id}")
    public CatDto replace(@RequestBody CatDto catDto, @PathVariable int id){
        return catService.replaceCat(id, catDto);
    }

    @PatchMapping("/cats/{id}")
    public CatDto update(@RequestBody CatWeight catWeight, @PathVariable int id){
        return catService.updateCat(id, catWeight);
    }
}
