package pers.prover.mall.coupon.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.to.SkuReductionTo;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.coupon.dao.SkuLadderDao;
import pers.prover.mall.coupon.entity.SkuLadderEntity;
import pers.prover.mall.coupon.service.SkuLadderService;


@Service("skuLadderService")
public class SkuLadderServiceImpl extends ServiceImpl<SkuLadderDao, SkuLadderEntity> implements SkuLadderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuLadderEntity> page = this.page(
                new Query<SkuLadderEntity>().getPage(params),
                new QueryWrapper<SkuLadderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(SkuReductionTo skuReductionTo) {
        BigDecimal fullPrice = skuReductionTo.getFullPrice();
        if (fullPrice.compareTo(BigDecimal.ZERO) > 0) {
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
            skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
            this.save(skuLadderEntity);
        }
    }

}