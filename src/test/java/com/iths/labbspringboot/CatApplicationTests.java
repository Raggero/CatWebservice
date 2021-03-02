package com.iths.labbspringboot;

import com.iths.labbspringboot.dtos.CatDto;
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

    @Test
    void contextLoads() {
        var result = testClient.getForEntity("http://localhost:"+port+"/cats", CatDto[].class);
        assertThat(result.getBody().length).isGreaterThan(0);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postNewCatToServiceReturnsStatusCreated() {

        CatDto catDto = new CatDto(2, "TestKatt", "Lion", "Male", 55.5);
        var result = testClient.postForEntity("http://localhost:"+port+"/cats", catDto, CatDto.class);
//        assertThat(result.getBody().getId()).isEqualTo(2);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

//        Kan verifieras genom get request for person with id
    }
    @Test
    void getRequestedCatByIdReturnsStatusOK() {

        CatDto catDto = new CatDto(2, "TestKatt", "Lion", "Male", 55.5);
        var result = testClient.getForEntity("http://localhost:"+port+"/cats/2", CatDto.class);
//        assertThat(result.getBody().getId()).isEqualTo(2);
        assertThat(result.getBody().getName().equals(catDto.getName()));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteCatThenGetCatReturnsStatusNotFound() {

        testClient.delete("http://localhost:" + port + "/cats/2", CatDto.class);

        var result = testClient.getForEntity("http://localhost:" + port + "/cats/2", CatDto.class);
        assertThat(result.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }
}
