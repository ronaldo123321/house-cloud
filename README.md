## 项目说明 ##
     本项目是模拟房地产开发平台，主要功能包括用户系统管理，房产售卖平台管理，博客，留言等服务。采用Spring Cloud全家桶技术栈，项目按微服务进行模块划分，采用token+jwt进行无状态统一权限认证，实现了异常和日志的统一管理。
     核心框架：springcloud Greenwich.SR1全家桶
     安全框架：token + jwt
     持久层框架：mybatis + mysql + druid
     日志管理：log4j2 + ELK分布式日志管理平台
     分布式缓存： redis
     前端：freemarker
     其他服务：邮箱服务等
## 平台目录结构简要说明 ##

├─house-cloud----------------------------根目录
│  │
│  ├─api-gateway---------------------------网关服务
│  │
│  ├─comment-service-----------------------博客服务
│  │
│  ├─eureka-service-------------------------微服务注册中心
│  │
│  ├─house-service--------------------------房产服务
│  │
│  ├─hystrixboard--------------------------断路器监控服务
│  │
│  ├─sql-------------------------sql脚本文件，分三个库
│  │  
│  │—user-service-------------------------用户服务  
│  │  
│  │—zipkin--------------------------------分布式链路追踪服务  

## 其他说明 ##
 1.restTemplate本身是基于httpclient,本项目对restTemplate进行二次封装，使其既支持直连调用，又支持以loadbalance方式进行调用。
 2.本项目采用jwt+token实现了登陆认证，鉴权，具备安全，自包含和检验发布者的特点。当用户发送带有用户名和密码的post给server端时，验证通过后生成jwt并将用户信息放入jwt中，然后将jwt放入cookie中，当前端浏览器发送带有jwt cookie的请求过来后，server验证签名后从jwt中获取用户信息，然后返回响应的正确/错误信息。
 3.热门推荐房产的实现：借助redis的zset的数据结构，用户在前端页面每点击一次房产详情，对应的房产id就增加一分，用reverseRange可以去除倒序后的数据，核心实现代码：
 redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY,""+id,1.0D);redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY,0,-11);
 Set<String> idSet = 
redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, size - 
1);
List<Long> ids = idSet.stream().map(b -> 
Long.parseLong(b)).collect(Collectors.toList());
4.解决跨域问题：
 @Override  public void addCorsMappings(CorsRegistry registry) {    super.addCorsMappings(registry);   
 registry.addMapping("/**")           
 .allowedOrigins("*")   //放行所有的域            
 .allowCredentials(true)  //是否允许发送cookie信息            .allowedMethods("GET","POST","PUT","DELETE")//放行哪些请求            
 .allowedHeaders("*")//用于预检请求            .exposedHeaders("Header1","Header2");//暴露哪些头部信息  
 }
 5.微服务数据拆分与数据最终一致性的实现
     每个服务使用独立的数据库，避免某个服务的数量量过于庞大，减轻对单一服务的过度依赖。
     微服务不适用分布式事务，可以通过可靠事件模式（通过mq实现）或者补偿模式来实现，补偿模式支持事务回滚，可以采用补偿子事务来完成。
 
 6.guava工具类的使用举例
 
 List<House> lists = Lists.newArrayList();

Splitter.on(",").splitToList(properties);

Splitter.on(",").join(properties);

public static final HashFunction FUNCTION = Hashing.md5();

public static final ImmutableMap<Object,RestCode> MAP = ImmutableMap.<Object,RestCode>builder()            .put(1,RestCode.WRONG_PAGE)         .put(2,RestCode.UNKNOW_ERROR)
 .build();

Strings.isNullOrEmpty(json)


File newFile = new File("a.txt");
Files.write(file.getBytes(),newFile);


Objects.equal(0L, house.getUserId())



 7.logbook的使用
 采用logbook可以自动输出http请求和响应日子，可定义输出日志级别，输出日志类型，引入方式可参考house-service服务。