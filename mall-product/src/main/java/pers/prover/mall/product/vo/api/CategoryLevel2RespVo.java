package pers.prover.mall.product.vo.api;

import lombok.Data;

import java.util.List;

/**
 * 二级分类响应数据体
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/4 15:55
 */
@Data
public class CategoryLevel2RespVo {

    private Long catalog1Id;

    private List<CategoryLevel3RespVo> catalog3List;

    private String id;

    private String name;

    @Data
    public static class CategoryLevel3RespVo {

        private Long catalog2Id;

        private String id;

        private String name;

    }
}
