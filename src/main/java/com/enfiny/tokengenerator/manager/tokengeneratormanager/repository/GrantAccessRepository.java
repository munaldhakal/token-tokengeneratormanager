package com.enfiny.tokengenerator.manager.tokengeneratormanager.repository;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.GrantType;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.GrantAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GrantAccessRepository extends JpaRepository<GrantAccess, Long> {
    GrantAccess findByAppAndGrantType(App app, GrantType grantType);

    boolean existsByAppAndGrantType(App app, GrantType grantType);

    List<GrantAccess> findByAppAndGrantTypeContaining(App app, String search, Pageable pageValue);

    List<GrantAccess> findByApp(App app, Pageable pageValue);

    Long countByAppAndGrantTypeContaining(App app, String search);

    Long countByApp(App app);
}
