package com.enfiny.tokengenerator.manager.tokengeneratormanager.service;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.ClientCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.*;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.User;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.ClientRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.GrantAccessRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.UserRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.ClientEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.ClientResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.utilities.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GrantAccessRepository grantAccessRepository;

    @Transactional
    public void createClient(ClientCreationDto dto) {
        commonService.checkByUsername(dto.getUsername());
        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setCreatedDate(new Date());
        client.setStatus(Status.ACTIVE);
        if (dto.getEmail() != null && !dto.getEmail().equals(""))
            client.setEmail(dto.getEmail());
        checkSecretLength(dto.getSecret());
        client.setSecret("{bcrypt}"+ BCrypt.hashpw(dto.getSecret(), BCrypt.gensalt()));
        client = clientRepository.save(client);
        try{
            Client client1 = clientRepository.getOne(1L);
            App app = commonService.getAppByClient(1L,client1);
            User user = userRepository.findByAppAndUsernameAndStatus(app,dto.getUsername(), Status.ACTIVE);
            if(user!=null)
                throw new AlreadyExistsException("User with username: "+dto.getUsername()+" already exists.");
            user = new User();
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setApp(app);
            user.setClient(client);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setUsername(dto.getUsername());
            if(dto.getEmail()!=null)
                user.setEmail(dto.getEmail());
            user.setPassword("{bcrypt}"+ BCrypt.hashpw(dto.getSecret(),BCrypt.gensalt()));
            user.setStatus(Status.ACTIVE);
            GrantAccess grantAccess = grantAccessRepository.getOne(1L);
            user.setGrantAccess(grantAccess);
            userRepository.save(user);
        }catch(Exception e){
            e.printStackTrace();
            clientRepository.delete(client);
        }
    }

    @Transactional
    public void editClient(ClientEditRequest request) {
        Client client = commonService.returnIfClientExists();
        User user = userRepository.findByUsernameAndStatus(client.getUsername(),Status.ACTIVE);
        if(user==null)
            throw new CustomException("Invalid Client", HttpStatus.BAD_REQUEST);
        if (request.getEmail() != null && !request.getEmail().equals("")) {
            client.setEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }
        if (request.getUsername() != null && !request.getUsername().equals("")) {
            commonService.checkByUsernameForEdit(client.getId(),request.getUsername());
            client.setUsername(request.getUsername());
            user.setUsername(request.getUsername());
        }
        client.setModifiedDate(new Date());
        clientRepository.save(client);
        userRepository.save(user);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = commonService.getIfClientExists(id);
        if(!client.getId().equals(commonService.getClientId()))
            throw new UnAuthorizedException("You are not authorized to delete other.");
        User user = userRepository.findByUsernameAndStatus(client.getUsername(),Status.ACTIVE);
        if(user==null)
            throw new CustomException("Invalid Client", HttpStatus.BAD_REQUEST);
        client.setModifiedDate(new Date());
        client.setStatus(Status.DELETED);
        user.setStatus(Status.DELETED);
        clientRepository.save(client);
        userRepository.save(user);
    }

    @Transactional
    public ClientResponseDto getClient(Long id) {
        Client client = commonService.getIfClientExists(id);
        return getClientDetails(client);
    }

    @Transactional
    public List<ClientResponseDto> getAllClients(int page, int size, String[] sort, String search) {
        if(commonService.getClientId()!=1)
            throw new UnAuthorizedException("You are unauthorized");
        Pageable pageValue = commonService.getPageable(sort, size, page);
        List<Client> clientList;
        if (search != null&&!search.isEmpty()&&!search.equals(" ")) {
            clientList = clientRepository.findByStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(Status.ACTIVE, search, search, pageValue);
        }
            else {
            clientList = clientRepository.findAllByStatus(Status.ACTIVE, pageValue);
        }
        if (clientList.isEmpty())
            throw new NotFoundException("No Clients Found");
        List<ClientResponseDto> response = new ArrayList<>();
        clientList.forEach(client -> response.add(getClientDetails(client)));
        return response;
    }

    @Transactional
    public Long countClients(String search) {
        if (search != null&&!search.isEmpty()&&!search.equals(" "))
            return clientRepository.countByStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(Status.ACTIVE, search, search);
        return clientRepository.countByStatus(Status.ACTIVE);
    }

    private void checkSecretLength(String secret) {
        if (secret.length() < 8)
            throw new InvalidLengthException("Secret length must be greater than or equal to 8");
    }

    private ClientResponseDto getClientDetails(Client client) {
        ClientResponseDto responseDto = new ClientResponseDto();
        responseDto.setUsername(client.getUsername());
        if (client.getEmail() != null)
            responseDto.setEmail(client.getEmail());
        responseDto.setClientId(client.getId());
        responseDto.setCreatedDate(new SimpleDateFormat("dd/MMM/yyyy").format(client.getCreatedDate()));
        if (client.getModifiedDate() != null)
            responseDto.setModifiedDate(new SimpleDateFormat("dd/MMM/yyyy").format(client.getModifiedDate()));
        responseDto.setStatus(client.getStatus().toString());
        return responseDto;
    }

}
