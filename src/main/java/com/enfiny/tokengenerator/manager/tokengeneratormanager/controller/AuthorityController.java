package com.enfiny.tokengenerator.manager.tokengeneratormanager.controller;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.AuthorityCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.AuthorityEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.AuthorityResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.AuthorityService;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.CommonService;
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
@PreAuthorize("hasAuthority('ACTIONS_AUTHORITY')")
@RequestMapping("authorities")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/createAuthority", method = RequestMethod.POST)
    public ResponseEntity<Object> createAuthority(@RequestBody AuthorityCreationDto dto, @RequestHeader Long clientId) {
        authorityService.createAuthority(dto);
        return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/editAuthority", method = RequestMethod.PUT)
    public ResponseEntity<Object> editAuthority(@RequestHeader AuthorityEditRequest request) {
        authorityService.editAuthority(request);
        return new ResponseEntity<>("Successfully Edited", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteAuthority/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteAuthority(@RequestHeader Long grantAccessId, @RequestHeader Long appId, @PathVariable("id") Long id) {
        authorityService.deleteAuthority(appId,grantAccessId,id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/getAuthority/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getAuthority(@RequestHeader Long grantAccessId, @RequestHeader Long appId, @PathVariable("id") Long id) {
        AuthorityResponseDto response = authorityService.getAuthority(grantAccessId,appId,id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllAuthority", method = RequestMethod.GET, params = {"sort", "size", "page", "search"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Default value is 0 (first page)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Default value is 20", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", dataType = "String array", paramType = "query", value = "Default sorting is desc.Multiple parameter can be added as sort=username&sort=email,desc", defaultValue = "id,ASC"),
            @ApiImplicitParam(name = "search", dataType = "String", paramType = "query", value = "Default value is null ", defaultValue = " ")})
    public ResponseEntity<Object> getAllAuthority(@RequestHeader Long appId, @RequestHeader Long grantAccessId,
                                                  @RequestParam(value = "sort", required = false) String[] sort,
                                                  @RequestParam(value = "size", required = false) int size,
                                                  @RequestParam(value = "page", required = false) int page,
                                                  @RequestParam(value = "search", required = false) String search) {
        GrantAccess grantAccess = commonService.getGrantAccess(appId,grantAccessId);
        List<AuthorityResponseDto> response = authorityService.getAllAuthority(grantAccess,page, size, sort, search);
        Map<Object, Object> responseMap = new HashMap<>();
        responseMap.put("page", page);
        responseMap.put("total", Math.ceil((double)authorityService.countAuthority(grantAccess,search)/size));
        responseMap.put("data", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}

