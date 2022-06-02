### 阿里云对象存储(OSS)

1. 开通阿里云对象存储功能，并拿到对应的 AccessKey & SecretKey(TODO：)

2. 引入依赖

   ```xml
   <!-- SpringCloud Alibaba OSS -->
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
       <version>2.2.0.RELEASE</version>
   </dependency>
   ```

3. 编写配置文件

   ```yaml
   spring:
       alicloud:
         access-key: LTAI5t9FXy6ywYqzsu2TK2Cy
         secret-key: scQEtNGG7zfq7pL3HS20XRL0YIDdiM
         oss:
           endpoint: oss-cn-shenzhen.aliyuncs.com
           bucketName: prover-mall
   ```

   bucket: 就是你存放数据的`容器`

4. 使用 [**Web 前端通过 Policy 直传**]，定义对应的接口，可以让前端获取 Poclicy

   ```java
   @RestController
   @RequestMapping("/admin/thirdparty/oss")
   public class OssController {
   
       @Resource
       private OSSClient ossClient;
   
       /**
        * bucket 名称
        */
       @Value("${spring.cloud.alicloud.oss.bucketName}")
       private String bucketName;
       @Value("${spring.cloud.alicloud.oss.endpoint}")
       private String endPoint;
   
       @GetMapping("/policy")
       public R getOssPolicy(){
           // 填写Host地址，格式为https://bucketname.endpoint。
           String host = String.format("https://%s.%s", bucketName, endPoint);
           // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
           String dir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
           Map<String, String> respMap = null;
           try {
               long expireTime = 30;
               long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
               Date expiration = new Date(expireEndTime);
               PolicyConditions policyConds = new PolicyConditions();
               policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
               policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
   
               String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
               byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
               String encodedPolicy = BinaryUtil.toBase64String(binaryData);
               String postSignature = ossClient.calculatePostSignature(postPolicy);
   
               // web 直传需要的数据
               respMap = new LinkedHashMap<>();
               respMap.put("accessid", ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
               respMap.put("policy", encodedPolicy);
               respMap.put("signature", postSignature);
               respMap.put("dir", dir);
               respMap.put("host", host);
               respMap.put("expire", String.valueOf(expireEndTime / 1000));
   
           } catch (Exception e) {
               // Assert.fail(e.getMessage());
               System.out.println(e.getMessage());
           }
           return R.ok().put("data", respMap);
       }
   
   }
   ```

5. 前端向对应的 http://endpointName.bucketName 发起请求并携带刚刚获取的有关 Policy 的信息

   ```shell
   post http://endpointName.bucketName
   --请求体
   {
           policy: "",
           signature: "",
           key: "",
           ossaccessKeyId: "",
           dir: "",
           host: "",
   }
   --其中 key 为 `dir+对应的文件名`；剩下的属性都是通过刚刚后端返回的
   ```

   