import model.User;
import repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        User user = new User(username, password);
        userRepository.save(user);
        return true;
    }
}
