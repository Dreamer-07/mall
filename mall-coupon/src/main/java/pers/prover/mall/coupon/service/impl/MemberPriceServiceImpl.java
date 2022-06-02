package pers.prover.mall.coupon.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.to.SkuReductionTo;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.coupon.dao.MemberPriceDao;
import pers.prover.mall.coupon.entity.MemberPriceEntity;
import pers.prover.mall.coupon.service.MemberPriceService;


@Service("memberPriceService")
public class MemberPriceServiceImpl extends ServiceImpl<MemberPriceDao, MemberPriceEntity> implements MemberPriceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberPriceEntity> page = this.page(
                new Query<MemberPriceEntity>().getPage(params),
                new QueryWrapper<MemberPriceEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long skuId, List<SkuReductionTo.MemberPrice> memberPrice) {
        List<MemberPriceEntity> memberPriceEntities = memberPrice.stream()
                .filter(mp -> mp.getPrice().compareTo(BigDecimal.ZERO) > 0)
                .map(mp -> {
                    MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                    memberPriceEntity.setSkuId(skuId);
                    memberPriceEntity.setMemberLevelId(mp.getId());
                    memberPriceEntity.setMemberLevelName(mp.getName());
                    memberPriceEntity.setMemberPrice(mp.getPrice());
                    return memberPriceEntity;
                }).collect(Collectors.toList());
        this.saveBatch(memberPriceEntities);
    }

}