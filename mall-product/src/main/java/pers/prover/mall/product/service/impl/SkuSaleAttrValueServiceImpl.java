package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.SkuSaleAttrValueDao;
import pers.prover.mall.product.entity.SkuSaleAttrValueEntity;
import pers.prover.mall.product.service.SkuSaleAttrValueService;
import pers.prover.mall.product.vo.SpuSaveVo;
import pers.prover.mall.product.vo.api.SkuItemVo;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long skuId, List<SpuSaveVo.Skus.Attr> attr) {
        attr.forEach(a -> {
            SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
            BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
            skuSaleAttrValueEntity.setSkuId(skuId);
            this.save(skuSaleAttrValueEntity);
        });
    }

    @Override
    public List<SkuItemVo.SpuSaleAttrVo> saleAttrList(Long spuId) {
        return this.baseMapper.saleAttrList(spuId);
    }

    @Override
    public List<SkuSaleAttrValueEntity> listBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuSaleAttrValueEntity> selectLqw = new LambdaQueryWrapper<SkuSaleAttrValueEntity>()
                .eq(SkuSaleAttrValueEntity::getSkuId, skuId);
        return this.list(selectLqw);
    }

}