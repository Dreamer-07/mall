package pers.prover.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.coupon.entity.SmsHomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:26:15
 */
public interface SmsHomeSubjectSpuService extends IService<SmsHomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

