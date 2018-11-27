package com.enfiny.tokengenerator.manager.tokengeneratormanager.controller;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.GrantAccessCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.GrantAccessEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.GrantAccessResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.CommonService;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.service.GrantAccessService;
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
@PreAuthorize("hasAuthority('ACTIONS_GRANT')")
@RequestMapping("grants")
public class GrantAccessController {
    @Autowired
    private GrantAccessService grantAccessService;
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/createGrantAccess", method = RequestMethod.POST)
    public ResponseEntity<Object> createGrantAccess(@RequestBody GrantAccessCreationDto dto, @RequestHeader Long clientId) {
        grantAccessService.createGrantAccess(dto);
        return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/editGrantAccess", method = RequestMethod.PUT)
    public ResponseEntity<Object> editGrantAccess(@RequestBody GrantAccessEditRequest request) { //@RequestBody UserEditRequest request
        grantAccessService.editGrantAccess(request);
        return new ResponseEntity<>("Successfully Edited", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteGrantAccess/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteGrantAccess(@RequestHeader Long appId, @PathVariable("id") Long id) {
          grantAccessService.deleteGrantAccess(appId,id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/getGrantAccess/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getGrantAccess(@RequestHeader Long appId, @PathVariable("id") Long id) {
        GrantAccessResponseDto response = grantAccessService.getGrantAccess(appId,id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllGrantAccess", method = RequestMethod.GET, params = {"sort", "size", "page", "search"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Default value is 0 (first page)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Default value is 20", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", dataType = "String array", paramType = "query", value = "Default sorting is desc.Multiple parameter can be added as sort=grantType&sort=grantType,desc", defaultValue = "id,ASC"),
            @ApiImplicitParam(name = "search", dataType = "String", paramType = "query", value = "Default value is null ", defaultValue = " ")})
    public ResponseEntity<Object> getAllGrantAccess(@RequestHeader Long appId,
                                                    @RequestParam(value = "sort", required = false) String[] sort,
                                                    @RequestParam(value = "size", required = false) int size,
                                                    @RequestParam(value = "page", required = false) int page,
                                                    @RequestParam(value = "search", required = false) String search) {
        App app = commonService.getApp(appId);
        List<GrantAccessResponseDto> response = grantAccessService.getAllGrantAccess(app,page, size, sort, search);
        Map<Object, Object> responseMap = new HashMap<>();
        responseMap.put("page", page);
        responseMap.put("total", Math.ceil((double)grantAccessService.countGrantAccess(app,search)/size));
        responseMap.put("data", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
