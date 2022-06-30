package com.bridgelabz.userregistration.service;

import com.bridgelabz.userregistration.dto.ResponseDTO;
import com.bridgelabz.userregistration.dto.UserDTO;
import com.bridgelabz.userregistration.dto.UserLoginDTO;
import com.bridgelabz.userregistration.exception.UserException;
import com.bridgelabz.userregistration.model.User;
import com.bridgelabz.userregistration.repository.UserRepository;
import com.bridgelabz.userregistration.util.EmailSenderService;
import com.bridgelabz.userregistration.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailSenderService mailService;

    @Autowired
    TokenUtility util;

    @Override
    public String addUser(UserDTO userDTO) {
        User newUser = new User(userDTO);
        userRepository.save(newUser);
        String token = util.createToken(newUser.getUserId());
        mailService.sendEmail(newUser.getEmail(), "User Registration", " Hi " + newUser.getFirstName() +
                " Your User Registered SuccessFully Completed. Please Click here to get data-> "
                + "http://localhost:8090/user/verify/" + token);
        return token;
    }

    @Override
    public String verifyUser(String token) {
        int id = util.decodeToken(token);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.toString();
        } else
            return "User not Verified!!";
    }

    @Override
    public ResponseDTO loginUser(UserLoginDTO userLoginDTO) {
        ResponseDTO dto = new ResponseDTO();
        Optional<User> login = userRepository.findByEmailid(userLoginDTO.getEmail());
        if (login.isPresent()) {
            String pass = login.get().getPassword();
            if (login.get().getPassword().equals(userLoginDTO.getPassword())) {
                mailService.sendEmail(login.get().getEmail(), "User Registration",
                        " Hi Welcome back " + login.get().getFirstName() +
                                ". You have Successfully Loggedin. ");
                dto.setMessage("login successful ");
                dto.setData(login.get());
                return dto;
            } else {
                dto.setMessage("Sorry! login is unsuccessful");
                dto.setData("Wrong password");
                return dto;
            }
        }
        return new ResponseDTO("User not found!", "Wrong email");
    }


    @Override
    public List<User> getAllUsers() {
        List<User> getUsers = userRepository.findAll();
        if (getUsers.isEmpty()) {
            throw new UserException("There is no User added yet");
        } else
            return getUsers;
    }

    @Override
    public Object getUserByToken(String token) {
        int id = util.decodeToken(token);
        Optional<User> getUser = userRepository.findById(id);
        if (getUser.isPresent()) {
            mailService.sendEmail(getUser.get().getEmail(), "User Registration", "Hi "
                    + getUser.get().getFirstName() + " Please Click here to get data-> "
                    + "http://localhost:8090/user/getAll/");
            return getUser;

        } else {
            throw new UserException("Record for provided userId is not found");
        }
    }

    @Override
    public User updateRecordById(Integer id, UserDTO userDTO) {
        Optional<User> updateUser = userRepository.findById(id);
        if (updateUser.isPresent()) {
            User newUser = new User(updateUser.get().getUserId(), userDTO);
            userRepository.save(newUser);
            String token = util.createToken(newUser.getUserId());
            mailService.sendEmail(newUser.getEmail(), "Welcome " + newUser.getFirstName(),
                    "Your User Registration details updated successfully!!");
            return newUser;
        }
        throw new UserException("Book Details for email not found");
    }


    @Override
    public String forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmailid(email);

        if (!userOptional.isPresent()) {
            return "Invalid email id.";
        } else {
            User user = userOptional.get();
            user.setToken(generateToken());
            user.setTokenCreationDate(LocalDateTime.now());

            user = userRepository.save(user);

            mailService.sendEmail(userOptional.get().getEmail(), "Reset Password", "Hi " + userOptional.get().getFirstName() + "\n"
                    + "You're receiving this email because you requested a password reset \n"
                    + "Click the following link to change the password : " + "http://localhost:8090/user/reset-password?token=" + user.getToken());
        }

        return "Check Your Email To Change The Password";
    }

    @Override
    public String resetPassword(String token, String password) {
        Optional<User> userOptional = userRepository.findByToken(token);

        if (!userOptional.isPresent()) {
            return "Invalid token.";
        }

        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token Expired.";
        }
        User user = userOptional.get();

        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);
        return "Your password successfully updated.";
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    @Override
    public User getByIdAPI(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserException("There are no users with given id");
        }
        return user.get();
    }

    @Override
    public Object deleteById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserException("Invalid UserId..please input valid Id");
        }
        userRepository.deleteById(id);
        mailService.sendEmail(user.get().getEmail(), "Hi" + user.get().getFirstName(),
                "Your User Registration Account deleted successfully!!");
        return user.get();
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }
}
