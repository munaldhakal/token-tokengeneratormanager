package com.enfiny.tokengenerator.manager.tokengeneratormanager.repository;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndStatus(String username, Status active);

    User findByAppAndUsernameAndStatus(App app, String username, Status active);

    boolean existsByAppAndUsernameAndStatus(App app, String username, Status active);

    User findByIdAndStatus(Long id, Status active);

    List<User> findByAppAndStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(App app, Status active, String search, String search1, Pageable pageValue);

    List<User> findByAppAndStatus(App app, Status active, Pageable pageValue);

    Long countByAppAndStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(App app, Status active, String search, String search1);

    Long countByAppAndStatus(App app, Status active);
}
