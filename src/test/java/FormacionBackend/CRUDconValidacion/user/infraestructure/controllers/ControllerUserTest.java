package FormacionBackend.CRUDconValidacion.user.infraestructure.controllers;


import FormacionBackend.CRUDconValidacion.user.domain.User;
import FormacionBackend.CRUDconValidacion.user.infraestructure.dtos.UserInputDTO;
import FormacionBackend.CRUDconValidacion.user.infraestructure.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;

import lombok.extern.slf4j.Slf4j;

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
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // I'm using this because I want the function starting to not be  static
@Slf4j
public class ControllerUserTest {

    @LocalServerPort
    int puerto; // port where the app is running

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void insertUser() throws Exception {

        //Creo userInputDTO para mandarselo al metodo insertUser del copntroller
        UserInputDTO userInputDTO = new UserInputDTO("Manuel", 46, "12345", "Manuel", "Rodriguez", "manuel@bosonit.com", "manuel@mail.com", "Logroño", true, new Date(), "", new Date());
        //Realizo llamada al metodo
        ResponseEntity<UserInputDTO> entity = testRestTemplate.postForEntity("http://localhost:" + puerto + "/user/", userInputDTO, UserInputDTO.class);
        //Creo lista de usuarios con todos los del repositorio, si funciona bien, deberia de tener longitud dos, el nurvo de ahora, y el existente al inicirar test.
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(users.get(0).getName(), "Manuel");
        userRepository.deleteAll();

    }

    @Test
    public void getUser() throws URISyntaxException {

        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);

        //Llamamos al metodo pasandole el id 1
        ResponseEntity<User> entity = testRestTemplate.getForEntity("http://localhost:" + puerto + "/user/obtener/" + user.getIdUser(), User.class);
        //Obtengo el usuario
        User body = entity.getBody();
        //Y compruebo que se llama Pablo
        Assertions.assertEquals(body.getName(), "Pablo");
        userRepository.deleteAll();
    }

    @Test
    public void updateUser() throws URISyntaxException {

        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);

        //Creo userInputDTO para mandarselo al metodo updateUser del copntroller
        UserInputDTO userInputDTO = new UserInputDTO("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Villar de Torre", true, new Date(), "", new Date());

        //Llamamos al metodo quie actualiza usuarios
        ResponseEntity<UserInputDTO> entity = testRestTemplate.postForEntity("http://localhost:" + puerto + "/user/actualizar/" + user.getIdUser(), userInputDTO, UserInputDTO.class);


        Assertions.assertEquals(userRepository.findById(user.getIdUser()).get().getCity(), "Villar de Torre");

        userRepository.deleteAll();
    }


    @Test
    public void deleteUser() throws URISyntaxException {

        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);
        //SI FALLA CAMBIAR ELK ID POR: USER.GETiD

        //We create URL for the connection
        RequestEntity<Void> request = RequestEntity.delete(new URI("http://localhost:" + puerto + "/user/eliminar/" + user.getIdUser())).accept(MediaType.APPLICATION_JSON).build();

        //Use this so it can return a List
        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {
        };

        //Response entity exchange called responseEntity
        ResponseEntity<List<User>> responseEntity = testRestTemplate.exchange(request, myList);

        //we check that it returns a 200
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);

        //We create a list with all the User entities of the repository
        List<User> users = userRepository.findAll();
        //having eliminated the only one we had, it must be 0
        Assertions.assertEquals(users.size(), 0);

    }


    @Test
    @DisplayName("Running app wihtout mockito")
    public void findAll() throws URISyntaxException {

        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);

        RequestEntity<Void> request = RequestEntity.get(new URI("http://localhost:" + puerto + "/user/findAll")).accept(MediaType.APPLICATION_JSON).build();  // we create connection URL

        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {
        }; // Use this so it can return a List
        ResponseEntity<List<User>> responseEntity = testRestTemplate.exchange(request, myList);

        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        var respuesta = responseEntity.getBody();
        Assertions.assertEquals(respuesta.size(), 1);
        Assertions.assertEquals(respuesta.get(0).getName(), "Pablo");
    }

    @Test
    public void findByName() throws URISyntaxException {

        //Creo otro User para comprobar que solo obtenemos el de dicho nombre:

        User user = new User("Chuchi", 26, "12345", "JesusJavier", "Puente", "jesus@bosonit.com", "jesus@mail.com", "Logroño", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);

        RequestEntity<Void> request = RequestEntity.get(new URI("http://localhost:" + puerto + "/user/findByName/JesusJavier")).accept(MediaType.APPLICATION_JSON).build();  // we create connection URL

        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {
        }; // Use this so it can return a List
        ResponseEntity<List<User>> responseEntity = testRestTemplate.exchange(request, myList);
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        var respuesta = responseEntity.getBody();
        Assertions.assertEquals(respuesta.size(), 1);

    }

}
