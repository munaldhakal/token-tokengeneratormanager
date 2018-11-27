package com.enfiny.tokengenerator.manager.tokengeneratormanager.service;


import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.AlreadyExistsException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.NotFoundException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.User;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.UserRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.PasswordEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.UserCreationRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.UserEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.UserResponseDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.utilities.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommonService commonService;

    @Transactional
    public void createUser(UserCreationRequest dto) {
        Client client = commonService.returnIfClientExists();
        App app = commonService.getAppByClient(dto.getAppId(),client);
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
        user.setFullName(dto.getFullName());
        user.set_id(dto.get_id());
        if(dto.getEmail()!=null)
            user.setEmail(dto.getEmail());
        user.setPassword("{bcrypt}"+ BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt()));
        user.setStatus(Status.ACTIVE);
        GrantAccess grantAccess = commonService.getGrantAccessByAppAndGrantName(app,dto.getGrantAccess());
        user.setGrantAccess(grantAccess);
        userRepository.save(user);
    }

    @Transactional
    public void editUser(UserEditRequest request) {
        App app = commonService.getApp(request.getAppId());
        User user = userRepository.findByIdAndStatus(request.getId(),Status.ACTIVE);
        if(user==null||!user.getApp().getId().equals(app.getId()))
            throw new NotFoundException("No User Found.");
        if(request.getUsername()!=null){
            if(userRepository.existsByAppAndUsernameAndStatus(app,request.getUsername(),Status.ACTIVE))
                throw new AlreadyExistsException("User with username: "+request.getUsername()+" already exists");
            else
                user.setUsername(request.getUsername());
        }
        if(request.getEmail()!=null)
            user.setEmail(request.getEmail());
        if(request.getFullName()!=null)
            user.setFullName(request.getFullName());
        if(request.getGrantType()!=null) {
            user.setGrantAccess(commonService.getGrantAccessByAppAndGrantName(app, request.getGrantType()));
        }
//        if(request.isAccountNonExpired())
//            user.setAccountNonExpired(true);
//        else
//            user.setAccountNonExpired(false);
//        if(request.isAccountNonLocked())
//            user.setAccountNonLocked(true);
//        else
//            user.setAccountNonLocked(false);
//        if(request.isCredentialNonExpired())
//            user.setCredentialsNonExpired(true);
//        else
//            user.setCredentialsNonExpired(false);
//        if(request.isEnabled())
//            user.setEnabled(true);
//        else
//            user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long appId,Long id) {
        User user = getUserStats(appId,id);
        user.setStatus(Status.DELETED);
        userRepository.save(user);
    }

    private User getUserStats(Long appId, Long id) {
        App app = commonService.getApp(appId);
        User user = userRepository.findByIdAndStatus(id,Status.ACTIVE);
        if(user==null)
            throw new NotFoundException("User not found.");
        if(!user.getApp().getId().equals(app.getId()))
            throw new NotFoundException("You are not authorized");
        return user;
    }

    @Transactional
    public UserResponseDto getUser(Long appId,Long id) {
        User user = getUserStats(appId,id);
        return getUserDetails(appId,user);
    }

    private UserResponseDto getUserDetails(Long appId,User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        responseDto.setFullName(user.getFullName());
        if(user.getEmail()!=null)
            responseDto.setEmail(user.getEmail());
        if(user.get_id()!=null)
            responseDto.set_id(user.get_id());
        responseDto.setAccountNonExpired(user.isAccountNonExpired());
        responseDto.setAccountNonLocked(user.isAccountNonLocked());
        responseDto.setAppId(appId);
        responseDto.setClientId(user.getClient().getId());
        responseDto.setEnabled(user.isEnabled());
        responseDto.setGrantAccess(user.getGrantAccess().getGrantType().toString());
        List<String> authorityList = new ArrayList<>();
        user.getGrantAccess().getAuthority().forEach(authority->authorityList.add(authority.getAuthority()));
        responseDto.setAuthorities(authorityList);
        responseDto.setStatus(user.getStatus().toString());
        return responseDto;
    }

    @Transactional
    public List<UserResponseDto> getAllUsers(App app, int page, int size, String[] sort, String search) {
        Pageable pageValue = commonService.getPageable(sort,size,page);
        List<User> userList = null;
        if(search != null&&!search.isEmpty()&&!search.equals(" "))
            userList = userRepository.findByAppAndStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(app,Status.ACTIVE,search,search,pageValue);
        else
            userList = userRepository.findByAppAndStatus(app,Status.ACTIVE,pageValue);
        if(userList.isEmpty())
            throw new NotFoundException("No users found");
        List<UserResponseDto> response = new ArrayList<>();
        userList.forEach(user -> response.add(getUserDetails(app.getId(),user)));
        return response;
    }

    @Transactional
    public Long countUsers(App app, String search) {
        if(search != null&&!search.isEmpty()&&!search.equals(" "))
            return userRepository.countByAppAndStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(app, Status.ACTIVE, search, search);
        return userRepository.countByAppAndStatus(app,Status.ACTIVE);
    }

    @Transactional
    public void changePassword(PasswordEditRequest request) {
        Client client = commonService.getIfClientExists(request.getClientId());
        App app = commonService.getAppByClient(request.getAppId(),client);
        User user = userRepository.findByAppAndUsernameAndStatus(app,request.getUsername(),Status.ACTIVE);
        if(user == null)
            throw new NotFoundException("User Not Found to edit");
        if(request.getPassword()!=null)
            user.setPassword("{bcrypt}"+BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
        userRepository.save(user);
    }
}
