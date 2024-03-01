package com.VigiDrive.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRequest implements Serializable {

    private String text;
}
