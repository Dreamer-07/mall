package pers.prover.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/2 15:58
 */
@Data
public class PurchaseReqVo {

    private Long id;

    private List<PurchaseDetailReqVo> items;

    @Data
    public static class PurchaseDetailReqVo {

        private Long itemId;

        private Integer status;

        private String reason;

    }
}
