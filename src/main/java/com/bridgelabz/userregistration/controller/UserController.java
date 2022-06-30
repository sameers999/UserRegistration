package com.bridgelabz.userregistration.controller;

import com.bridgelabz.userregistration.dto.ResponseDTO;
import com.bridgelabz.userregistration.dto.UserDTO;
import com.bridgelabz.userregistration.dto.UserLoginDTO;
import com.bridgelabz.userregistration.service.IUserService;
import com.bridgelabz.userregistration.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    //  Ability to Create account
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        String newUser = userService.addUser(userDTO);
        ResponseDTO responseDTO = new ResponseDTO("User Registered Successfully", newUser);
        return new ResponseEntity(responseDTO, HttpStatus.CREATED);
    }
    //    Ability to login
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        return new ResponseEntity<ResponseDTO>(userService.loginUser(userLoginDTO), HttpStatus.OK);
    }

    //    Ability to getAll user
    @GetMapping(value = "/getAll")
    public ResponseEntity<String> getAllUser() {
        List<User> listOfUsers = userService.getAllUsers();
        ResponseDTO dto = new ResponseDTO("User retrieved successfully (:", listOfUsers);
        return new ResponseEntity(dto, HttpStatus.OK);
    }
    //    Ability to get user by token
    @GetMapping(value = "/getUser/{token}")
    public ResponseEntity<ResponseDTO> getAllUserDataByToken(@PathVariable String token) {
        Object user = this.userService.getUserByToken(token);
        ResponseDTO response = new ResponseDTO("Requested User : ", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    //    Ability to Update by id
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRecordById(@PathVariable Integer id, @Valid @RequestBody UserDTO userDTO) {
        User entity = userService.updateRecordById(id, userDTO);
        ResponseDTO dto = new ResponseDTO("User Record updated successfully", entity);
        return new ResponseEntity(dto, HttpStatus.ACCEPTED);
    }

    //to delete specific user using id provided
    @DeleteMapping({"/delete/{id}"})
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable Integer id) {
        ResponseDTO response = new ResponseDTO("User deleted successfully", userService.deleteById(id));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //Ability to verify user by token
    @GetMapping("/verify/{token}")
    ResponseEntity<ResponseDTO> verifyUser(@Valid @PathVariable String token) {
        String userVerification = userService.verifyUser(token);
        if (userVerification != null) {
            ResponseDTO responseDTO = new ResponseDTO("User verified :", userVerification);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            ResponseDTO responseDTO = new ResponseDTO("User Not verified data:", userVerification);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }
    //Ability
    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam String email){
        String user = userService.forgotPassword(email);
        ResponseDTO response = new ResponseDTO("Forgot password", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestParam String token, @RequestParam String password){
        String user = userService.resetPassword(token, password);
        ResponseDTO response = new ResponseDTO("Reset Password", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //--------------------API Calls for RestTemplate----------------------------//

    @GetMapping("/findById/{userId}")
    ResponseEntity<ResponseDTO> getByIdAPI(@PathVariable Integer userId){
        User user =  userService.getByIdAPI(userId);
        ResponseDTO response = new ResponseDTO("User Id found", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
