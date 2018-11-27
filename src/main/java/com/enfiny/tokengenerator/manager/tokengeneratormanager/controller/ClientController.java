package com.enfiny.tokengenerator.manager.tokengeneratormanager.controller;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.ClientCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.ClientEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.ClientResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.ClientService;
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
@RequestMapping("clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

//    @RequestMapping(value = "/createClient", method = RequestMethod.POST)
//    public ResponseEntity<Object> createClient(@RequestBody ClientCreationDto dto) {
//        clientService.createClient(dto);
//        return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
//    }

    @PreAuthorize("hasAuthority('ACTIONS_CLIENT')")
    @RequestMapping(value = "/editClient", method = RequestMethod.PUT)
    public ResponseEntity<Object> editClient(@RequestBody ClientEditRequest request) {
        clientService.editClient(request);
        return new ResponseEntity<>("Successfully Edited", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ACTIONS_CLIENT')")
    @RequestMapping(value = "/deleteClient/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/getClient/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ACTIONS_CLIENT')")
    public ResponseEntity<Object> getClient(@PathVariable("id") Long id) {
        ClientResponseDto response = clientService.getClient(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ACTIONS_CLIENT')")
    @RequestMapping(value = "/getAllClients", method = RequestMethod.GET, params = {"sort", "size", "page", "search"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Default value is 0 (first page)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Default value is 20", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", dataType = "String array", paramType = "query", value = "Default sorting is desc.Multiple parameter can be added as sort=username&sort=email,desc", defaultValue = "id,ASC"),
            @ApiImplicitParam(name = "search", dataType = "String", paramType = "query", value = "Default value is null ", defaultValue = "", required = false)})
    public ResponseEntity<Object> getAllClients(@RequestParam(value = "sort", required = false) String[] sort,
                                                @RequestParam(value = "size", required = false) int size,
                                                @RequestParam(value = "page", required = false) int page,
                                                @RequestParam(value = "search", defaultValue = "",required = false) String search) {
        List<ClientResponseDto> response = clientService.getAllClients(page, size, sort, search);
        Map<Object, Object> responseMap = new HashMap<Object, Object>();
        responseMap.put("page", page);
        responseMap.put("total", Math.ceil((double)clientService.countClients(search)/size));
        responseMap.put("data", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
