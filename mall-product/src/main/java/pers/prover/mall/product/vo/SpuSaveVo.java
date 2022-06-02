/**
  * Copyright 2022 json.cn 
  */
package pers.prover.mall.product.vo;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2022-06-02 8:44:13
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class SpuSaveVo {

    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    // 发布状态
    private Integer publishStatus;
    // 商品描述(大图)
    private List<String> decript;
    // SKU 中会使用的图集
    private List<String> images;
    // 积分
    private Bounds bounds;
    // 规格参数
    private List<BaseAttrs> baseAttrs;
    // sku 集合
    private List<Skus> skus;

    @Data
    public static class BaseAttrs {
        // 属性标识
        private Long attrId;
        // 属性值
        private String attrValues;
        // 是否快熟展示
        private Integer showDesc;
    }

    @Data
    public static class Bounds {

        private BigDecimal buyBounds;
        private BigDecimal growBounds;

    }

    @Data
    public static class Skus {
        // sku 对应的 销售属性
        private List<Attr> attr;
        private String skuName;
        private BigDecimal price;
        private String skuTitle;
        private String skuSubtitle;
        // sku 会使用的 图集
        private List<Images> images;
        // 销售属性的集合
        private List<String> descar;
        // 折扣条件
        private int fullCount;
        // 折扣优惠
        private BigDecimal discount;
        // 折扣是否可以添加优惠
        private int countStatus;
        // 满减条件
        private BigDecimal fullPrice;
        // 满减优惠
        private BigDecimal reducePrice;
        // 满减是否可以添加优惠
        private int priceStatus;
        // 会员价格信息
        private List<MemberPrice> memberPrice;

        @Data
        public static class Attr {
            private int attrId;
            private String attrName;
            private String attrValue;
        }

        @Data
        public static class Images {
            private String imgUrl;
            private int defaultImg;
        }

        @Data
        public static class MemberPrice {
            private int id;
            private String name;
            private int price;
        }

    }


}