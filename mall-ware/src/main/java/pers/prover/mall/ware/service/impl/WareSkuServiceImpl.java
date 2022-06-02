package pers.prover.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.ware.client.ProductFeignClient;
import pers.prover.mall.ware.dao.WareSkuDao;
import pers.prover.mall.ware.entity.PurchaseDetailEntity;
import pers.prover.mall.ware.entity.WareSkuEntity;
import pers.prover.mall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        LambdaQueryWrapper<WareSkuEntity> selectLqw = new LambdaQueryWrapper<WareSkuEntity>()
                .eq(!StringUtils.isBlank(skuId) && !"0".equalsIgnoreCase(skuId), WareSkuEntity::getSkuId, skuId)
                .eq(!StringUtils.isBlank(wareId) && !"0".equalsIgnoreCase(wareId), WareSkuEntity::getWareId, wareId);
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                selectLqw
        );

        return new PageUtils(page);
    }

    @Override
    public void done(List<PurchaseDetailEntity> purchaseDetailEntities) {
        for (PurchaseDetailEntity purchaseDetailEntity : purchaseDetailEntities) {
            // 获取 sku id
            Long skuId = purchaseDetailEntity.getSkuId();
            // 判断库存中是否含有对应的 sku 库存信息
            LambdaQueryWrapper<WareSkuEntity> selectLqw = new LambdaQueryWrapper<WareSkuEntity>()
                    .eq(WareSkuEntity::getSkuId, skuId);
            WareSkuEntity wareSkuEntity = this.getOne(selectLqw);

            if (wareSkuEntity == null) {
                wareSkuEntity = new WareSkuEntity();
                // 调用远程服务获取 sku 信息
                String skuName = (String) productFeignClient.getSkuName(skuId).get("data");
                // 不含有，新建
                wareSkuEntity.setSkuId(purchaseDetailEntity.getSkuId());
                wareSkuEntity.setSkuName(skuName);
                wareSkuEntity.setStock(purchaseDetailEntity.getSkuNum());
                wareSkuEntity.setWareId(purchaseDetailEntity.getWareId());
                wareSkuEntity.setStockLocked(0);
                this.save(wareSkuEntity);
            } else {
                // 含有，在原有基础上增加库存数量
                wareSkuEntity.setStock(wareSkuEntity.getStock() + purchaseDetailEntity.getSkuNum());
                this.updateById(wareSkuEntity);
            }
        }
    }

}