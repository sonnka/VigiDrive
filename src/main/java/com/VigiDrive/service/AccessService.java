package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccessService {

    AccessDTO getAccess(Authentication auth, Long driverId, Long accessId);

    AccessDTO requestAccess(Authentication auth, Long managerId, AccessRequest access) throws UserException;

    AccessDTO giveAccess(Authentication auth, Long driverId, Long accessId) throws UserException;

    AccessDTO stopAccess(Authentication auth, Long driverId, Long accessId) throws UserException;

    AccessDTO extendAccess(Authentication auth, Long managerId, Long accessId, ExtendAccessRequest access)
            throws UserException;

    List<AccessDTO> getAllInactiveAccessesByDriver(Authentication auth, Long driverId) throws UserException;

    List<AccessDTO> getAllActiveAccessesByDriver(Authentication auth, Long driverId) throws UserException;

    List<AccessDTO> getAllInactiveAccessesByManager(Authentication auth, Long managerId) throws UserException;

    List<AccessDTO> getAllActiveAccessesByManager(Authentication auth, Long managerId) throws UserException;

    List<AccessDTO> getAllExpiringAccessesByManager(Authentication auth, Long managerId) throws UserException;
}
