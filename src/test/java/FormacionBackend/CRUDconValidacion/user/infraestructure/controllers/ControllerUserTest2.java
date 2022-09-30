package FormacionBackend.CRUDconValidacion.user.infraestructure.controllers;

import FormacionBackend.CRUDconValidacion.exceptions.EntityNotFoundException;
import FormacionBackend.CRUDconValidacion.user.domain.User;
import FormacionBackend.CRUDconValidacion.user.infraestructure.dtos.UserInputDTO;
import FormacionBackend.CRUDconValidacion.user.infraestructure.dtos.UserOutputDTO;
import FormacionBackend.CRUDconValidacion.user.infraestructure.repository.UserRepository;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


import java.net.MalformedURLException;
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

        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);
    }


    @Test
    public void insertUser() throws URISyntaxException {


      /*  //Creo UserInputDTO
        UserInputDTO userInputDTO = new UserInputDTO("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, new Date(), "", new Date());

        RequestEntity<Void> request= RequestEntity.post(new URI("http://localhost:" + puerto + "/user")).accept(MediaType.APPLICATION_JSON).build();

        //Use this so it can return a List
        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {};

        //Response entity exchange called responseEntity
        ResponseEntity<List<User>> responseEntity= testRestTemplate.exchange(request, myList);

        //we check that it returns a 200
        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);

        //We create a list with all the User entities of the repository
        List<User>users = userRepository.findAll();
        //having eliminated the only one we had, it must be 0
        Assertions.assertEquals(users.size(),2);*/

    }

    @Test
    public void getUser() throws  URISyntaxException {
        //Mi metodo obtener/{id} necesita el id y retorna un UserOutputDTO
        //Necesitaria cambiar List a UserOutputDTO?
        //UserOutputDTO necesita un User para crearse

        /*URI url = new URI("http://localhost:" + puerto + "/user/obtener/" + 1);

        RequestEntity<?> entity = RequestEntity.get(url).build();
        Assertions.assertEquals(HttpMethod.GET, entity.getMethod());*/


        RequestEntity<?> request= RequestEntity.get(new URI("http://localhost:" + puerto + "/user/obtener/" + 1)).accept(MediaType.APPLICATION_JSON).build();  // we create connection URL

        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {}; // Use this so it can return a List
        ResponseEntity<List<User>> responseEntity= testRestTemplate.exchange(request,myList);

        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);
        var respuesta=responseEntity.getBody();
        Assertions.assertEquals(respuesta.size(),1);
        Assertions.assertEquals(respuesta.get(0).getName(),"Pablo");




    }

    @Test
    public void updateUser()throws URISyntaxException {}



    @Test
    public void deleteUser() throws URISyntaxException{

        //We create URL for the connection
        RequestEntity<Void> request= RequestEntity.delete(new URI("http://localhost:" + puerto + "/user/eliminar/" + 1)).accept(MediaType.APPLICATION_JSON).build();

        //Use this so it can return a List
        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {};

        //Response entity exchange called responseEntity
        ResponseEntity<List<User>> responseEntity= testRestTemplate.exchange(request, myList);

        //we check that it returns a 200
        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);

        //We create a list with all the User entities of the repository
        List<User>users = userRepository.findAll();
        //having eliminated the only one we had, it must be 0
        Assertions.assertEquals(users.size(),0);

    }


    @Test
    @DisplayName("Running app wihtout mockito")
    public void findAll() throws URISyntaxException {

        RequestEntity<Void> request= RequestEntity.get(new URI("http://localhost:" + puerto + "/user/findAll")).accept(MediaType.APPLICATION_JSON).build();  // we create connection URL

        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {}; // Use this so it can return a List
        ResponseEntity<List<User>> responseEntity= testRestTemplate.exchange(request, myList);

        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);
        var respuesta=responseEntity.getBody();
        Assertions.assertEquals(respuesta.size(),1);
        Assertions.assertEquals(respuesta.get(0).getName(),"Pablo");
    }

    @Test
    public void findByName() throws URISyntaxException {

        //Creo otro User para comprobar que solo obtenemos el de dicho nombre:

        User user = new User("Chuchi", 26, "12345", "JesusJavier", "Puente", "jesus@bosonit.com", "jesus@mail.com", "Logroño", true, new Date(), "", new Date());
        //Lo guardo
        userRepository.save(user);

        RequestEntity<Void> request= RequestEntity.get(new URI("http://localhost:" + puerto + "/user/findByName/JesusJavier")).accept(MediaType.APPLICATION_JSON).build();  // we create connection URL

        ParameterizedTypeReference<List<User>> myList = new ParameterizedTypeReference<List<User>>() {}; // Use this so it can return a List
        ResponseEntity<List<User>> responseEntity= testRestTemplate.exchange(request, myList);
        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);
        var respuesta=responseEntity.getBody();
        Assertions.assertEquals(respuesta.size(),1);


    }

}
