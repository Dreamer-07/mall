package pers.prover.mall.search.service;

import pers.prover.mall.common.to.es.ProductMapping;
import pers.prover.mall.search.vo.ProductSearchParamVo;
import pers.prover.mall.search.vo.ProductSearchResultVo;

import java.util.List;

public interface SearchProductService {
    /**
     * 保存商品上架信息
     * @param productMappingList
     * @return
     */
    boolean saveProductUpInfo(List<ProductMapping> productMappingList);

    /**
     * 搜索商品
     * @param productSearchParamVo
     * @return
     */
    ProductSearchResultVo search(ProductSearchParamVo productSearchParamVo);
}
