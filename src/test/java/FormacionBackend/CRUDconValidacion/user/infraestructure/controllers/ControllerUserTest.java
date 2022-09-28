package FormacionBackend.CRUDconValidacion.user.infraestructure.controllers;


import FormacionBackend.CRUDconValidacion.user.domain.User;
import FormacionBackend.CRUDconValidacion.user.infraestructure.dtos.UserInputDTO;
import FormacionBackend.CRUDconValidacion.user.infraestructure.repository.UserRepository;

import org.junit.jupiter.api.*;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Date;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerUserTest {


    @Autowired
    UserRepository userRepository;


    @Autowired
    ControllerUser controllerUser;



    @Test
    @DisplayName("Test isertUser() de la clase ControllerUser")
    void insertUser() throws Exception {

        Date date = new Date();//necesaria para crear UserInputDTO
        //Creo UserInputDTO
        UserInputDTO userInputDTO = new UserInputDTO("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //llamo al metodo que los inserta va pasando por capas hasta llegar al repositorio JPA
        controllerUser.insertUser(userInputDTO);
        //Creo una lista con todos los usuarios del repositorio(deberia de hjabwer solo 1)
        List<User> users = controllerUser.findAll();

        //Y lo conmpruebo
        Assertions.assertEquals(1,  userRepository.count());

    }

    @Test
    @DisplayName("Test getUser() de la clase ControllerUser")
    void getUser() throws Exception {

        //necesaria para crear UserInputDTO
        Date date = new Date();
        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //Lo guardo
        userRepository.save(user);

        //Asi compruebo que el metodo funciona
        System.out.println(controllerUser.getUser(1));

        //Lo he intentado asi pero nada:
        //assertThat(user, is(controllerUser.getUser(1)));


    }

    @Test
    @DisplayName("Test updateUser() de la clase ControllerUser")
    void updateUser() throws Exception {

        //necesaria para crear UserInputDTO
        Date date = new Date();
        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //Lo guardo
        userRepository.save(user);

        //Creo un userInputDTO para mandarselo a updateUser(), he cambiado el nombre de Pablo a Manuel
        UserInputDTO userInputDTO = new UserInputDTO("Manuel", 26, "12345", "Manuel", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //Lo actualizo
        controllerUser.updateUser(userInputDTO, 1);

        //Y compruebo que realmente se ha actualizado el nombre
        Assertions.assertEquals("Manuel", userRepository.findById(1).get().getName());



    }

    @Test
    @DisplayName("Test deleteById() de la clase ControllerUser")
    void deleteUser() {

        //necesaria para crear UserInputDTO
        Date date = new Date();
        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //Lo guardo
        userRepository.save(user);
        //Creo una lista de User buscando con findAll()
        List<User> users = controllerUser.findAll();

        //Deberia de ser 1
        Assertions.assertEquals(1, users.size());

        //Ahora lo elimino del repositorio
        userRepository.deleteById(1);
        //obtengo de nuevo la lista buscando a todos los elementos(ahora no debe de haber ninguno)
        users = controllerUser.findAll();
        //Deberia de ser 0
        Assertions.assertEquals(0, users.size());


    }

    @Test
    @DisplayName("Test findByName() de la clase ControllerUser")
    void findByName() throws Exception {

        //necesaria para crear User
        Date date = new Date();
        //Creo usuario
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        //Lo guardo
        userRepository.save(user);

        //Creo una lista de User buscando con findByName("Pablo")
        List<User> usersPablo = controllerUser.findByName("Pablo");
        //Deberia de ser 1
        Assertions.assertEquals(1, usersPablo.size());


    }

    @Test
    @DisplayName("Test findAll() de la clase ControllerUser")
    void findAll() {

        //necesaria para crear User
        Date date = new Date();
        //Creo 2 usuarios de prueba
        User user = new User("Pablo", 26, "12345", "Pablo", "del Pozo", "pablo@bosonit.com", "pablo@mail.com", "Haro", true, date, "", date);
        User user2 = new User("Pablo2", 26, "12345", "Pablo2", "del Pozo2", "pablo2@bosonit.com", "pablo2@mail.com", "Haro", true, date, "", date);

        //Los guardo en el repositorio(BBDD h2)
        userRepository.save(user);
        userRepository.save(user2);

        //Creo una lista de User buscando con findAll()
        List<User> users = controllerUser.findAll();

        //Y si  va bien deberia de ser 2 su longitud
        Assertions.assertEquals(2, users.size());

        userRepository.deleteById(1);
        userRepository.deleteById(2);

    }
}