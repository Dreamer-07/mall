package pers.prover.mall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.coupon.dao.SmsSkuBoundsDao;
import pers.prover.mall.coupon.entity.SmsSkuBoundsEntity;
import pers.prover.mall.coupon.service.SmsSkuBoundsService;


@Service("smsSkuBoundsService")
public class SmsSkuBoundsServiceImpl extends ServiceImpl<SmsSkuBoundsDao, SmsSkuBoundsEntity> implements SmsSkuBoundsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsSkuBoundsEntity> page = this.page(
                new Query<SmsSkuBoundsEntity>().getPage(params),
                new QueryWrapper<SmsSkuBoundsEntity>()
        );

        return new PageUtils(page);
    }

}