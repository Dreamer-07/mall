package pers.prover.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:30:45
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

