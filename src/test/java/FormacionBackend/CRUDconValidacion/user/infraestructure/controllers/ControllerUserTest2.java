package FormacionBackend.CRUDconValidacion.user.infraestructure.controllers;

import FormacionBackend.CRUDconValidacion.user.domain.User;
import FormacionBackend.CRUDconValidacion.user.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;


/**
 * We are using @SpringBootTest, so SpringBoot run the whole application and listen in a random port.
 * We can send requests to our controller, using HTTP requests using TestRestTemplate
 * We don't use  Mockito.
 *
 * @author Pablo del Pozo
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // I'm using this because I want the function starting to not be  static
@Slf4j
public class ControllerUserTest2 {

    @LocalServerPort
    int puerto; // port where the app is running

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;


    /**
     * Loading Initial data for h2 database.
     */
    @BeforeAll
    public  void starting()
    {
        //necesaria para crear User
        Date date = new Date();
        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //Lo guardo
        userRepository.save(user);
    }


    @Test
    public void insertUser() {

    }

    @Test
    public void getUser() {


    }

    @Test
    public void updateUser() {

    }

    @Test
    public void deleteUser() {


    }

    @Test
    @DisplayName("Running app wihtout mockito")
    public void findAll() throws URISyntaxException {
        RequestEntity<Void> request= RequestEntity.get(new URI("http://localhost:" + puerto + "/user/findAll")) // we create connection URL
                .accept(MediaType.APPLICATION_JSON).build();

        ParameterizedTypeReference<List<User>> myList =
                new ParameterizedTypeReference<List<User>>() {}; // Use this so it can return a List
        ResponseEntity<List<User>> responseEntity= testRestTemplate.exchange(request, myList);

        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);
        var respuesta=responseEntity.getBody();
        Assertions.assertEquals(respuesta.size(),1);
        Assertions.assertEquals(respuesta.get(0).getName(),"Pablo");

    }

}
