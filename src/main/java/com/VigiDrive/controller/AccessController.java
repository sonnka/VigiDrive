package com.VigiDrive.controller;

import com.VigiDrive.model.request.AccessRequest;
import com.VigiDrive.model.request.ExtendAccessRequest;
import com.VigiDrive.model.response.AccessDTO;
import com.VigiDrive.service.AccessService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccessController {

    private AccessService accessService;

    @PostMapping("/managers/{manager-id}/accesses")
    public AccessDTO requestAccess(@PathVariable("manager-id") Long managerId,
                                   @RequestBody @Valid AccessRequest access) {
        return accessService.requestAccess(managerId, access);
    }

    @PostMapping("/drivers/{driver-id}/accesses/{access-id}")
    public AccessDTO giveAccess(@PathVariable("driver-id") Long driverId,
                                @PathVariable("access-id") Long accessId) {
        return accessService.giveAccess(driverId, accessId);
    }

    @PatchMapping("/drivers/{driver-id}/accesses/{access-id}/stop")
    public AccessDTO stopAccess(@PathVariable("driver-id") Long driverId,
                                @PathVariable("access-id") Long accessId) {
        return accessService.stopAccess(driverId, accessId);
    }

    @PatchMapping("/managers/{manager-id}/accesses/{access-id}/extend")
    public AccessDTO extendAccess(@PathVariable("manager-id") Long managerId,
                                  @PathVariable("access-id") Long accessId,
                                  @RequestBody @Valid ExtendAccessRequest access) {
        return accessService.extendAccess(managerId, accessId, access);
    }

    @GetMapping("/drivers/{driver-id}/accesses/inactive")
    public List<AccessDTO> getAllInactiveAccessesByDriver(@PathVariable("driver-id") Long driverId) {
        return accessService.getAllInactiveAccessesByDriver(driverId);
    }

    @GetMapping("/drivers/{driver-id}/accesses/active")
    public List<AccessDTO> getAllActiveAccessesByDriver(@PathVariable("driver-id") Long driverId) {
        return accessService.getAllActiveAccessesByDriver(driverId);
    }

    @GetMapping("/managers/{manager-id}/accesses/inactive")
    public List<AccessDTO> getAllInactiveAccessesByManager(@PathVariable("manager-id") Long managerId) {
        return accessService.getAllInactiveAccessesByManager(managerId);
    }

    @GetMapping("/managers/{manager-id}/accesses/active")
    public List<AccessDTO> getAllActiveAccessesByManager(@PathVariable("manager-id") Long managerId) {
        return accessService.getAllActiveAccessesByManager(managerId);
    }

    @GetMapping("/managers/{manager-id}/accesses/expiring")
    public List<AccessDTO> getAllExpiringAccessesByManager(@PathVariable("manager-id") Long managerId) {
        return accessService.getAllExpiringAccessesByManager(managerId);
    }
}
