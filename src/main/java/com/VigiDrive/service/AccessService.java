package com.VigiDrive.service;

import com.VigiDrive.exceptions.AccessException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;

import java.util.List;

public interface AccessService {

    AccessDTO getAccessInfo(String email, Long driverId, Long accessId) throws UserException, AccessException;

    AccessDTO requestAccess(String email, Long managerId, AccessRequest access) throws UserException;

    AccessDTO giveAccess(String email, Long driverId, Long accessId) throws UserException;

    AccessDTO stopAccess(String email, Long driverId, Long accessId) throws UserException;

    AccessDTO extendAccess(String email, Long managerId, Long accessId, ExtendAccessRequest access)
            throws UserException;

    List<AccessDTO> getAllAccessRequestsByDriver(String email, Long driverId) throws UserException;

    List<AccessDTO> getAllInactiveAccessesByDriver(String email, Long driverId) throws UserException;

    List<AccessDTO> getAllActiveAccessesByDriver(String email, Long driverId) throws UserException;

    List<AccessDTO> getAllInactiveAccessesByManager(String email, Long managerId) throws UserException;

    List<AccessDTO> getAllActiveAccessesByManager(String email, Long managerId) throws UserException;

    List<AccessDTO> getAllExpiringAccessesByManager(String email, Long managerId) throws UserException;
}
