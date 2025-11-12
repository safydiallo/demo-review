// Importation des classes nécessaires pour le hachage des mots de passe
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

// Importation des classes User et UserRepository utilisées dans ce service
import model.User;
import repository.UserRepository;

/**
 * Classe UserService : 
 * Elle contient la logique métier pour gérer les utilisateurs (inscription, etc.)
 */
public class UserService {

    // Dépendance vers la classe UserRepository qui gère la persistance des utilisateurs
    private final UserRepository userRepository;

    /**
     * Constructeur : permet d'injecter le UserRepository (principe d’injection de dépendance)
     * @param userRepository l'objet responsable de la gestion des utilisateurs en base
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Méthode pour enregistrer un nouvel utilisateur
     * @param username le nom d'utilisateur à enregistrer
     * @param password le mot de passe de l'utilisateur
     * @return true si l'inscription a réussi, false sinon
     */

public boolean registerUser(String username, String password) {
    System.out.println("Branche B : validation stricte");
    if (username == null || username.isBlank() || password == null) return false;
    String hashedPassword = hashPassword(password);
    userRepository.save(new User(username, hashedPassword));
    return true;
}
}
