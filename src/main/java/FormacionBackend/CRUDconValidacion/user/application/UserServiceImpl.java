package FormacionBackend.CRUDconValidacion.user.application;


import FormacionBackend.CRUDconValidacion.exceptions.EntityNotFoundException;
import FormacionBackend.CRUDconValidacion.exceptions.UnprocessableEntityException;
import FormacionBackend.CRUDconValidacion.user.domain.User;
import FormacionBackend.CRUDconValidacion.user.infraestructure.dtos.UserInputDTO;
import FormacionBackend.CRUDconValidacion.user.infraestructure.dtos.UserOutputDTO;
import FormacionBackend.CRUDconValidacion.user.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;


@Component
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    /*
     *
     * Recibimos el inputDTO
     * creamos un User
     * Llamos al metodo de User pasandole el inputDTO
     * lo guardamos en el repositorio
     * retornamos el Output de User
     * */
    @Override
    public UserOutputDTO createUser(UserInputDTO userInputDTO) throws UnprocessableEntityException {


        if ((userInputDTO.getUser() == null) || (userInputDTO.getUser().equals(""))) {
            throw new UnprocessableEntityException("User field can not be null", 422);
        }
        if (userInputDTO.getUser().length() > 10) {
            throw new UnprocessableEntityException("User field length is not > 10", 422);
        }

        User user = new User();

        user.createUser(userInputDTO);
        userRepository.save(user);
        return new UserOutputDTO(user);

    }


    /*
     *
     * Creamos user y le asignamos el user del repositorio con el id pasado por parametro(ya tenemos aqui el user a actualizar)
     * Lo guardamos con los datos recibidos
     * y lo retornamos
     *
     * */
    @Override
    public UserOutputDTO updateUser(UserInputDTO userInputDTO, Integer id) throws EntityNotFoundException {

        Optional<User> userOpt = userRepository.findById(id);

        if(userOpt.isEmpty())
            throw new EntityNotFoundException("The userdoes no exist", 404);

        if ((userInputDTO.getUser() == null) || (userInputDTO.getUser().equals("")))
            throw new UnprocessableEntityException("User field can not be null", 422);

        User user = userOpt.get();

        user.setIdUser(id);
        user.createUser(userInputDTO);
        userRepository.save(user);

        return new UserOutputDTO(user);
    }


    /*
     * Recibimos el id
     * Buscamos en el repositorio de User un usuario con el mismo id que el psado como parametro
     * lo retornamos
     * */
    @Override
    public UserOutputDTO getUser(Integer id) throws EntityNotFoundException {

        //Obtengo el usuario por si id
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found",404));

        return new UserOutputDTO(user);
    }


    @Override
    public void deleteUser(Integer id) {


        //Obtengo el usuario por si id
        Optional<User> userOpt = userRepository.findById(id);

        if(userOpt.isEmpty())
            throw new EntityNotFoundException("The user does no exist", 404);

        userRepository.deleteById(id);
    }



    @Override
    public List<User> findByName(String name) {

        return userRepository.findByName(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


}
