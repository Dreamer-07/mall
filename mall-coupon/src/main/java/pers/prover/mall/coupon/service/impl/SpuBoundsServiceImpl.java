package pers.prover.mall.coupon.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.to.SpuBoundTo;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.coupon.dao.SpuBoundsDao;
import pers.prover.mall.coupon.entity.SpuBoundsEntity;
import pers.prover.mall.coupon.service.SpuBoundsService;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(SpuBoundTo spuBoundTo) {
        System.out.println(spuBoundTo);
        BigDecimal buyBounds = spuBoundTo.getBuyBounds();
        BigDecimal growBounds = spuBoundTo.getGrowBounds();
        if (buyBounds.compareTo(BigDecimal.ZERO) > 0 || growBounds.compareTo(BigDecimal.ZERO) > 0) {
            SpuBoundsEntity spuBoundsEntity = new SpuBoundsEntity();
            BeanUtils.copyProperties(spuBoundTo, spuBoundsEntity);
            this.save(spuBoundsEntity);
        }
    }

}