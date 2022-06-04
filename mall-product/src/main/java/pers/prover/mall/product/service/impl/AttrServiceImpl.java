package pers.prover.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.constant.ProductConstant;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.AttrDao;
import pers.prover.mall.product.entity.AttrEntity;
import pers.prover.mall.product.entity.ProductAttrValueEntity;
import pers.prover.mall.product.service.*;
import pers.prover.mall.product.vo.Attr;
import pers.prover.mall.product.vo.AttrReqVo;
import pers.prover.mall.product.vo.AttrRespVo;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {


    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void save(AttrReqVo attrReqVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrReqVo, attrEntity);
        this.save(attrEntity);
        if (attrReqVo.getAttrGroupId() != null) {
            attrAttrgroupRelationService.save(attrEntity.getAttrId(), attrReqVo.getAttrGroupId());
        }
    }

    @Override
    public PageUtils queryBaseListPage(Map<String, Object> params, Long catelogId) {
        // 查找三级分类的销售属性
        LambdaQueryWrapper<AttrEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(catelogId != 0L, AttrEntity::getCatelogId, catelogId);
        lqw.eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        String key = (String) params.get("key");
        lqw.and(!StringUtils.isBlank(key), q -> q.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key));
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), lqw
        );

        // 调整数据格式
        List<AttrRespVo> attrRespVos = page.getRecords().stream().map(attr -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            // copy 源数据
            BeanUtils.copyProperties(attr, attrRespVo);

            // 查找分类数据
            String catelogName = categoryService.getCatelogPathStr(attr.getCatelogId());
            attrRespVo.setCatelogName(catelogName);

            // 查找分组名字
            Long attrGroupId = attrAttrgroupRelationService.getAttrGroupId(attr.getAttrId());
            if (attrGroupId != null) {
                String groupName = attrGroupService.getGroupName(attrGroupId);
                attrRespVo.setGroupName(groupName);
            }

            return attrRespVo;
        }).collect(Collectors.toList());

        // 重新设置数据
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getDetailInAdmin(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        // get attrGroupId
        Long attrGroupId = attrAttrgroupRelationService.getAttrGroupId(attrId);
        if (attrGroupId != null) {
            attrRespVo.setAttrGroupId(attrGroupId);
        }
        // get catelogPath
        List<Long> catelogPath = categoryService.getCatelogPath(attrRespVo.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);
        return attrRespVo;
    }

    @Override
    @Transactional
    public void updateById(AttrReqVo attrReqVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrReqVo, attrEntity);
        this.updateById(attrEntity);
        if (attrReqVo.getAttrGroupId() != null) {
            attrAttrgroupRelationService.update(attrReqVo.getAttrId(), attrReqVo.getAttrGroupId());
        }
    }

    @Override
    public PageUtils pageByNotInIds(Map<String, Object> params, List<Long> attrIds, Long catelogId) {
        LambdaQueryWrapper<AttrEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AttrEntity::getCatelogId, catelogId);
        lqw.notIn(attrIds.size() > 0,AttrEntity::getAttrId, attrIds);
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                lqw
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils querySaleListPage(Map<String, Object> params, Long catelogId) {
        // 查找三级分类的销售属性
        LambdaQueryWrapper<AttrEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(catelogId != 0L, AttrEntity::getCatelogId, catelogId);
        lqw.eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        String key = (String) params.get("key");
        lqw.and(!StringUtils.isBlank(key), q -> q.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key));
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), lqw
        );

        // 调整数据格式
        List<AttrRespVo> attrRespVos = page.getRecords().stream().map(attr -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            // copy 源数据
            BeanUtils.copyProperties(attr, attrRespVo);

            // 查找分类数据
            String catelogName = categoryService.getCatelogPathStr(attr.getCatelogId());
            attrRespVo.setCatelogName(catelogName);

            // 查找分组名字
            Long attrGroupId = attrAttrgroupRelationService.getAttrGroupId(attr.getAttrId());
            if (attrGroupId != null) {
                String groupName = attrGroupService.getGroupName(attrGroupId);
                attrRespVo.setGroupName(groupName);
            }

            return attrRespVo;
        }).collect(Collectors.toList());

        // 重新设置数据
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public List<ProductAttrValueEntity> getBaseListBySpuId(Long spuId) {
        return productAttrValueService.getBySpuId(spuId);
    }

    @Override
    public void updateBaseAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntityList) {
        productAttrValueService.updateAttr(spuId, productAttrValueEntityList);
    }

    @Override
    public List<AttrEntity> listByIdsAndSearchType(Collection<Long> attrIds) {
        LambdaQueryWrapper<AttrEntity> selectLqw = new LambdaQueryWrapper<AttrEntity>()
                .in(AttrEntity::getAttrId, attrIds)
                .eq(AttrEntity::getSearchType, 1);
        return this.list(selectLqw);
    }


}