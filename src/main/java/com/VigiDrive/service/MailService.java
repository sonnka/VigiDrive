package com.VigiDrive.service;

import com.VigiDrive.exceptions.MailException;

public interface MailService {

    void sendNewAdminMessage(String receiverEmail) throws MailException;

    void sendNotApprovedAdminMessage(String receiverEmail) throws MailException;

    void sendApprovedAdminMessage(String receiverEmail, String tempPassword) throws MailException;
}
