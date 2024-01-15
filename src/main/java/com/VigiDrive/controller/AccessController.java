package com.VigiDrive.controller;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;
import com.VigiDrive.service.AccessService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AccessController {

    private AccessService accessService;

    @GetMapping("/drivers/{driver-id}/accesses/{access-id}")
    public AccessDTO getAccess(Authentication auth,
                               @PathVariable("driver-id") Long driverId,
                               @PathVariable("access-id") Long accessId) {
        return accessService.getAccess(auth, driverId, accessId);
    }

    @PostMapping("/managers/{manager-id}/accesses")
    public AccessDTO requestAccess(Authentication auth,
                                   @PathVariable("manager-id") Long managerId,
                                   @RequestBody @Valid AccessRequest access) throws UserException {
        return accessService.requestAccess(auth, managerId, access);
    }

    @PostMapping("/drivers/{driver-id}/accesses/{access-id}")
    public AccessDTO giveAccess(Authentication auth,
                                @PathVariable("driver-id") Long driverId,
                                @PathVariable("access-id") Long accessId) throws UserException {
        return accessService.giveAccess(auth, driverId, accessId);
    }

    @PatchMapping("/drivers/{driver-id}/accesses/{access-id}/stop")
    public AccessDTO stopAccess(Authentication auth,
                                @PathVariable("driver-id") Long driverId,
                                @PathVariable("access-id") Long accessId) throws UserException {
        return accessService.stopAccess(auth, driverId, accessId);
    }

    @PatchMapping("/managers/{manager-id}/accesses/{access-id}/extend")
    public AccessDTO extendAccess(Authentication auth,
                                  @PathVariable("manager-id") Long managerId,
                                  @PathVariable("access-id") Long accessId,
                                  @RequestBody @Valid ExtendAccessRequest access) throws UserException {
        return accessService.extendAccess(auth, managerId, accessId, access);
    }

    @GetMapping("/drivers/{driver-id}/accesses/inactive")
    public List<AccessDTO> getAllInactiveAccessesByDriver(Authentication auth,
                                                          @PathVariable("driver-id") Long driverId)
            throws UserException {
        return accessService.getAllInactiveAccessesByDriver(auth, driverId);
    }

    @GetMapping("/drivers/{driver-id}/accesses/active")
    public List<AccessDTO> getAllActiveAccessesByDriver(Authentication auth,
                                                        @PathVariable("driver-id") Long driverId)
            throws UserException {
        return accessService.getAllActiveAccessesByDriver(auth, driverId);
    }

    @GetMapping("/managers/{manager-id}/accesses/inactive")
    public List<AccessDTO> getAllInactiveAccessesByManager(Authentication auth,
                                                           @PathVariable("manager-id") Long managerId)
            throws UserException {
        return accessService.getAllInactiveAccessesByManager(auth, managerId);
    }

    @GetMapping("/managers/{manager-id}/accesses/active")
    public List<AccessDTO> getAllActiveAccessesByManager(Authentication auth,
                                                         @PathVariable("manager-id") Long managerId)
            throws UserException {
        return accessService.getAllActiveAccessesByManager(auth, managerId);
    }

    @GetMapping("/managers/{manager-id}/accesses/expiring")
    public List<AccessDTO> getAllExpiringAccessesByManager(Authentication auth,
                                                           @PathVariable("manager-id") Long managerId)
            throws UserException {
        return accessService.getAllExpiringAccessesByManager(auth, managerId);
    }
}
