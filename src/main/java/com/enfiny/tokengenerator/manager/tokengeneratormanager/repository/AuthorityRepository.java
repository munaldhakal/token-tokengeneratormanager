package com.enfiny.tokengenerator.manager.tokengeneratormanager.repository;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Authority;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByGrantAccessAndAuthority(GrantAccess grantAccess, String authority);

    List<Authority> findByGrantAccessAndAuthorityContainingIgnoreCase(GrantAccess grantAccess, String search, Pageable pageValue);

    List<Authority> findByGrantAccess(GrantAccess grantAccess, Pageable pageValue);

    Long countByGrantAccessAndAuthorityContainingIgnoreCase(GrantAccess grantAccess, String search);

    Long countByGrantAccess(GrantAccess grantAccess);
}
