package pers.prover.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取树型列表
     * @return
     */
    List<CategoryEntity> treeList();

    /**
     * 删除指定标识集合(ids)的数据
     * @param ids
     */
    void removeByIds(Long[] ids);

    /**
     * 获取能表示分类继承关系的 id 集合
     * @param catelogId
     * @return
     */
    List<Long> getCatelogPath(Long catelogId);

    /**
     * 级联更新分类信息
     * @param category
     */
    void updateCascade(CategoryEntity category);

    /**
     * 获取分类继承关系的字符串
     * @param catelogId
     * @return
     */
    String getCatelogPathStr(Long catelogId);

    /**
     * 获取分类名
     * @param catalogId
     * @return
     */
    String getCatelogName(Long catalogId);
}

