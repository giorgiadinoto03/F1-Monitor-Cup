package its.fullstack.biblioteca_proj.service;

import its.fullstack.biblioteca_proj.dto.UserDto;
import its.fullstack.biblioteca_proj.exception.EmailAlreadyUsedException;
import its.fullstack.biblioteca_proj.exception.UserNotFoundException;
import its.fullstack.biblioteca_proj.models.Ruolo;
import its.fullstack.biblioteca_proj.models.User;
import its.fullstack.biblioteca_proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Converti la stringa del ruolo nell'enum Ruolo
        Ruolo ruolo = userDto.getRole() != null ? 
                     Ruolo.valueOf(userDto.getRole().toUpperCase()) : 
                     Ruolo.CLIENT;
        user.setRole(ruolo);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        User user = findById(id);

        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        
        if (userDto.getRole() != null) {
            Ruolo ruolo = Ruolo.valueOf(userDto.getRole().toUpperCase());
            user.setRole(ruolo);
        }

        if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);

        // Verifica se l'utente ha prestiti attivi
        if (userRepository.hasActiveLoans(id)) {
            throw new IllegalStateException("Impossibile eliminare l'utente: ha prestiti attivi non ancora restituiti.");
        }

        userRepository.delete(user);
    }
}