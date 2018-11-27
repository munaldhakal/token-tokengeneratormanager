package com.enfiny.tokengenerator.manager.tokengeneratormanager.controller;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.PasswordEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.UserCreationRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.UserEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.UserResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.CommonService;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ACTIONS_USER')")
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody UserCreationRequest dto, @RequestHeader Long clientId) {
        userService.createUser(dto);
        return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.PUT)
    public ResponseEntity<Object> editUser(@RequestBody UserEditRequest request) { //@RequestBody UserEditRequest request
        userService.editUser(request);
        return new ResponseEntity<>("Successfully Edited", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public ResponseEntity<Object> changePassword(@RequestBody PasswordEditRequest request) { //@RequestBody UserEditRequest request
        userService.changePassword(request);
        return new ResponseEntity<>("Password Successfully Changed", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@RequestHeader Long appId, @PathVariable("id") Long id) {
        userService.deleteUser(appId,id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@RequestHeader Long appId, @PathVariable("id") Long id) {
        UserResponseDto response = userService.getUser(appId,id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET, params = {"sort", "size", "page", "search"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Default value is 0 (first page)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Default value is 20", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", dataType = "String array", paramType = "query", value = "Default sorting is desc.Multiple parameter can be added as sort=appName&sort=email,desc", defaultValue = "id,ASC"),
            @ApiImplicitParam(name = "search", dataType = "String", paramType = "query", value = "Default value is null ", defaultValue = "")})
    public ResponseEntity<Object> getAllUsers(@RequestHeader Long appId,
                                              @RequestParam(value = "sort", required = false) String[] sort,
                                              @RequestParam(value = "size", required = false) int size,
                                              @RequestParam(value = "page", required = false) int page,
                                              @RequestParam(value = "search", required = false) String search) {
        App app =commonService.getApp(appId);
        List<UserResponseDto> response = userService.getAllUsers(app,page, size, sort, search);
        Map<Object, Object> responseMap = new HashMap<>();
        responseMap.put("page", page);
        responseMap.put("total", Math.ceil((double)userService.countUsers(app,search)/size));
        responseMap.put("data", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
