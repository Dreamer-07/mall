package pers.prover.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.SpuInfoDescDao;
import pers.prover.mall.product.entity.SpuInfoDescEntity;
import pers.prover.mall.product.service.SpuInfoDescService;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long spuInfoId, List<String> decript) {
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoId);
        spuInfoDescEntity.setDecript(String.join(",",decript));
        this.save(spuInfoDescEntity);
    }

}