package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.AttrGroupDao;
import pers.prover.mall.product.entity.AttrAttrgroupRelationEntity;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.AttrGroupEntity;
import pers.prover.mall.product.service.AttrAttrgroupRelationService;
import pers.prover.mall.product.service.AttrGroupService;
import pers.prover.mall.product.service.AttrService;
import pers.prover.mall.product.vo.AttrAttrgroupRelationReqVo;
import pers.prover.mall.product.vo.AttrGroupRespVo;
import pers.prover.mall.product.vo.AttrRespVo;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrService attrService;

    @Override

    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        // ?????????????????????
        String key = (String) params.get("key");
        // ????????????
        LambdaQueryWrapper<AttrGroupEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(catelogId != 0L, AttrGroupEntity::getCatelogId, catelogId);
        lqw.and(!StringUtils.isBlank(key), q -> {
            q.eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key);
        });
        // ????????????
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), lqw);
        return new PageUtils(page);
    }

    @Override
    public String getGroupName(Long attrGroupId) {
        LambdaQueryWrapper<AttrGroupEntity> lqw = new LambdaQueryWrapper<>();
        lqw.select(AttrGroupEntity::getAttrGroupName);
        lqw.eq(AttrGroupEntity::getAttrGroupId, attrGroupId);
        return this.getOne(lqw).getAttrGroupName();
    }

    @Override
    public Long getCatelogId(Long attrGroupId) {
        LambdaQueryWrapper<AttrGroupEntity> lqw = new LambdaQueryWrapper<>();
        lqw.select(AttrGroupEntity::getCatelogId, AttrGroupEntity::getAttrGroupId);
        lqw.eq(AttrGroupEntity::getAttrGroupId, attrGroupId);
        return this.getOne(lqw).getCatelogId();
    }

    @Override
    public List<AttrEntity> getAttrGroupRelationAttr(String attrGroupId) {
        List<AttrEntity> attrEntities = null;

        // ????????????????????????????????????
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> selectAttrIdsLqw = new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .select(AttrAttrgroupRelationEntity::getAttrId)
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId);
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationService.list(selectAttrIdsLqw);

        if (attrAttrgroupRelationEntities != null && attrAttrgroupRelationEntities.size() > 0) {
            List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            LambdaQueryWrapper<AttrEntity> selectAttrLqw = new LambdaQueryWrapper<AttrEntity>().in(AttrEntity::getAttrId, attrIds);
            attrEntities = attrService.list(selectAttrLqw);
        }
        return attrEntities;
    }

    @Override
    public PageUtils getAttrGroupNoRelationAttr(Map<String, Object> params, String attrGroupId) {
        // ??????????????? ???????????? ?????? ?????? ????????????????????? ??????
        IPage<AttrGroupEntity> page = null;
        // 1. ????????????
        Long catelogId = this.getById(attrGroupId).getCatelogId();
        // 2. ????????????????????????????????????
        LambdaQueryWrapper<AttrGroupEntity> selectAttrGroupLqw = new LambdaQueryWrapper<>();
        selectAttrGroupLqw.select(AttrGroupEntity::getAttrGroupId);
        selectAttrGroupLqw.eq(AttrGroupEntity::getCatelogId, catelogId);
        List<AttrGroupEntity> attrGroupEntities = this.list(selectAttrGroupLqw);
        // 3. ??????????????????????????????????????????
        List<Long> attrGroupIds = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());

        // 4. ??????????????????????????????
        List<Long> attrIds = attrAttrgroupRelationService.getAttrIds(attrGroupIds);

        // 5. ??????????????????????????????????????????
        return attrService.pageByNotInIds(params, attrIds, catelogId);
    }

    @Override
    public void saveAttrRelation(List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos) {
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationReqVos);
    }

    @Override
    public void removeBatch(List<AttrAttrgroupRelationReqVo> attrAttrgroupRelationReqVos) {
        this.baseMapper.deleteBatch(attrAttrgroupRelationReqVos);
    }

    @Override
    public List<AttrGroupRespVo> getAttrGroupWithAttrByCatlogId(Long catlogId) {
        // ????????????????????????
        LambdaQueryWrapper<AttrGroupEntity> selectAttrGroupLqw = new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catlogId);
        List<AttrGroupEntity> attrGroupEntities = this.list(selectAttrGroupLqw);

        if (attrGroupEntities != null && attrGroupEntities.size() > 0) {
            return attrGroupEntities.stream().map(attrGroupEntity -> {
                AttrGroupRespVo attrGroupRespVo = new AttrGroupRespVo();
                BeanUtils.copyProperties(attrGroupEntity, attrGroupRespVo);

                // ??????????????????
                List<Long> attrIds = attrAttrgroupRelationService.getAttrIds(attrGroupEntity.getAttrGroupId());

                if (attrIds.size() > 0) {
                    List<AttrEntity> attrEntities = attrService.listByIds(attrIds);
                    attrGroupRespVo.setAttrs(attrEntities.stream().map(attrEntity -> {
                        AttrRespVo attrRespVo = new AttrRespVo();
                        BeanUtils.copyProperties(attrEntity, attrRespVo);
                        return attrRespVo;
                    }).collect(Collectors.toList()));
                }

                return attrGroupRespVo;
            }).collect(Collectors.toList());
        }

        return null;
    }

}