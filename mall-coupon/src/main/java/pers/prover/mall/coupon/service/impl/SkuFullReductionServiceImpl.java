package pers.prover.mall.coupon.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.to.SkuReductionTo;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.coupon.dao.SkuBoundsDao;
import pers.prover.mall.coupon.dao.SkuFullReductionDao;
import pers.prover.mall.coupon.entity.SkuBoundsEntity;
import pers.prover.mall.coupon.entity.SkuFullReductionEntity;
import pers.prover.mall.coupon.service.MemberPriceService;
import pers.prover.mall.coupon.service.SkuBoundsService;
import pers.prover.mall.coupon.service.SkuFullReductionService;
import pers.prover.mall.coupon.service.SkuLadderService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void save(SkuReductionTo skuReductionTo) {
        BigDecimal fullPrice = skuReductionTo.getFullPrice();
        if (fullPrice.compareTo(BigDecimal.ZERO) > 0) {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
            skuFullReductionEntity.setAddOther(skuReductionTo.getPriceStatus());
            this.save(skuFullReductionEntity);
        }

        // 保存满减信息
        skuLadderService.save(skuReductionTo);

        // 保存会员信息
        memberPriceService.save(skuReductionTo.getSkuId(), skuReductionTo.getMemberPrice());

    }

}