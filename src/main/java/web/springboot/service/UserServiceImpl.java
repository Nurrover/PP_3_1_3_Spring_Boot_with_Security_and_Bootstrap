package web.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.springboot.model.User;
import web.springboot.repository.UserRepository;
import web.springboot.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void updateUser(User newUser) {
        User oldUser = userRepository.findById(newUser.getId()).get();
        if (!oldUser.getPassword().equals(newUser.getPassword())) {
            newUser.setPassword(encoder.encode(newUser.getPassword()));
        }

        if (newUser.getRoles() == null || newUser.getRoles().isEmpty()) {
            newUser.setRoles(oldUser.getRoles());
        }

        userRepository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User newUser) {
        Optional<User> user = userRepository.findByEmail(newUser.getEmail());
        if (user.isEmpty()) {
            newUser.setPassword(encoder.encode(newUser.getPassword()));
        }

        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void removeUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(user.get());
    }
}
