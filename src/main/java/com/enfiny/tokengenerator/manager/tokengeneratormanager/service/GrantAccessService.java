package com.enfiny.tokengenerator.manager.tokengeneratormanager.service;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.GrantAccessCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.AlreadyExistsException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.RequiredException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.UnAuthorizedException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.AuthorityRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.GrantAccessRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.GrantAccessEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.GrantAccessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class GrantAccessService {
    @Autowired
    private GrantAccessRepository grantAccessRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Transactional
    public void createGrantAccess(GrantAccessCreationDto dto) {
        App app = commonService.getApp(dto.getAppId());
        GrantAccess grantAccess = grantAccessRepository.findByAppAndGrantType(app,dto.getGrantType());
        if(grantAccess!=null) {
            throw new AlreadyExistsException("Grant Access Type already exists");
        }
        else
            grantAccess = new GrantAccess();
        grantAccess.setGrantType(dto.getGrantType());
        grantAccess.setApp(app);
        grantAccessRepository.save(grantAccess);
    }

    @Transactional
    public void editGrantAccess(GrantAccessEditRequest request) {
        App app = commonService.getApp(request.getAppId());
        GrantAccess grantAccess = checkByID(app,request.getGrantId());
        if(grantAccessRepository.existsByAppAndGrantType(app,request.getGrantType()))
            throw new AlreadyExistsException("Grant Access Type Already Exists.");
        grantAccess.setGrantType(request.getGrantType());
        grantAccessRepository.save(grantAccess);
    }

    @Transactional
    public void deleteGrantAccess(Long appId, Long id) {
        checkAppId(appId);
        App app =commonService.getApp(appId);
        GrantAccess grantAccess = checkByID(app,id);
        authorityRepository.deleteAll(grantAccess.getAuthority());
        grantAccessRepository.delete(grantAccess);
    }

    private GrantAccess checkByID(App app,Long id){
        GrantAccess grantAccess = grantAccessRepository.getOne(id);
        if(grantAccess==null||!grantAccess.getApp().equals(app))
            throw new UnAuthorizedException("You are not authorized");
        return grantAccess;
    }
    private void checkAppId(Long appId){
        if(appId==null)
            throw new RequiredException("AppID is required");
    }

    @Transactional
    public GrantAccessResponseDto getGrantAccess(Long appId, Long id) {
        checkAppId(appId);
        App app = commonService.getApp(appId);
        GrantAccess grantAccess = checkByID(app,id);
        return getGrantAccessDetails(grantAccess);
    }

    private GrantAccessResponseDto getGrantAccessDetails(GrantAccess grantAccess) {
        GrantAccessResponseDto responseDto = new GrantAccessResponseDto();
        responseDto.setId(grantAccess.getId());
        responseDto.setGrantName(grantAccess.getGrantType().toString());
        return responseDto;
    }

    public List<GrantAccessResponseDto> getAllGrantAccess(App app,int page, int size, String[] sort, String search) {
        Pageable pageValue = commonService.getPageable(sort, size, page);
        List<GrantAccess> grantAccessList = null;
        if(search != null&&!search.isEmpty()&&!search.equals(" ")) {
            //GrantType toSearch = getGrantType(search);
            System.out.println("----->>>>>>>>>>>>"+search);
            grantAccessList = grantAccessRepository.findByAppAndGrantTypeContaining(app,search,pageValue);
        }else
            grantAccessList = grantAccessRepository.findByApp(app,pageValue);
        List<GrantAccessResponseDto> responseDto = new ArrayList<>();
        grantAccessList.forEach(grantAccess -> responseDto.add(getGrantAccessDetails(grantAccess)));
        return responseDto;
    }

    public Long countGrantAccess(App app,String search) {
        if(search != null&&!search.isEmpty()&&!search.equals(" "))
            return grantAccessRepository.countByAppAndGrantTypeContaining(app, search);
        else
            return grantAccessRepository.countByApp(app);
    }
}
