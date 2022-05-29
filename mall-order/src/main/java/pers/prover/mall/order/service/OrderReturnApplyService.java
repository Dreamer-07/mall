package pers.prover.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.order.entity.OrderReturnApplyEntity;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:28:33
 */
public interface OrderReturnApplyService extends IService<OrderReturnApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

