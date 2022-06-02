package pers.prover.mall.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/2 10:32
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

    @Data
    public static class MemberPrice {
        private Long id;
        private String name;
        private BigDecimal price;
    }
}
