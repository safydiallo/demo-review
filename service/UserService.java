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

        if (username == null || username.trim().isEmpty() || password == null || password.length() < 8) {
        System.out.println("Nom d'utilisateur ou mot de passe invalide !");
        return false;
    }

        // Vérifie si l'utilisateur existe déjà dans la base
        if (userRepository.existsByUsername(username)) {
            return false; // utilisateur déjà inscrit
        }

        // Hache le mot de passe avant de le stocker (sécurité)
        String hashedPassword = hashPassword(password);

        // Crée un nouvel utilisateur avec le nom et le mot de passe haché
        User user = new User(username, hashedPassword);

        // Enregistre l'utilisateur dans la base via le dépôt (repository)
        userRepository.save(user);

        // Retourne true pour indiquer que l'inscription a réussi
        return true;
    }

    /**
     * Méthode privée pour hacher un mot de passe avec l'algorithme SHA-256
     * @param password le mot de passe à hacher
     * @return le mot de passe haché sous forme de chaîne hexadécimale
     */
    private String hashPassword(String password) {
        try {
            // Obtient une instance de l'algorithme de hachage SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Calcule le hachage du mot de passe (converti en tableau d’octets)
            byte[] hash = md.digest(password.getBytes());

            // Convertit le tableau d’octets en une chaîne hexadécimale lisible
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b)); // %02x → format hexadécimal à 2 chiffres
            }

            // Retourne le mot de passe haché final
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // En cas d'erreur (algorithme non trouvé), on relance une exception
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    // Nouvelle méthode ajoutée par Branch A
    public boolean updatePassword(String username, String newPassword) {
        if (username == null || username.isBlank() || newPassword == null || newPassword.isBlank()) {
            return false;
        }
        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) return false;
        User user = opt.get();
        user.setPassword(hashPassword(newPassword));
        userRepository.save(user);
        return true;
    }

//Nouvelle branche cree par Djiby FALL
    public boolean deleteUser(String username) {
    if (username == null || username.isBlank()) {
        return false;
    }

    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isEmpty()) {
        return false;
    }

    userRepository.delete(userOpt.get());
    return true;
}

}
