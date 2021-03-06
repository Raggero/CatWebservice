package com.iths.labbspringboot;

import com.iths.labbspringboot.dtos.CatDto;
import com.iths.labbspringboot.entities.Cat;
import com.iths.labbspringboot.repositories.CatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate testClient;

    @Autowired
    CatRepository catRepository;

//  Alla tester körs med en testdatabas genom att konfigurering läses från foldern test i consul som hänvisar till testdatabasen.
    @BeforeEach
    void setUp(){

        Cat cat = new Cat(1,"Simba", "lion", "male", 100.0);
        Cat cat2 = new Cat(2,"Nala", "lion", "female", 90.2);
        catRepository.save(cat);
        catRepository.save(cat2);
    }

    @Test
    void getAllCatsReturnsAllCatsAndStatusOK_GetCatsByGenderReturnsCatsAndStatusOK(){

        var resultGetAll = testClient.getForEntity("http://localhost:"+port+"/cats", CatDto[].class);
        assertThat(resultGetAll.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultGetAll.getBody().length).isEqualTo(2);

        var resultGetMales = testClient.getForEntity("http://localhost:"+port+"/cats/search?gender=male", CatDto[].class);
        assertThat(resultGetMales.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultGetMales.getBody().length).isEqualTo(1);

        var resultGetFemales = testClient.getForEntity("http://localhost:"+port+"/cats/search?gender=female", CatDto[].class);
        assertThat(resultGetFemales.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultGetFemales.getBody().length).isEqualTo(1);
    }

    @Test
    void postNewCatReturnsStatusCreatedOrNotFoundIfIncorrectBody_GetPostedCatReturnsCatAndStatusOk_DeleteCatThenGetCatReturnsStatusNotFound() {

        CatDto catDto = new CatDto(3, "Tiger", "tiger", "female", 80.5);
        var resultPost = testClient.postForEntity("http://localhost:"+port+"/cats", catDto, CatDto.class);
        assertThat(resultPost.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        CatDto catDtoWrong = new CatDto();
        var resultPostWrong = testClient.postForEntity("http://localhost:"+port+"/cats", catDtoWrong, CatDto.class);
        assertThat(resultPostWrong.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        var resultGet = testClient.getForEntity("http://localhost:"+port+"/cats/3", CatDto.class);
        assertThat(resultGet.getBody().getName()).isEqualTo("Tiger");
        assertThat(resultGet.getStatusCode()).isEqualTo(HttpStatus.OK);

        testClient.delete("http://localhost:" + port + "/cats/3", CatDto.class);
        var result = testClient.getForEntity("http://localhost:" + port + "/cats/3", CatDto.class);
        assertThat(result.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    void putChangedCatReturnsCat_PatchWeightChangesOnlyWeightAndReturnsCat(){

        CatDto catDtoPut = new CatDto(1,"Mofasa", "lion", "male", 180.5);
        testClient.put("http://localhost:"+port+"/cats/1", catDtoPut, CatDto.class);
        var resultGet = testClient.getForEntity("http://localhost:"+port+"/cats/1", CatDto.class);
        assertThat(resultGet.getBody().getName()).isEqualTo("Mofasa");
        assertThat(resultGet.getBody().getWeight()).isEqualTo(180.5);
        assertThat(resultGet.getStatusCode()).isEqualTo(HttpStatus.OK);

        CatDto catDtoPatch = new CatDto(1,"Lejon", "lion", "something", 155.2);
        var resultPatch = testClient.patchForObject("http://localhost:"+port+"/cats/1", catDtoPatch, CatDto.class);
        assertThat(resultPatch.getWeight()).isEqualTo(155.2);
        assertThat(resultPatch.getName()).isEqualTo("Mofasa");
        assertThat(resultPatch.getGender()).isEqualTo("male");
    }
}
