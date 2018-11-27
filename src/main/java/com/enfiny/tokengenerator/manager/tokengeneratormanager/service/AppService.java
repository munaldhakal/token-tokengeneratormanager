package com.enfiny.tokengenerator.manager.tokengeneratormanager.service;

import com.enfiny.tokengenerator.manager.tokengeneratormanager.dto.AppCreationDto;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.Status;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.exception.NotFoundException;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.App;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.model.Client;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.repository.AppRepository;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.request.AppEditRequest;
import com.enfiny.tokengenerator.manager.tokengeneratormanager.response.AppResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private CommonService commonService;

    @Transactional
    public void createApp(AppCreationDto dto) {
        Client client = commonService.checkIfAppDoesNotExists(dto.getAppName());
        App app = new App();
        app.setAppName(dto.getAppName());
        app.setClient(client);
        app.setStatus(Status.ACTIVE);
        appRepository.save(app);
    }

    @Transactional
    public void editApp(AppEditRequest request) {
        App app = commonService.getApp(request.getAppId());
        commonService.checkIfAppDoesNotExists(request.getAppName());
        app.setAppName(request.getAppName());
        appRepository.save(app);
    }

    @Transactional
    public void deleteApp(Long id) {
        App app = commonService.getApp(id);
        app.setStatus(Status.DELETED);
        appRepository.save(app);
    }

    @Transactional
    public AppResponseDto getApp(Long id) {
        App app = commonService.getApp(id);
        return getAppDetails(app);
    }

    private AppResponseDto getAppDetails(App app) {
        AppResponseDto response = new AppResponseDto();
        response.setAppName(app.getAppName());
        response.setId(app.getId());
        return response;
    }

    @Transactional
    public List<AppResponseDto> getAllApps(Client client,int page, int size, String[] sort, String search) {
        System.out.println("###################################"+client.getId());
        Pageable pageValue = commonService.getPageable(sort, size, page);
        List<App> appList = null;
        if(search!=null||!search.equals(""))
            appList = appRepository.findByStatusAndClientAndAppNameContainingIgnoreCase(Status.ACTIVE,client,search,pageValue);
        else
            appList = appRepository.findAllByStatusAndClient(Status.ACTIVE,client,pageValue);
        if(appList.isEmpty())
            throw new NotFoundException("No apps found.");
        List<AppResponseDto> response = new ArrayList<>();
        appList.forEach(app->response.add(getAppDetails(app)));
        return response;
    }

    @Transactional
    public Long countApps(Client client, String search) {
        if(search != null&&!search.isEmpty()&&!search.equals(" "))
            return appRepository.countByStatusAndClientAndAppNameContainingIgnoreCase(Status.ACTIVE, client, search);
        return appRepository.countByStatusAndClient(Status.ACTIVE,client);
    }
}
