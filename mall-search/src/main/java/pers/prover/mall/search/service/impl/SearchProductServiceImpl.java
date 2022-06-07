package pers.prover.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.prover.mall.common.constant.ProductConstant;
import pers.prover.mall.common.constant.SearchIndexConstant;
import pers.prover.mall.common.to.es.ProductMapping;
import pers.prover.mall.common.utils.R;
import pers.prover.mall.search.client.ProductFeignClient;
import pers.prover.mall.search.config.ElasticSearchConfig;
import pers.prover.mall.search.constant.SearchConstant;
import pers.prover.mall.search.service.SearchProductService;
import pers.prover.mall.search.vo.ProductSearchParamVo;
import pers.prover.mall.search.vo.ProductSearchResultVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ProductFeignClient productFeignClient;

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

    @Override
    public ProductSearchResultVo search(ProductSearchParamVo productSearchParamVo) {
        SearchResponse searchResponse;

        try {
            // 构建检索商品的 DSL 查询语句
            SearchRequest searchRequest = buildSearchProductDSL(productSearchParamVo);
            // 检索
            searchResponse = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // System.out.println(searchResponse.toString());
        } catch (IOException e) {
            throw new RuntimeException("商品搜索失败，请联系管理员");
        }

        return buildSearchProductResult(searchResponse, productSearchParamVo);
    }

    private ProductSearchResultVo buildSearchProductResult(SearchResponse searchResponse, ProductSearchParamVo productSearchParamVo) {
        ProductSearchResultVo productSearchResultVo = new ProductSearchResultVo();
        // 命中的数据
        SearchHits hits = searchResponse.getHits();
        // 聚合数据
        Aggregations aggregations = searchResponse.getAggregations();
        // 封装分页数据
        productSearchResultVo.setPageNum(productSearchParamVo.getPageNum());
        productSearchResultVo.setTotalDataCount((int) hits.getTotalHits().value);
        productSearchResultVo.setTotalPages(productSearchResultVo.getTotalDataCount() + 1 % SearchConstant.SEARCH_PAGE_SIZE);
        // 封装实体数据
        List<ProductMapping> productMappingList = new ArrayList<>();
        for (SearchHit searchHit : hits.getHits()) {
            ProductMapping productMapping = JSON.parseObject(searchHit.getSourceAsString(), ProductMapping.class);
            productMapping.setSkuTitle(searchHit.getHighlightFields().get("skuTitle").getFragments()[0].toString());
            productMappingList.add(productMapping);
        }
        productSearchResultVo.setProductMappingList(productMappingList);

        // 封装聚合信息
        // 获取聚合规格参数信息
        ParsedNested parsedNested = aggregations.get("attrAgg");
        // 获取 attrId
        ParsedLongTerms attrIdAgg = parsedNested.getAggregations().get("attrIdAgg");
        List<ProductSearchResultVo.Attr> attrs = new ArrayList<>();
        List<ProductSearchResultVo.SearchParamVo> searchParamVoList = new ArrayList<>();
        // 获取作为检索条件的 attr
        List<Long> searchAttrIds = productSearchParamVo.getAttrs().stream().map(attr -> Long.valueOf(attr.split("_")[0])).collect(Collectors.toList());
        // 将得到的 attrAgg 封装成需要的数据
        for (Terms.Bucket attrIdBucket : attrIdAgg.getBuckets()) {
            long attrId = attrIdBucket.getKeyAsNumber().longValue();
            // 获取对应的属性名
            ParsedStringTerms attrNameAgg = attrIdBucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            // 获取属性值
            ParsedStringTerms attrValuesAgg = attrIdBucket.getAggregations().get("attrValuesAgg");
            List<String> attrValues = new ArrayList<>();
            for (Terms.Bucket attrValuesBucket : attrValuesAgg.getBuckets()) {
                attrValues.add(attrValuesBucket.getKeyAsString());
            }

            if (!searchAttrIds.contains(attrId)) {
                ProductSearchResultVo.Attr attr = new ProductSearchResultVo.Attr();
                // 聚合属性还未作为检索条件出现，需要显示
                attr.setAttrId(attrId);
                attr.setAttrName(attrName);
                attr.setAttrValues(attrValues);
                attrs.add(attr);
            } else {
                // 如果已经作为检索条件选择了，不需要作为条件筛选出现
                ProductSearchResultVo.SearchParamVo searchParamVo = new ProductSearchResultVo.SearchParamVo();
                searchParamVo.setParamId(attrId);
                searchParamVo.setParamName(attrName);
                searchParamVo.setParamValues(attrValues);
                searchParamVoList.add(searchParamVo);
            }
        }
        productSearchResultVo.setAttrList(attrs);

        // 获取商品的分类信息
        ParsedLongTerms catalogIdAgg = aggregations.get("catalogIdAgg");
        if (catalogIdAgg != null) {
            List<ProductSearchResultVo.CategoryVo> categoryVoList = new ArrayList<>();
            for (Terms.Bucket categoryIdBucket : catalogIdAgg.getBuckets()) {
                // 获取分类标识
                ProductSearchResultVo.CategoryVo categoryVo = new ProductSearchResultVo.CategoryVo();
                categoryVo.setCatelogId(categoryIdBucket.getKeyAsNumber().longValue());
                // 获取分类名
                ParsedStringTerms catalogNameAgg = categoryIdBucket.getAggregations().get("catalogNameAgg");
                String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
                categoryVo.setCatelogName(catalogName);
                categoryVoList.add(categoryVo);
            }
            productSearchResultVo.setCategoryVoList(categoryVoList);
        }

        // 获取商品的品牌信息
        ParsedLongTerms brandAgg = aggregations.get("brandAgg");
        if (brandAgg != null) {
            List<ProductSearchResultVo.BrandVo> brandVoList = new ArrayList<>();
            for (Terms.Bucket brandAggBucket : brandAgg.getBuckets()) {
                // 获取品牌标识
                ProductSearchResultVo.BrandVo brandVo = new ProductSearchResultVo.BrandVo();
                brandVo.setBrandId(brandAggBucket.getKeyAsNumber().longValue());
                // 获取品牌名
                ParsedStringTerms brandNameAgg = brandAggBucket.getAggregations().get("brandNameAgg");
                String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
                brandVo.setBrandName(brandName);
                // 获取品牌图片
                ParsedStringTerms brandImgAgg = brandAggBucket.getAggregations().get("brandImgAgg");
                String brandImage = brandImgAgg.getBuckets().get(0).getKeyAsString();
                brandVo.setBrandImage(brandImage);

                brandVoList.add(brandVo);
            }
            productSearchResultVo.setBrandVoList(brandVoList);
        } else {
            R brandNamesResult = productFeignClient.brandNameList(productSearchParamVo.getBrandIds());
            ProductSearchResultVo.SearchParamVo searchParamVo = new ProductSearchResultVo.SearchParamVo();
            searchParamVo.setParamName("品牌");
            searchParamVo.setParamValues(brandNamesResult.getData(new TypeReference<List<String>>(){}));
            searchParamVoList.add(searchParamVo);
        }

        productSearchResultVo.setSearchParamVoList(searchParamVoList);

        return productSearchResultVo;
    }

    /**
     * 构建搜索商品的 DSL 查询语句
     *
     * @param productSearchParamVo
     * @return
     */
    private SearchRequest buildSearchProductDSL(ProductSearchParamVo productSearchParamVo) {
        SearchRequest searchRequest = new SearchRequest(SearchIndexConstant.PRODUCT);
        SearchSourceBuilder searchSource = new SearchSourceBuilder();

        /*
         * query: {
         *   bool: {
         *       must: req.keyword
         *       filter: req.catelog3Id, req.brandId，req.hasStock, req.priceRange, req.attrs(nested)
         *   }
         * }
         */
        // 关键字搜索
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isBlank(productSearchParamVo.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", productSearchParamVo.getKeyword()));
            /*
             * 如果检索了关键字还需要进行高亮显示：
             *  hightlight: {
             *    "fields": resp.sku.skuTitle
             *    "pre_tags": "<b style='color:red'>"
             *    "post_tags": "</b>"
             * }
             * */
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            // 需要高亮显示的属性
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSource.highlighter(highlightBuilder);
        }
        // 检索分类
        if (productSearchParamVo.getCatelog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", productSearchParamVo.getCatelog3Id()));
        } else {
            // 如果没有锁定分类还需要通过聚合查找所有的分类
            TermsAggregationBuilder catelogAgg = AggregationBuilders.terms("catalogIdAgg").field("catalogId");
            catelogAgg.subAggregation(AggregationBuilders.terms("catalogNameAgg").field("catalogName"));
            searchSource.aggregation(catelogAgg);
        }
        // 检索品牌
        if (!CollectionUtils.isEmpty(productSearchParamVo.getBrandIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", productSearchParamVo.getBrandIds()));
        } else {
            // 如果没有锁定品牌还需要通过聚合查找所有的品牌
            TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId");
            brandAgg.subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"));
            brandAgg.subAggregation(AggregationBuilders.terms("brandImgAgg").field("brandImg"));
            searchSource.aggregation(brandAgg);
        }
        // 判断是否有库存
        if (productSearchParamVo.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", productSearchParamVo.getHasStock()));
        }
        // 判断价格区间
        if (!StringUtils.isBlank(productSearchParamVo.getPriceRange())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            // 调整请求数据格式
            String[] priceRange = productSearchParamVo.getPriceRange().split("_");
            if (priceRange.length == 2) {
                // 设定了最小最大价格区间
                rangeQueryBuilder.from(priceRange[0]);
                rangeQueryBuilder.to(priceRange[1]);
            } else if (priceRange.length == 1) {
                // 只设定了一个区间
                if (productSearchParamVo.getPriceRange().startsWith("_")) {
                    // 以 _ 开头, 设置了最大价格
                    rangeQueryBuilder.to(priceRange[0]);
                } else {
                    // 不以 _ 开头，设定了最小价格
                    rangeQueryBuilder.from(priceRange[0]);
                }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        /*
         * 检索属性
         *   query.nested.path
         *   query.nested.query.bool.must[]
         * */
        if (!CollectionUtils.isEmpty(productSearchParamVo.getAttrs())) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            for (String attr : productSearchParamVo.getAttrs()) {
                // 调整请求数据格式 - 属性标识_属性值[:属性值]
                String attrId = attr.split("_")[0];
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                String[] attrValues = attr.split("_")[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
            }
            boolQueryBuilder.filter(QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None));
        }
        searchSource.query(boolQueryBuilder);


        /*
         * sort: {
         *    req.sort
         * }
         * */
        // 构建排序 - `排序字段_排序类型`
        if (!StringUtils.isBlank(productSearchParamVo.getSort())) {
            String[] sort = productSearchParamVo.getSort().split("_");
            FieldSortBuilder sortBuilder = SortBuilders.fieldSort(sort[0]).order(SortOrder.valueOf(sort[1].toUpperCase()));
            searchSource.sort(sortBuilder);
        }

        /*
         * form: 分页
         * */
        searchSource.from((productSearchParamVo.getPageNum() - 1) * SearchConstant.SEARCH_PAGE_SIZE );
        searchSource.size(SearchConstant.SEARCH_PAGE_SIZE);

        /*
         * agg: {
         *   所有 sku 商品的 attr(nested)
         *   所有 sku 商品的品牌
         *   所有 sku 商品的分类(如果已经选择分类就不用选择)
         * }
         * */
        // 聚合 sku 已有的 attr
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        // 需要根据 attrId 再次聚合得出对应的 attrName 和 attrValue
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValuesAgg").field("attrs.attrValue"));
        attrAgg.subAggregation(attrIdAgg);
        searchSource.aggregation(attrAgg);

        System.out.println(searchSource.toString());
        return searchRequest.source(searchSource);
    }
}
