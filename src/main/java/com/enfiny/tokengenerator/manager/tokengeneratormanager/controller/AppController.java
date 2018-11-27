package com.enfiny.tokengenerator.manager.tokengeneratormanager.controller;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.AppCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.AppEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.AppResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.AppService;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.CommonService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ACTIONS_APP')")
@RequestMapping(value = "apps",produces = {"application/json"})
public class AppController {
    @Autowired
    private AppService appService;
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/createApp", method = RequestMethod.POST)
    public ResponseEntity<Object> createApp(@RequestBody @Valid AppCreationDto dto, @RequestHeader Long clientId) {
        appService.createApp(dto);
        Map<Object,Object> response = new HashMap<>();
        response.put("message","Successfully Created");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/editApp", method = RequestMethod.PUT)
    public ResponseEntity<Object> editApp(@RequestBody AppEditRequest request) { //@RequestBody UserEditRequest request
        appService.editApp(request);
        return new ResponseEntity<>("Successfully Edited", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteApp/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteApp(@PathVariable("id") Long id) {
        appService.deleteApp(id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/getApp/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getApp(@PathVariable("id") Long id) {
        AppResponseDto response = appService.getApp(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllApps", method = RequestMethod.GET, params = {"sort", "size", "page", "search"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Default value is 0 (first page)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Default value is 20", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", dataType = "String array", paramType = "query", value = "Default sorting is desc.Multiple parameter can be added as sort=appName&sort=email,desc", defaultValue = "id,ASC"),
            @ApiImplicitParam(name = "search", dataType = "String", paramType = "query", value = "Default value is null ", defaultValue = "")})
    public ResponseEntity<Object> getAllApps(@RequestParam(value = "sort", required = false) String[] sort,
                                             @RequestParam(value = "size", required = false) int size,
                                             @RequestParam(value = "page", required = false) int page,
                                             @RequestParam(value = "search", required = false) String search) {
        Client client = commonService.returnIfClientExists();
        List<AppResponseDto> response = appService.getAllApps(client,page, size, sort, search);
        Map<Object, Object> responseMap = new HashMap<>();
        responseMap.put("page", page);
        responseMap.put("total", Math.ceil((double)appService.countApps(client,search)/size));
        responseMap.put("data", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
