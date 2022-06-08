package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.ProductAttrValueDao;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import pers.prover.mall.product.service.AttrService;
import pers.prover.mall.product.service.ProductAttrValueService;
import pers.prover.mall.product.vo.SpuSaveVo;
import pers.prover.mall.product.vo.api.SkuItemVo;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long spuInfoId, List<SpuSaveVo.BaseAttrs> baseAttrs) {
        baseAttrs.forEach(attr -> {
            // 封装规格参数
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setAttrId(attr.getAttrId());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            // 获取属性名
            String attrName = attrService.getById(attr.getAttrId()).getAttrName();
            productAttrValueEntity.setAttrName(attrName);
            productAttrValueEntity.setSpuId(spuInfoId);
            // 保存
            this.save(productAttrValueEntity);
        });
    }

    @Override
    public List<ProductAttrValueEntity> getBySpuId(Long spuId) {
        LambdaQueryWrapper<ProductAttrValueEntity> selectLqw = new LambdaQueryWrapper<ProductAttrValueEntity>()
                .eq(ProductAttrValueEntity::getSpuId, spuId);
        return this.list(selectLqw);
    }

    @Override
    public void updateAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntityList) {
        // 删除已有的
        LambdaQueryWrapper<ProductAttrValueEntity> removeLqw = new LambdaQueryWrapper<ProductAttrValueEntity>()
                .eq(ProductAttrValueEntity::getSpuId, spuId);
        this.remove(removeLqw);

        // 添加
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueEntityList.stream()
                .peek(productAttrValueEntity -> productAttrValueEntity.setSpuId(spuId))
                .collect(Collectors.toList());

        this.saveBatch(productAttrValueEntityList);
    }

    @Override
    public List<SkuItemVo.SpuBaseAttrVo> baseAttrList(Long catalogId, Long spuId) {
        return this.baseMapper.baseAttrList(catalogId, spuId);
    }

}