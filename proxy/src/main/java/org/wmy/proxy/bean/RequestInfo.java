package org.wmy.proxy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wmy
 * @create 2021-06-18 17:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {

    private long startTimestamp;
    private long responseTime;
    private String apiName;
}
