package com.enfiny.tokengenerator.manager.tokengeneratormanager.repository;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {

    App findByClientAndAppNameAndStatus(Client client, String app, Status active);

    List<App> findByStatusAndClientAndAppNameContainingIgnoreCase(Status active, Client client, String search, Pageable pageValue);

    List<App> findAllByStatusAndClient(Status active, Client client, Pageable pageValue);

    Long countByStatusAndClientAndAppNameContainingIgnoreCase(Status active, Client client, String search);

    Long countByStatusAndClient(Status active, Client client);
}
