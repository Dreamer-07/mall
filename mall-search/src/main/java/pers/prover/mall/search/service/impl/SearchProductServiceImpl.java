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
 * @author ?????????????????????????????????????????????
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
        // ???????????? bulk ??????
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
            log.error("??????????????????????????????");
            return false;
        }
    }

    @Override
    public ProductSearchResultVo search(ProductSearchParamVo productSearchParamVo) {
        SearchResponse searchResponse;

        try {
            // ????????????????????? DSL ????????????
            SearchRequest searchRequest = buildSearchProductDSL(productSearchParamVo);
            // ??????
            searchResponse = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // System.out.println(searchResponse.toString());
        } catch (IOException e) {
            throw new RuntimeException("???????????????????????????????????????");
        }

        return buildSearchProductResult(searchResponse, productSearchParamVo);
    }

    private ProductSearchResultVo buildSearchProductResult(SearchResponse searchResponse, ProductSearchParamVo productSearchParamVo) {
        ProductSearchResultVo productSearchResultVo = new ProductSearchResultVo();
        // ???????????????
        SearchHits hits = searchResponse.getHits();
        // ????????????
        Aggregations aggregations = searchResponse.getAggregations();
        // ??????????????????
        productSearchResultVo.setPageNum(productSearchParamVo.getPageNum());
        productSearchResultVo.setTotalDataCount((int) hits.getTotalHits().value);
        productSearchResultVo.setTotalPages(productSearchResultVo.getTotalDataCount() + 1 % SearchConstant.SEARCH_PAGE_SIZE);
        // ??????????????????
        List<ProductMapping> productMappingList = new ArrayList<>();
        for (SearchHit searchHit : hits.getHits()) {
            ProductMapping productMapping = JSON.parseObject(searchHit.getSourceAsString(), ProductMapping.class);
            productMapping.setSkuTitle(searchHit.getHighlightFields().get("skuTitle").getFragments()[0].toString());
            productMappingList.add(productMapping);
        }
        productSearchResultVo.setProductMappingList(productMappingList);

        // ??????????????????
        // ??????????????????????????????
        ParsedNested parsedNested = aggregations.get("attrAgg");
        // ?????? attrId
        ParsedLongTerms attrIdAgg = parsedNested.getAggregations().get("attrIdAgg");
        List<ProductSearchResultVo.Attr> attrs = new ArrayList<>();
        List<ProductSearchResultVo.SearchParamVo> searchParamVoList = new ArrayList<>();
        // ??????????????????????????? attr
        List<Long> searchAttrIds = productSearchParamVo.getAttrs().stream().map(attr -> Long.valueOf(attr.split("_")[0])).collect(Collectors.toList());
        // ???????????? attrAgg ????????????????????????
        for (Terms.Bucket attrIdBucket : attrIdAgg.getBuckets()) {
            long attrId = attrIdBucket.getKeyAsNumber().longValue();
            // ????????????????????????
            ParsedStringTerms attrNameAgg = attrIdBucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            // ???????????????
            ParsedStringTerms attrValuesAgg = attrIdBucket.getAggregations().get("attrValuesAgg");
            List<String> attrValues = new ArrayList<>();
            for (Terms.Bucket attrValuesBucket : attrValuesAgg.getBuckets()) {
                attrValues.add(attrValuesBucket.getKeyAsString());
            }

            if (!searchAttrIds.contains(attrId)) {
                ProductSearchResultVo.Attr attr = new ProductSearchResultVo.Attr();
                // ?????????????????????????????????????????????????????????
                attr.setAttrId(attrId);
                attr.setAttrName(attrName);
                attr.setAttrValues(attrValues);
                attrs.add(attr);
            } else {
                // ???????????????????????????????????????????????????????????????????????????
                ProductSearchResultVo.SearchParamVo searchParamVo = new ProductSearchResultVo.SearchParamVo();
                searchParamVo.setParamId(attrId);
                searchParamVo.setParamName(attrName);
                searchParamVo.setParamValues(attrValues);
                searchParamVoList.add(searchParamVo);
            }
        }
        productSearchResultVo.setAttrList(attrs);

        // ???????????????????????????
        ParsedLongTerms catalogIdAgg = aggregations.get("catalogIdAgg");
        if (catalogIdAgg != null) {
            List<ProductSearchResultVo.CategoryVo> categoryVoList = new ArrayList<>();
            for (Terms.Bucket categoryIdBucket : catalogIdAgg.getBuckets()) {
                // ??????????????????
                ProductSearchResultVo.CategoryVo categoryVo = new ProductSearchResultVo.CategoryVo();
                categoryVo.setCatelogId(categoryIdBucket.getKeyAsNumber().longValue());
                // ???????????????
                ParsedStringTerms catalogNameAgg = categoryIdBucket.getAggregations().get("catalogNameAgg");
                String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
                categoryVo.setCatelogName(catalogName);
                categoryVoList.add(categoryVo);
            }
            productSearchResultVo.setCategoryVoList(categoryVoList);
        }

        // ???????????????????????????
        ParsedLongTerms brandAgg = aggregations.get("brandAgg");
        if (brandAgg != null) {
            List<ProductSearchResultVo.BrandVo> brandVoList = new ArrayList<>();
            for (Terms.Bucket brandAggBucket : brandAgg.getBuckets()) {
                // ??????????????????
                ProductSearchResultVo.BrandVo brandVo = new ProductSearchResultVo.BrandVo();
                brandVo.setBrandId(brandAggBucket.getKeyAsNumber().longValue());
                // ???????????????
                ParsedStringTerms brandNameAgg = brandAggBucket.getAggregations().get("brandNameAgg");
                String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
                brandVo.setBrandName(brandName);
                // ??????????????????
                ParsedStringTerms brandImgAgg = brandAggBucket.getAggregations().get("brandImgAgg");
                String brandImage = brandImgAgg.getBuckets().get(0).getKeyAsString();
                brandVo.setBrandImage(brandImage);

                brandVoList.add(brandVo);
            }
            productSearchResultVo.setBrandVoList(brandVoList);
        } else {
            R brandNamesResult = productFeignClient.brandNameList(productSearchParamVo.getBrandIds());
            ProductSearchResultVo.SearchParamVo searchParamVo = new ProductSearchResultVo.SearchParamVo();
            searchParamVo.setParamName("??????");
            searchParamVo.setParamValues(brandNamesResult.getData(new TypeReference<List<String>>(){}));
            searchParamVoList.add(searchParamVo);
        }

        productSearchResultVo.setSearchParamVoList(searchParamVoList);

        return productSearchResultVo;
    }

    /**
     * ????????????????????? DSL ????????????
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
         *       filter: req.catelog3Id, req.brandId???req.hasStock, req.priceRange, req.attrs(nested)
         *   }
         * }
         */
        // ???????????????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isBlank(productSearchParamVo.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", productSearchParamVo.getKeyword()));
            /*
             * ??????????????????????????????????????????????????????
             *  hightlight: {
             *    "fields": resp.sku.skuTitle
             *    "pre_tags": "<b style='color:red'>"
             *    "post_tags": "</b>"
             * }
             * */
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            // ???????????????????????????
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSource.highlighter(highlightBuilder);
        }
        // ????????????
        if (productSearchParamVo.getCatelog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", productSearchParamVo.getCatelog3Id()));
        } else {
            // ??????????????????????????????????????????????????????????????????
            TermsAggregationBuilder catelogAgg = AggregationBuilders.terms("catalogIdAgg").field("catalogId");
            catelogAgg.subAggregation(AggregationBuilders.terms("catalogNameAgg").field("catalogName"));
            searchSource.aggregation(catelogAgg);
        }
        // ????????????
        if (!CollectionUtils.isEmpty(productSearchParamVo.getBrandIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", productSearchParamVo.getBrandIds()));
        } else {
            // ??????????????????????????????????????????????????????????????????
            TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId");
            brandAgg.subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"));
            brandAgg.subAggregation(AggregationBuilders.terms("brandImgAgg").field("brandImg"));
            searchSource.aggregation(brandAgg);
        }
        // ?????????????????????
        if (productSearchParamVo.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", productSearchParamVo.getHasStock()));
        }
        // ??????????????????
        if (!StringUtils.isBlank(productSearchParamVo.getPriceRange())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            // ????????????????????????
            String[] priceRange = productSearchParamVo.getPriceRange().split("_");
            if (priceRange.length == 2) {
                // ?????????????????????????????????
                rangeQueryBuilder.from(priceRange[0]);
                rangeQueryBuilder.to(priceRange[1]);
            } else if (priceRange.length == 1) {
                // ????????????????????????
                if (productSearchParamVo.getPriceRange().startsWith("_")) {
                    // ??? _ ??????, ?????????????????????
                    rangeQueryBuilder.to(priceRange[0]);
                } else {
                    // ?????? _ ??????????????????????????????
                    rangeQueryBuilder.from(priceRange[0]);
                }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        /*
         * ????????????
         *   query.nested.path
         *   query.nested.query.bool.must[]
         * */
        if (!CollectionUtils.isEmpty(productSearchParamVo.getAttrs())) {
            BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            for (String attr : productSearchParamVo.getAttrs()) {
                // ???????????????????????? - ????????????_?????????[:?????????]
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
        // ???????????? - `????????????_????????????`
        if (!StringUtils.isBlank(productSearchParamVo.getSort())) {
            String[] sort = productSearchParamVo.getSort().split("_");
            FieldSortBuilder sortBuilder = SortBuilders.fieldSort(sort[0]).order(SortOrder.valueOf(sort[1].toUpperCase()));
            searchSource.sort(sortBuilder);
        }

        /*
         * form: ??????
         * */
        searchSource.from((productSearchParamVo.getPageNum() - 1) * SearchConstant.SEARCH_PAGE_SIZE );
        searchSource.size(SearchConstant.SEARCH_PAGE_SIZE);

        /*
         * agg: {
         *   ?????? sku ????????? attr(nested)
         *   ?????? sku ???????????????
         *   ?????? sku ???????????????(???????????????????????????????????????)
         * }
         * */
        // ?????? sku ????????? attr
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        // ???????????? attrId ??????????????????????????? attrName ??? attrValue
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValuesAgg").field("attrs.attrValue"));
        attrAgg.subAggregation(attrIdAgg);
        searchSource.aggregation(attrAgg);

        System.out.println(searchSource.toString());
        return searchRequest.source(searchSource);
    }
}
