package com.enfiny.tokengenerator.manager.tokengeneratormanager.service;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.GrantType;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.SortByUser;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.*;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.filter.TokenGeneratorManagerFilter;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.AppRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.ClientRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.GrantAccessRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.utilities.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private TokenGeneratorManagerFilter tokenGeneratorFilter;

    @Autowired
    private GrantAccessRepository grantAccessRepository;

    private static boolean contains(String sortBy) {
        for (SortByUser user : SortByUser.values()) {
            if (user.name().equals(sortBy)) {
                return true;
            }
        }
        return false;
    }

    public Pageable getPageable(String[] sort, int size, int page) {
        if (sort.length == 0) {
            sort = new String[2];
            sort[0] = "id";
            sort[1] = "desc";
        }
        if (sort.length == 1) {
            String[] newSort = new String[2];
            newSort[1] = "asc";
            newSort[0] = sort[0];
            sort = newSort;
        }
        if (!contains(sort[0])) {
            throw new InvalidValueException("Invalid sort value '" + sort[0] + "'");
        }
            Sort sortObj = Sort.by(sort[1].equalsIgnoreCase(OrderBy.asc.name()) ? Sort.Direction.ASC : Sort.Direction.DESC, sort[0]);
        return PageRequest.of(page, size, sortObj);
    }

    public void checkByUsername(String username) {
        Client client = getClientByUsername(username);
        if (client != null)
            throwErrorForClientAlreadyExists(username);
    }

    public Client returnIfClientExists() {
        Client client = clientRepository.getOne(getClientId());
        if (client == null)
            throw new NotFoundException("No Client found.");
        return client;
    }
    public Client getIfClientExists(Long id) {
        Client client = clientRepository.getOne(id);
        if (client == null)
            throw new NotFoundException("No Client found.");
        return client;
    }

    public Client checkIfAppDoesNotExists(String app){
        Client client = returnIfClientExists();
        App appToCheck = getTheApp(client, app);
        if(appToCheck!=null)
            throw new AlreadyExistsException("App of name: "+app+" already exists");
        return client;
    }

    public App getTheApp(Client client, String app){
        return appRepository.findByClientAndAppNameAndStatus(client, app, Status.ACTIVE);
    }

    public void checkByUsernameForEdit(Long id, String username) {
        Client checkClient = getClientByUsername(username);
        if(!checkClient.getId().equals(id))
            throwErrorForClientAlreadyExists(username);
    }

    private void throwErrorForClientAlreadyExists(String username) {
        throw new AlreadyExistsException("Client with username: " + username + " already exists.");
    }

    private Client getClientByUsername(String username){
        return clientRepository.findByUsername(username);
    }

    public Long getClientId(){
        Long clientId = tokenGeneratorFilter.getClientId();
        if(clientId==null)
            throw new NotFoundException("Please provide the clientId.");
        return clientId;
    }

    public App getApp(Long appId) {
        App app = getAppById(appId);
        if(!app.getClient().getId().equals(getClientId()))
            throw new UnAuthorizedException("You are not authorized to retrieve, edit or delete other's app");
        return app;
    }

    public GrantAccess getGrantAccess(Long appId,Long grantAccessId){
        GrantAccess grantAccess = grantAccessRepository.getOne(grantAccessId);
        if(grantAccess==null)
            throw new NotFoundException("No GrantAccess Found");
        if(!grantAccess.getApp().getId().equals(appId))
            throw new UnAuthorizedException("You are not authorized to view edit or delete other's app.");
        return grantAccess;
    }

    public App getAppByClient(Long appId, Client client) {
        App app = getAppById(appId);
        if(!app.getClient().getId().equals(client.getId()))
            throw new UnAuthorizedException("You are not authorized to create user.");
        return  app;
    }

    private App getAppById(Long appId) {
        App app = appRepository.getOne(appId);
        if(app == null)
            throw new NotFoundException("No app found.");
        return  app;
    }

    public GrantAccess getGrantAccessByAppAndGrantName(App app, String grantAccess) {
        if(grantAccess.equals(""))
            throw new RequiredException("GrantAccess is required");
        GrantType grantString;
        try{
            grantString = GrantType.valueOf(grantAccess.toUpperCase());
        }catch(Exception e){
            throw new NotFoundException("Please provide proper grantAccess value");
        }
        GrantAccess grantAccessData = grantAccessRepository.findByAppAndGrantType(app,grantString);
        if(grantAccessData==null)
            throw new NotFoundException("Please provide proper grantAccess value");
        return grantAccessData;
    }
}
