package com.VigiDrive.service;

import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;

import java.util.List;

public interface AccessService {
    AccessDTO requestAccess(Long managerId, AccessRequest access);

    AccessDTO giveAccess(Long driverId, Long accessId);

    AccessDTO stopAccess(Long driverId, Long accessId);

    AccessDTO extendAccess(Long managerId, Long accessId, ExtendAccessRequest access);

    List<AccessDTO> getAllInactiveAccessesByDriver(Long driverId);

    List<AccessDTO> getAllActiveAccessesByDriver(Long driverId);

    List<AccessDTO> getAllInactiveAccessesByManager(Long managerId);

    List<AccessDTO> getAllActiveAccessesByManager(Long managerId);

    List<AccessDTO> getAllExpiringAccessesByManager(Long managerId);
}
