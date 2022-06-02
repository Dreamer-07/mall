package pers.prover.mall.product.vo;

import lombok.Data;
import pers.prover.mall.product.entity.AttrEntity;

import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/1 16:41
 */
@Data
public class AttrGroupRespVo {

    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 属性集合
     */
    private List<AttrRespVo> attrs;

}
