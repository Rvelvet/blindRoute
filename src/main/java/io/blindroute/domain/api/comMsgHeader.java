package io.blindroute.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class comMsgHeader {
    private String requestMsgID;
    private String responseMsgID;
    private String responseTime;
    private String returnCode;
    private String errMsg;
    private String successYN;

}
