package com.VigiDrive.service;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;

public interface AdminService {

    AdminDTO registerAdmin(RegisterRequest newAdmin);
}
