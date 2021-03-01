package com.iths.labbspringboot.mappers;

import com.iths.labbspringboot.dtos.CatDto;
import com.iths.labbspringboot.entities.Cat;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatMapper {

    public CatMapper() {
    }

    public CatDto mapp(Cat cat) {
        return new CatDto(cat.getId(), cat.getName(), cat.getType(), cat.getGender(), cat.getWeight());
    }

    public Cat mapp(CatDto catDto) {
        return new Cat(catDto.getId(), catDto.getName(), catDto.getType(), catDto.getGender(), catDto.getWeight());
    }

    public Optional<CatDto> mapp(Optional<Cat> optionalCat) {
        if (optionalCat.isEmpty())
            return Optional.empty();
        return Optional.of(mapp(optionalCat.get()));
    }

    public List<CatDto> mapp(List<Cat> all) {
        return all
                .stream()
                .map(this::mapp)
                .collect(Collectors.toList());
    }
}