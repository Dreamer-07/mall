package pers.prover.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pers.prover.mall.common.utils.PageUtils;
import pers.prover.mall.common.utils.Query;

import pers.prover.mall.product.dao.CategoryDao;
import pers.prover.mall.product.entity.CategoryEntity;
import pers.prover.mall.product.service.CategoryBrandRelationService;
import pers.prover.mall.product.service.CategoryService;
import pers.prover.mall.product.vo.api.CategoryLevel2RespVo;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private final String delLockLuaScript = "if redis.call(\"get\",KEYS[1]) == ARGV[1] \n" +
            "then\n" +
            "\treturn redis.call(\"del\",KEYS[1])\n" +
            "else\n" +
            "    return 0\n" +
            "end;";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> treeList() {
        List<CategoryEntity> categoryEntityList = this.list();
        return covertTreeList(categoryEntityList, 0);
    }

    @Override
    public void removeByIds(Long[] ids) {
        // TODO 判断数据是否被引用
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<Long> getCatelogPath(Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);
        List<Long> categoryPath = new ArrayList<>();
        getCategoryParentPath(categoryEntity, categoryPath);
        return categoryPath;
    }

    /**
     * {@code @CacheEvict} 注解的使用：方法执行后删除指定的缓存
     *   - cacheNames：指定缓存前缀
     *   - key：要删除的缓存的 key
     *   - allEntries：一般不和 key 一起使用，表示请求 cacheNames 下的所有缓存
     * @param category
     */
    @CacheEvict(cacheNames = "product:category", allEntries = true)
    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
        if (!StringUtils.isBlank(category.getName())) {
            categoryBrandRelationService.updateCategoryName(category.getCatId(), category.getName());
        }
        this.updateById(category);
    }

    @Override
    public String getCatelogPathStr(Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);
        List<String> categoryPath = new ArrayList<>();
        getCategoryParentPathStr(categoryEntity, categoryPath);
        return String.join("/", categoryPath);
    }

    @Override
    public String getCatelogName(Long catalogId) {
        LambdaQueryWrapper<CategoryEntity> selectLqw = new LambdaQueryWrapper<CategoryEntity>()
                .select(CategoryEntity::getName)
                .eq(CategoryEntity::getCatId, catalogId);
        return this.getOne(selectLqw).getName();
    }

    /**
     * {@code @Cacheable} 注解的使用：将方法返回结构保存到缓存中
     *   - value/cacheNames: 缓存的前缀就是
     *   - key: 缓存的 key，支持 SpEL 表达式 - https://blog.csdn.net/yangshangwei/article/details/78157834
     *   - sync: 本地锁，当缓存未命中时，避免缓存击穿
     * @param parentId
     * @return
     */
    @Cacheable(cacheNames = "product:category", key = "#root.methodName", sync = true)
    @Override
    public List<CategoryEntity> listByParentId(Long parentId) {
        LambdaQueryWrapper<CategoryEntity> selectLqw = new LambdaQueryWrapper<>();
        selectLqw.eq(CategoryEntity::getParentCid, parentId);
        return this.list(selectLqw);
    }

    @Override
    public Map<Long, List<CategoryLevel2RespVo>> listMapByParentId() {
        // 获取缓存是否缓存
        String value = redisTemplate.opsForValue().get("product:category:listMapByParentId");
        // 如果不存在就查找 db
        if (StringUtils.isBlank(value)) {
            // 获取可重入锁
            RLock lock = redissonClient.getLock("product:category:listMapByParentIdLock");
            // 上锁(阻塞式等待，如果没有抢到锁就会一直等待)
            lock.lock();
            Map<Long, List<CategoryLevel2RespVo>> result;
            try {
                // 查找数据库(TODO: 内部还有查找一次 redis，可以设置一个标识位，如果是查找 redis 的直接返回就好了)
                result = getLongListMapForDb();
                // 将数据保存到缓存中
                redisTemplate.opsForValue().set(
                        "product:category:listMapByParentId",
                        JSON.toJSONString(result),
                        300, TimeUnit.SECONDS
                );
            } finally {
                // 释放锁
                lock.unlock();
            }
            return result;

        }
        // 如果存在就转换未对象(存储的是 json 字符串)后返回
        return JSON.parseObject(value, new TypeReference<Map<Long, List<CategoryLevel2RespVo>>>() {
        });
    }

    public Map<Long, List<CategoryLevel2RespVo>> listMapByParentIdv1() {
        // 获取缓存是否缓存
        String value = redisTemplate.opsForValue().get("product:category:listMapByParentId");
        // 如果不存在就查找 db
        if (StringUtils.isBlank(value)) {
            // 加锁: setIfAbsent == setnx
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(
                    "product:category:listMapByParentIdLock",
                    String.valueOf(Thread.currentThread().getId()),
                    300, TimeUnit.SECONDS
            );
            // 加锁成功
            if (ifAbsent != null && ifAbsent) {
                Map<Long, List<CategoryLevel2RespVo>> result;
                try {
                    // 查找数据库(TODO: 内部还有查找一次 redis，可以设置一个标识位，如果是查找 redis 的直接返回就好了)
                    result = getLongListMapForDb();
                    // 将数据保存到缓存中
                    redisTemplate.opsForValue().set(
                            "product:category:listMapByParentId",
                            JSON.toJSONString(result),
                            300, TimeUnit.SECONDS
                    );
                } finally {
                    // 释放锁(通过执行 lua 脚本保证锁的检查和删除操作的原子性)
                    redisTemplate.execute(new DefaultRedisScript<Long>(delLockLuaScript, Long.class),
                            Collections.singletonList("product:category:listMapByParentIdLock"),
                            String.valueOf(Thread.currentThread().getId()));
                }
                return result;
            } else {
                try {
                    // 线程阻塞后再次查找(自旋锁)
                    Thread.sleep(200);
                    return listMapByParentId();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 如果存在就转换未对象(存储的是 json 字符串)后返回
        return JSON.parseObject(value, new TypeReference<Map<Long, List<CategoryLevel2RespVo>>>() {
        });
    }

    /**
     * 从数据库查找并返回
     *
     * @return
     */
    private Map<Long, List<CategoryLevel2RespVo>> getLongListMapForDb() {
        // 获取是否存在缓存数据
        String value = redisTemplate.opsForValue().get("product:category:listMapByParentId");
        // 如果不存在就查找 db
        if (!StringUtils.isBlank(value)) {
            return JSON.parseObject(value, new TypeReference<Map<Long, List<CategoryLevel2RespVo>>>() {});
        } else {
            System.out.println("查询数据库...");
            // 获取所有分类信息
            List<CategoryEntity> categoryEntities = this.list();

            // 获取所有的一级分类 id 信息
            return categoryEntities.stream()
                    .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                    .map(CategoryEntity::getCatId)
                    .collect(Collectors.toMap(catId -> catId, catId -> {
                        // 获取对应的二级分类
                        List<CategoryEntity> categoryLevel2List = covertTreeList(categoryEntities, catId);

                        // 将二级分类的结构进行转换
                        return categoryLevel2List.stream().map(categoryLevel2Entity -> {

                            CategoryLevel2RespVo categoryLevel2RespVo = new CategoryLevel2RespVo();
                            categoryLevel2RespVo.setId(categoryLevel2Entity.getCatId().toString());
                            categoryLevel2RespVo.setName(categoryLevel2Entity.getName());
                            categoryLevel2RespVo.setCatalog1Id(catId);

                            // 获取二级分类标识
                            Long catLevel2Id = categoryLevel2Entity.getCatId();

                            // 将三级分类进行转换
                            List<CategoryLevel2RespVo.CategoryLevel3RespVo> categoryLevel3RespVos = categoryLevel2Entity.getChildren().stream().map(categoryLevel3Entity -> {
                                CategoryLevel2RespVo.CategoryLevel3RespVo categoryLevel3RespVo = new CategoryLevel2RespVo.CategoryLevel3RespVo();
                                categoryLevel3RespVo.setCatalog2Id(catLevel2Id);
                                categoryLevel3RespVo.setId(categoryLevel3Entity.getCatId().toString());
                                categoryLevel3RespVo.setName(categoryLevel3Entity.getName());
                                return categoryLevel3RespVo;
                            }).collect(Collectors.toList());
                            categoryLevel2RespVo.setCatalog3List(categoryLevel3RespVos);

                            return categoryLevel2RespVo;
                        }).collect(Collectors.toList());
                    }));
        }
    }

    /**
     * 获取分类的父id路径
     *
     * @param categoryEntity
     * @param categoryPath
     */
    private void getCategoryParentPath(CategoryEntity categoryEntity, List<Long> categoryPath) {
        if (categoryEntity.getParentCid() != 0) {
            getCategoryParentPath(this.getById(categoryEntity.getParentCid()), categoryPath);
        }
        categoryPath.add(categoryEntity.getCatId());
    }

    /**
     * 获取分类的父分类的名
     *
     * @param categoryEntity
     */
    private void getCategoryParentPathStr(CategoryEntity categoryEntity, List<String> categoryPath) {
        if (categoryEntity.getParentCid() != 0) {
            getCategoryParentPathStr(this.getById(categoryEntity.getParentCid()), categoryPath);
        }
        categoryPath.add(categoryEntity.getName());
    }

    private List<CategoryEntity> covertTreeList(List<CategoryEntity> categoryEntityList, long parentId) {
        return categoryEntityList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == parentId)
                .peek(categoryEntity -> {
                    categoryEntity.setChildren(covertTreeList(categoryEntityList, categoryEntity.getCatId()));
                })
                .sorted(Comparator.comparingInt(c -> (c.getSort() == null ? 0 : c.getSort())))
                .collect(Collectors.toList());
    }

}