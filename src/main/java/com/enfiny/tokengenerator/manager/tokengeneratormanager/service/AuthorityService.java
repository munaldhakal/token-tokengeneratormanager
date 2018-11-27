package com.enfiny.tokengenerator.manager.tokengeneratormanager.service;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.AuthorityCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.AlreadyExistsException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.NotFoundException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.UnAuthorizedException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Authority;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.AuthorityRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.AuthorityEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.AuthorityResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private CommonService commonService;

    @Transactional
    public void createAuthority(AuthorityCreationDto dto) {
        GrantAccess grantAccess = commonService.getGrantAccess(dto.getAppId(),dto.getGrantAccessId());
        Authority authority = authorityRepository.findByGrantAccessAndAuthority(grantAccess,dto.getAuthority().toUpperCase());
        if(authority!=null)
            throw new AlreadyExistsException("Authority already exists");
        authority = new Authority();
        authority.setAuthority(dto.getAuthority().toUpperCase());
        authority.setGrantAccess(grantAccess);
        authorityRepository.save(authority);
    }

    @Transactional
    public void editAuthority(AuthorityEditRequest request) {
        GrantAccess grantAccess = commonService.getGrantAccess(request.getAppId(),request.getGrantAccessId());
        Authority authority = authorityRepository.getOne(request.getAuthorityId());
        if(!authority.getGrantAccess().getId().equals(grantAccess.getId()))
            throw new UnAuthorizedException("You are not authorized.");
        Authority toSave = authorityRepository.findByGrantAccessAndAuthority(grantAccess,request.getAuthority());
        if(toSave!=null){
            if(toSave.getId().equals(request.getAuthorityId()))
                return;
            else
                throw new AlreadyExistsException("Authority already exists");
        }
        authority.setAuthority(request.getAuthority().toUpperCase());
        authorityRepository.save(authority);
    }

    @Transactional
    public void deleteAuthority(Long appId, Long grantAccessId, Long id) {
        GrantAccess grantAccess = commonService.getGrantAccess(appId,grantAccessId);
        Authority authority = authorityRepository.getOne(id);
        if(authority==null)
            throw new NotFoundException("No Authority Found.");
        if(!authority.getGrantAccess().getId().equals(grantAccess.getId()))
            throw new UnAuthorizedException("You are not authorized.");
        authorityRepository.delete(authority);
    }

    @Transactional
    public AuthorityResponseDto getAuthority(Long grantAccessId, Long appId, Long id) {
        GrantAccess grantAccess = commonService.getGrantAccess(appId,grantAccessId);
        Authority authority = authorityRepository.getOne(id);
        if(authority==null)
            throw new NotFoundException("No Authority Found.");
        if(!authority.getGrantAccess().getId().equals(grantAccess.getId()))
            throw new UnAuthorizedException("You are not authorized. Are you trying to sneak in?");
        return getAuthorityDetails(authority);
    }

    private AuthorityResponseDto getAuthorityDetails(Authority authority) {
        AuthorityResponseDto response = new AuthorityResponseDto();
        response.setAuthority(authority.getAuthority());
        response.setId(authority.getId());
        return response;
    }

    @Transactional
    public List<AuthorityResponseDto> getAllAuthority(GrantAccess grantAccess, int page, int size, String[] sort, String search) {
        Pageable pageValue = commonService.getPageable(sort, size, page);
        List<Authority> authorityList = null;
        if(search != null&&!search.isEmpty()&&!search.equals(" "))
            authorityList = authorityRepository.findByGrantAccessAndAuthorityContainingIgnoreCase(grantAccess,search,pageValue);
        else
            authorityList = authorityRepository.findByGrantAccess(grantAccess,pageValue);
        if(authorityList==null)
            throw new NotFoundException("No authority found");
        List<AuthorityResponseDto> response = new ArrayList<>();
        authorityList.forEach(authority -> response.add(getAuthorityDetails(authority)));
        return response;
    }

    @Transactional
    public Long countAuthority(GrantAccess grantAccess, String search) {
        if(search != null&&!search.isEmpty()&&!search.equals(" "))
            return authorityRepository.countByGrantAccessAndAuthorityContainingIgnoreCase(grantAccess, search);
        return  authorityRepository.countByGrantAccess(grantAccess);
    }
}
