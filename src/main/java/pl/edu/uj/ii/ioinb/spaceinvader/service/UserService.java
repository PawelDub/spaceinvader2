package pl.edu.uj.ii.ioinb.spaceinvader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.uj.ii.ioinb.spaceinvader.model.Role;
import pl.edu.uj.ii.ioinb.spaceinvader.model.User;
import pl.edu.uj.ii.ioinb.spaceinvader.repository.RoleRepository;
import pl.edu.uj.ii.ioinb.spaceinvader.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roleRepository") RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }
//    public static String encrytePassword(String password) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder.encode(password);
//    }
//        public static void main(String[] args) {
//        String password = "$2a$10$oKZPLbArNtjLlzPhI3SSN.M7tYhB6q/L9qa7zAQCr2lXG5WeKgj9y";
//        String encrytedPassword = encrytePassword(password);
//
//        System.out.println("Encryted Password: " + encrytedPassword);
//
//    }

}