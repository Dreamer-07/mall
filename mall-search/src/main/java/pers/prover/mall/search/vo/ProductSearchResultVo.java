package pers.prover.mall.search.vo;

import lombok.Data;
import pers.prover.mall.common.to.es.ProductMapping;

import java.util.List;

/**
 * 商品检索返回结果集 Vo
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 13:42
 */
@Data
public class ProductSearchResultVo {

    /**
     * sku 商品集合
     */
    private List<ProductMapping> productMappingList;

    /**
     * 检索时出现的所有分类
     */
    private List<CategoryVo> categoryVoList;

    /**
     * 检索时出现的所有品牌
     */
    private List<BrandVo> brandVoList;

    /**
     * 检索商品的所有规格参数
     */
    private List<Attr> attrList;

    /**
     * 已经使用的搜索条件
     */
    private List<SearchParamVo> searchParamVoList;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Integer totalDataCount;

    /**
     * 总页数
     */
    private Integer totalPages;

    @Data
    public static class Attr {
        /**
         * 属性标识
         */
        private Long attrId;
        /**
         * 属性名
         */
        private String attrName;
        /**
         * 属性值集合
         */
        private List<String> attrValues;
    }

    @Data
    public static class CategoryVo {
        /**
         * 分类标识
         */
        private Long catelogId;

        /**
         * 分类名
         */
        private String catelogName;
    }

    @Data
    public static class BrandVo {
        /**
         * 品牌标识
         */
        private Long brandId;

        /**
         * 品牌名
         */
        private String brandName;

        /**
         * 品牌 logo
         */
        private String brandImage;
    }

    /**
     * 已经选择的检索参数
     */
    @Data
    public static class SearchParamVo {
        /**
         * 参数标识，如果是品牌属性，则没有标识
         */
        private Long paramId;

        /**
         * 参数名
         */
        private String paramName;

        /**
         * 参数值
         */
        private List<String> paramValues;
    }

}
