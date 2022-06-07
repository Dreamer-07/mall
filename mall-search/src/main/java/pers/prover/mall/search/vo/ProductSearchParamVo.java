package pers.prover.mall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 商品检索条件 Vo
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 13:31
 */
@Data
public class ProductSearchParamVo {

    /**
     * 搜索的关键字
     */
    private String keyword;

    /**
     * 选择的三级分类标识
     */
    private Long catelog3Id;

    /**
     * 选择的品牌标识集合
     */
    private List<Long> brandIds;

    /**
     * 排序方式
     *  格式 - `排序字段_排序类型`
     */
    private String sort;

    /**
     * 是否有库存
     *   0 - 没有
     *   1 - 有
     */
    private Integer hasStock;

    /**
     * 价格区间
     *  格式 - `最低价_最高价`
     */
    private String priceRange;

    /**
     * 选择的规格参数属性
     *   格式 - `属性标识_属性值[:属性值]`
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

}
