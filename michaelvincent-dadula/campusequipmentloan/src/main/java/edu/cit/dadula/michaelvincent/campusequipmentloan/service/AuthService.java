package edu.cit.dadula.michaelvincent.campusequipmentloan.service;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Student;
import edu.cit.dadula.michaelvincent.campusequipmentloan.model.User;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.StudentRepository;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student register(Student student) {
        User user = new User();
        user.setUsername(student.getUser().getUsername());
        user.setEmail(student.getEmail());
        user.setPassword(passwordEncoder.encode(student.getUser().getPassword())); // encode password
        user.setRole("STUDENT");

        user = userRepository.save(user);

        student.setUser(user);

        return studentRepository.save(student);
    }

    public boolean login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }
}
