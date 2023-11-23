package com.VigiDrive.service;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;

import java.util.List;

public interface AccessService {
    AccessDTO requestAccess(Long managerId, AccessRequest access) throws UserException;

    AccessDTO giveAccess(Long driverId, Long accessId) throws UserException;

    AccessDTO stopAccess(Long driverId, Long accessId) throws UserException;

    AccessDTO extendAccess(Long managerId, Long accessId, ExtendAccessRequest access) throws UserException;

    List<AccessDTO> getAllInactiveAccessesByDriver(Long driverId) throws UserException;

    List<AccessDTO> getAllActiveAccessesByDriver(Long driverId) throws UserException;

    List<AccessDTO> getAllInactiveAccessesByManager(Long managerId) throws UserException;

    List<AccessDTO> getAllActiveAccessesByManager(Long managerId) throws UserException;

    List<AccessDTO> getAllExpiringAccessesByManager(Long managerId) throws UserException;
}
