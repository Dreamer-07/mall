package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.AttrAttrgroupRelationDao;
import pers.prover.mall.product.entity.AttrAttrgroupRelationEntity;
import pers.prover.mall.product.service.AttrAttrgroupRelationService;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(Long attrId, Long attrGroupId) {
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrId);
        attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
        attrAttrgroupRelationEntity.setAttrSort(0);
        this.save(attrAttrgroupRelationEntity);
    }

    @Override
    public Long getAttrGroupId(Long attrId) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> lqw = new LambdaQueryWrapper<>();
        lqw.select(AttrAttrgroupRelationEntity::getAttrGroupId, AttrAttrgroupRelationEntity::getAttrId);
        lqw.eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = this.getOne(lqw);
        if (attrAttrgroupRelationEntity == null) {
            return null;
        }
        return attrAttrgroupRelationEntity.getAttrGroupId();
    }

    @Override
    public void update(Long attrId, Long attrGroupId) {
        this.baseMapper.updateAttrGroupId(attrId, attrGroupId);
    }

    @Override
    public List<Long> getAttrIds(List<Long> attrGroupIds) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> selectAttrIdsLqw = new LambdaQueryWrapper<>();
        selectAttrIdsLqw.select(AttrAttrgroupRelationEntity::getAttrId);
        selectAttrIdsLqw.in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds);
        return this.list(selectAttrIdsLqw).stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
    }

    @Override
    public void saveBatch(List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationReqVos.stream().map(attrAttrgroupRelationReqVo -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(attrAttrgroupRelationReqVo, attrAttrgroupRelationEntity);
            attrAttrgroupRelationEntity.setAttrSort(0);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(attrAttrgroupRelationEntities);
    }

    @Override
    public List<Long> getAttrIds(Long attrGroupId) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> lqw = new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .select(AttrAttrgroupRelationEntity::getAttrId)
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId);
        return this.list(lqw).stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
    }

}