package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AuthDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.UserDTO;
import com.example.Spring_Salon_Project.dto.UserDataDTO;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import static com.example.Spring_Salon_Project.constant.ResponseCode.OPERATION_SUCCESS;
import static com.example.Spring_Salon_Project.constant.ResponseMassage.SUCCESS_MESSAGE;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/user_saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveUser(@RequestBody UserDTO userDTO){
        UserDTO saveDto = userService.saveUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS,saveDto, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllUsers(){
        List<UserDTO> getAllUser = userService.getAllUsers();
        return new CommonResponse(OPERATION_SUCCESS,getAllUser,SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/filter-users",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterUsers(@RequestParam(value = "userName",required = false)String userName){
        List<UserDTO> userDTOS = userService.filterUsers(userName);
        return new CommonResponse(OPERATION_SUCCESS,userDTOS,SUCCESS_MESSAGE);

    }

    @DeleteMapping(value = "/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteUser(@PathVariable long userId){
        userService.deleteUser(userId);
        return new CommonResponse(OPERATION_SUCCESS,SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/select-user/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectUser(@PathVariable long userId){
        UserDTO userDto = userService.selectUser(userId);
        return new CommonResponse(OPERATION_SUCCESS,userDto,SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/update-user",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateUser(@RequestBody UserDTO userDTO){
        userService.updateUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS,SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse loginUser(@RequestBody AuthDTO authDTO) {
        UserDTO userDetails = userService.getUserDetails(authDTO.getUserName(), authDTO.getPassword());

        System.out.println("API called here");
        String token = jwtUtil.generateToken(userDetails);

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setToken(token);
        userDataDTO.setUserId(userDetails.getUserId());

        return new CommonResponse(OPERATION_SUCCESS, userDataDTO, SUCCESS_MESSAGE);
    }

}
