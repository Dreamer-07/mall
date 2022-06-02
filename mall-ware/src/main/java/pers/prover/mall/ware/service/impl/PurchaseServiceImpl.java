package pers.prover.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.constant.WareConstant;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.ware.dao.PurchaseDao;
import pers.prover.mall.ware.entity.PurchaseDetailEntity;
import pers.prover.mall.ware.entity.PurchaseEntity;
import pers.prover.mall.ware.service.PurchaseDetailService;
import pers.prover.mall.ware.service.PurchaseService;
import pers.prover.mall.ware.vo.MergeVo;
import pers.prover.mall.ware.vo.PurchaseReqVo;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    // @Autowired
    // private PurchaseService purchaseService;
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<PurchaseEntity> selectLqw = new LambdaQueryWrapper<PurchaseEntity>()
                .and(!StringUtils.isBlank(key), (qw) -> {
                    qw.eq(PurchaseEntity::getId, key).or().like(PurchaseEntity::getAssigneeName, key);
                });


        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                selectLqw
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnreceiveList(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> selectLqw = new LambdaQueryWrapper<PurchaseEntity>();
        selectLqw.eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.CREATE.getCode())
                .or()
                .eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                selectLqw
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveMegre(MergeVo mergeVo) {
        // 判断采购单是否存在
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            // 新增
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATE.getCode());
            purchaseEntity.setPriority(1);
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setCreateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(mergeVo.getItems());
        for (PurchaseDetailEntity purchaseDetailEntity : purchaseDetailEntities) {
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            purchaseDetailEntity.setPurchaseId(purchaseId);
        }
        purchaseDetailService.updateBatchById(purchaseDetailEntities);

    }

    @Override
    public void received(List<Long> purchaseIds) {
        List<PurchaseEntity> purchaseEntities = purchaseIds.stream().map(purchaseId -> {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(purchaseId);
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.PURCHASING.getCode());
            // 修改对应的采购需求状态
            purchaseDetailService.updateDetail(purchaseId, WareConstant.PurchaseDetailStatusEnum.PURCHASING.getCode());
            return purchaseEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(purchaseEntities);
    }

    @Override
    @Transactional
    public void done(PurchaseReqVo purchaseReqVo) {
        // 采购需求的状态
        purchaseDetailService.done(purchaseReqVo.getItems());

        boolean flag = purchaseReqVo.getItems().stream()
                .anyMatch(purchaseDetailReqVo -> purchaseDetailReqVo.getStatus() == WareConstant.PurchaseDetailStatusEnum.FAILED.getCode());

        // 修改采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseReqVo.getId());
        purchaseEntity.setStatus(!flag ? WareConstant.PurchaseStatusEnum.COMPLETED.getCode() : WareConstant.PurchaseStatusEnum.FAILED.getCode());
        this.updateById(purchaseEntity);
    }

    @Override
    @Transactional
    public void updateDetail(PurchaseEntity purchase) {
        purchaseDetailService.updateDetail(purchase.getId(), purchase.getStatus());
        this.updateById(purchase);
    }

}