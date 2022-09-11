package com.klc.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = -2898578180195614188L;

    private Long teamId;

    private String password;
}
