package pers.prover.mall.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.prover.mall.common.constant.SearchIndexConstant;
import pers.prover.mall.common.to.es.ProductMapping;
import pers.prover.mall.search.config.ElasticSearchConfig;
import pers.prover.mall.search.service.SearchProductService;

import java.io.IOException;
import java.util.List;

/**
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/3 22:18
 */
@Service
@Slf4j
public class SearchProductServiceImpl implements SearchProductService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean saveProductUpInfo(List<ProductMapping> productMappingList) {
        // 创建一个 bulk 请求
        BulkRequest bulkRequest = new BulkRequest();
        productMappingList.forEach(productMapping -> {
            IndexRequest indexRequest = new IndexRequest(SearchIndexConstant.PRODUCT);
            indexRequest.id(String.valueOf(productMapping.getSkuId()));
            indexRequest.source(JSONObject.toJSONString(productMapping), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
            return !bulkResponse.hasFailures();
        } catch (IOException e) {
            log.error("保存商品库存信息失败");
            return false;
        }
    }
}
