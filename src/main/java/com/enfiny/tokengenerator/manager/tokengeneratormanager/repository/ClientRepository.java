package com.enfiny.tokengenerator.manager.tokengeneratormanager.repository;


import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUsername(String username);

    Long countByStatus(Status active);

    List<Client> findByStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(Status active, String search, String search1, Pageable pageValue);

    Long countByStatusAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(Status active, String search, String search1);

    List<Client> findAllByStatus(Status active,Pageable pageValue);

}
