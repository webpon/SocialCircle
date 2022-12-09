package com.socialCircle.msg;

import lombok.Data;

@Data
public class WithdrawMsg extends Message {
    private String msgId;
    private Boolean success;
}
