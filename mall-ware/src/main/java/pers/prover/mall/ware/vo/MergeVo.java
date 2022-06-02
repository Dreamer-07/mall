package pers.prover.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/2 15:31
 */
@Data
public class MergeVo {

    private Long purchaseId;

    private List<Long> items;

}
