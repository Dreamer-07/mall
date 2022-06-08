package pers.prover.mall.product.vo.api;

import lombok.Data;
import pers.prover.mall.product.entity.SkuImagesEntity;
import pers.prover.mall.product.entity.SkuInfoEntity;
import pers.prover.mall.product.entity.SpuInfoDescEntity;

import java.util.List;

/**
 * sku 详情
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/8 15:45
 */
@Data
public class SkuItemVo {

    /**
     * sku 基本信息
     */
    private SkuInfoEntity skuInfoEntity;

    /**
     * 是否有库存
     */
    private boolean hasStock = true;

    /**
     * sku 对应的 spu 的销售属性
     */
    private List<SpuSaleAttrVo> spuSaleAttrVoList;

    /**
     * spu 商品的介绍
     */
    private SpuInfoDescEntity spuInfoDescEntity;

    /**
     * sku 对应的 spu 的规格参数
     */
    private List<SpuBaseAttrVo> spuBaseAttrVoList;

    /**
     * sku 的图片信息
     */
    private List<SkuImagesEntity> skuImagesEntityList;

    /**
     * spu 规格参数
     */
    @Data
    public static class SpuBaseAttrVo {
        /**
         * 组名
         */
        private String groupName;

        /**
         * 一个组名可以有多个属性
         */
        private List<BaseAttrVo> baseAttrVoList;

        /**
         * 规格参数
         */
        @Data
        public static class BaseAttrVo {
            private String attrName;

            private String attrValues;
        }
    }

    /**
     * spu 销售属性
     */
    @Data
    public static class SpuSaleAttrVo {
        private Long attrId;

        private String attrName;

        private List<SaleAttrValuesVo> saleAttrValuesVoList;

        @Data
        public static class SaleAttrValuesVo {
            /**
             * 销售属性的值
             */
            private String attrValue;

            /**
             * 关联的 skuId 集合
             */
            private String skuIds;
        }
    }
}
