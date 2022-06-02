package pers.prover.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.ware.dao.PurchaseDetailDao;
import pers.prover.mall.ware.entity.PurchaseDetailEntity;
import pers.prover.mall.ware.service.PurchaseDetailService;
import pers.prover.mall.ware.service.WareSkuService;
import pers.prover.mall.ware.vo.PurchaseReqVo;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {


    @Autowired
    private WareSkuService wareSkuService;

    private static PurchaseDetailEntity apply(PurchaseDetailEntity purchaseDetailEntity) {
        return purchaseDetailEntity;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");

        LambdaQueryWrapper<PurchaseDetailEntity> selectLqw = new LambdaQueryWrapper<PurchaseDetailEntity>()
                .and(!StringUtils.isBlank(key), (qw) -> {
                    qw.eq(PurchaseDetailEntity::getPurchaseId, qw).or().like(PurchaseDetailEntity::getSkuNum, qw);
                })
                .eq(!StringUtils.isBlank(status), PurchaseDetailEntity::getStatus, status)
                .eq(!StringUtils.isBlank(wareId), PurchaseDetailEntity::getWareId, wareId);
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                selectLqw
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(Long id, Integer status) {
        // LambdaQueryWrapper<PurchaseDetailEntity> selectLqw = new LambdaQueryWrapper<PurchaseDetailEntity>()
        //         .eq(PurchaseDetailEntity::getPurchaseId, id);
        // this.list()
        this.baseMapper.updateStatus(id, status);
    }

    @Override
    public void done(List<PurchaseReqVo.PurchaseDetailReqVo> getPurchaseDetailReqVos) {
        // 修改采购需求信息
        List<PurchaseDetailEntity> purchaseDetailEntities = getPurchaseDetailReqVos.stream().map(purchaseDetailReqVo -> {
            PurchaseDetailEntity purchaseDetailEntity = this.getById(purchaseDetailReqVo.getItemId());
            purchaseDetailEntity.setStatus(purchaseDetailReqVo.getStatus());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(purchaseDetailEntities);

        // 修改对应 sku 的库存信息
        wareSkuService.done(purchaseDetailEntities);
    }

}