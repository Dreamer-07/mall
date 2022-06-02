package pers.prover.mall.product.service.impl;

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

}