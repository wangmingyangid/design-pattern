package org.wmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wmy
 * @create 2021-06-17 15:07
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchWord {

    // 关键词
    private String keyWord;
    // 最后更新时间戳
    private long lastUpdateTime;
    // 引用次数
    private int num;
}
