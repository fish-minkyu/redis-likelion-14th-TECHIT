# Redis
- 2024.02.13 ~ 02.14

`2월 13일`
<details>
<summary><strong>Page</strong></summary>

- 공통사항

<div>application.yaml</div>
<hr>

- Spring Boot에서 Redis 사용하기
<div>RedisConfig</div>
<div>SimpleController</div>
<div>PersonDto</div>
<hr>

- Http와 Session
<div>build.gradle: 1. 서버 인스턴스 세션 공유 설정</div>
<div>RedisConfig: 2. 서버 인스턴스 세션 공유 설정</div>
<div>SessionController</div>
<hr>

- Redis Caching
<div>Item</div>
<div>ItemRepository</div>
<div>ItemDto</div>
<div>ItemService</div>
<div>ItemController</div>
<div>SlowDataQuery: 1초 지연 동작하는 DB</div>
</details>

1. `application.yaml`에 Redis DB을 등록 
2. Redis 콘솔창에서 Redis 자료형을 다뤄봄  
3. Spring Boot에서 Redis 사용하기
4. Http와 Session & Redis Caching 사용하기



`2월 14일`
<details>
<summary><strong>Page</strong></summary>

- @EnableCaching
<div>CacheConfig</div>

- @Cacheable, @CachePut
<div>ItemService</div>

- Sorted Set
<div>RedisConfig</div>
<div>CacheConfig</div>
<div>Item</div>
<div>ItemOrder</div>
<div>itemDto</div>
<div>ItemRepository</div>
<div>OrderRepository</div>
<div>SlowDataQuery</div>
<div>ItemService</div>
<div>ItemController</div>
</details>

1. `@EnableCaching`어노테이션으로 CacheConfig.java를 만들어 캐시 설정
2. `@Cacheable`으로 CashAside & `@CachePut`으로 WriteThrough
3. Sorted Set 구현

## 스팩

- Spring Boot 3.2.2
- Spring Web
- Lombok
- Spring Data Redis
- H2 Database

## Key Point

`02/13`
<details>
<summary><strong>02/13 - keyPoint</strong></summary>

1. application.yaml: Redis 연결
```yaml
spring:
  data:
    redis:
      host: <서버 주소>
      port: <포트 번호>
      username: <사용자 계정, 기본값 default>
      password: <사용자 비밀번호>
```

2. RedisTemplate & ValueOperations (String)  
[SimpleController.java](src/main/java/com/example/redis/SimpleController.java)
```java
@RestController
@RequiredArgsConstructor
public class SimpleController {
  // 문자열 Key와 문자열로 구성된 Value를 다루기 위한 RedisTemplate
  // (Java 기준 Value가 문자열이란 뜻이다.)
  private final StringRedisTemplate redisTemplate;

  // Put을 통해 Key-Value를 설정해서 Redis에 저장
  @PutMapping("string")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setString(
    @RequestParam("key")
    String key,
    @RequestParam("value")
    String value
  ) {
    // Redis에 String을 저장하고 싶다
    // ValueOperations: Redis 기준 문자열 작업을 위한 클래스
    ValueOperations<String, String> operations
      = redisTemplate.opsForValue();
    // SET key value
    operations.set(key, value);

//    // List를 위한 클래스
//    ListOperations<String, String> listOperations
//      = redisTemplate.opsForList();
//    listOperations.leftPush(key, value);
//    listOperations.leftPop(key);

//    // Set을 위한 클래스
//    SetOperations<String, String> setOperations
//            = redisTemplate.opsForSet();
//    setOperations.add(key, value);
  }

  // Get을 통해서 데이터 회수
  @GetMapping("string")
  public String getString(
    @RequestParam("key")
    String key
  ) {
    ValueOperations<String, String> operations
      = redisTemplate.opsForValue();
    // GET key
    String value = operations.get(key); // nullable이다.
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return value;

        /* // Sets 데이터 회수
        SetOperations<String, String> operations
                = redisTemplate.opsForSet();
        return operations.members(key);
        */
  }
}
```

3. Configuration (PersonDto) & RedisTemplate & ValueOperations (PersonDto)  
[RedisConfig.java](src/main/java/com/example/redis/config/RedisConfig.java)
```java
@Configuration
public class RedisConfig {

  // PersonDto
  @Bean
  public RedisTemplate<String, PersonDto> personRedisTemplate(
    // RedisConnectionFactory: Redis와 연결해주는 객체
    RedisConnectionFactory connectionFactory
  ) {
    RedisTemplate<String, PersonDto> template = new RedisTemplate<>();
    // 연결을 어떻게 받아올 것인지 설정
    template.setConnectionFactory(connectionFactory);
    // 주어진 데이터의 직렬화 방식을 결정한다.
    // Redis의 Value은 결국 문자열의 형식이니까,
    // 주어진 데이터(DTO)를 어떻게 문자열로 바꿀 것인지를 정의
    // 미리 만들어진 JSON 변환기를 설정 (Jackson 라이브러리에게 객체를 직렬화하는 방식을 전달)
//    template.setDefaultSerializer(RedisSerializer.json());
    // 미리 만들어진 String 변환기를 설정
    template.setKeySerializer(RedisSerializer.string());
    // 미리 만들어진 JSON 변환기를 설정
    template.setValueSerializer(RedisSerializer.json());

    return template;
  }
}
```
[SimpleController.java](src/main/java/com/example/redis/SimpleController.java)
```java
@RestController
@RequiredArgsConstructor
public class SimpleController {
  // 커스텀한 Configuration한 Bean 주입
  private final RedisTemplate<String, PersonDto> personRedisTemplate;

  @PutMapping("/person")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setPerson(
    @RequestParam("name")
    String name,
    @RequestBody
    PersonDto dto
  ) {
    ValueOperations<String, PersonDto> operations
      = personRedisTemplate.opsForValue();
    operations.set(name, dto);
  }

  @GetMapping("/person")
  public PersonDto getPerson(
    @RequestParam("name")
    String name
  ) {
    ValueOperations<String, PersonDto> operations
      = personRedisTemplate.opsForValue();
    PersonDto value = operations.get(name);
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    return value;
  }
}
```
4. 분산환경에서 Redis를 사용해 HttpSession 공유 설정
build.gradle & RedisConfig & SessionController  
[build.gradle](build.gradle)
```yaml
	// 1. 서버 인스턴스 세션 공유 설정
	implementation 'org.springframework.session:spring-session-data-redis'
```
[RedisConfig.java](src/main/java/com/example/redis/config/RedisConfig.java)
```java
@Configuration
// @EnableRedisHttpSession
// : 2. 서버 인스턴스 세션 공유 설정
// maxInactiveIntervalInSeconds
// : 세션을 유지할 시간 설정
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10)
public class RedisConfig {
  // ...
}
```
[SessionController.java](src/main/java/com/example/redis/SessionController.java)
```java
// 인증 외 상황(장바구니 등)에서 세션 정보를 저장하기 위한 Controller
@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {
  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setSession(
    @RequestParam("key") String key,
    @RequestParam("value") String value,
    // Key - Value로 이뤄져 있음
    HttpSession session
  ) {
    session.setAttribute(key, value);
  }

  @GetMapping
  public String getSession(
    @RequestParam("key") String key,
    HttpSession session
  ) {
    // setAttribute를 Object형으로 저장하였기에 Object로 받아준다.
    Object value = session.getAttribute(key);
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    if (!(value instanceof String))
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

    return value.toString();
  }
}
```
5. Redis Caching  
RedisConfig & Item & ItemRepository & ItemDto & SlowDataQuery & ItemService & ItemController  
[RedisConfig.java](src/main/java/com/example/redis/config/RedisConfig.java)
```java
@Configuration
// @EnableRedisHttpSession
// : 2. 서버 인스턴스 세션 공유 설정
// maxInactiveIntervalInSeconds
// : 세션을 유지할 시간 설정
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10)
public class RedisConfig {
  // ItemDto
  @Bean
  public RedisTemplate<Long, ItemDto> cacheRedisTemplate(
    RedisConnectionFactory connectionFactory
  ) {
    RedisTemplate<Long, ItemDto> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setDefaultSerializer(RedisSerializer.json());
    return template;
  }
}
```
[ItemService.java](src/main/java/com/example/redis/ItemService.java)
```java
@Slf4j
@Service
@RequiredArgsConstructor
// SlowDataQuery를 사용하는 ItemService
public class ItemService {
  private final SlowDataQuery repository;

  // @Resource로 DI 해준다.
  // RedisTemplate이 가지고 있는 ValueOperations를 바로 받아올 수 있다.
  @Resource(name = "cacheRedisTemplate")
  private ValueOperations<Long, ItemDto> cacheOps;
  

  public List<ItemDto> readAll() {
    return repository.findAll()
      .stream()
      .map(ItemDto::fromEntity)
      .toList();
  }
  
  public ItemDto readOne(Long id) {
    // Cache Aside를 RedisTemplate을 활용해 직접 구현해 보자.
    // 1. cacheOps에서 ItemDto를 찾아본다.
    // GET id
    ItemDto found = cacheOps.get(id);
    // 2. null일 경우 데이터베이스에서 조회한다.
    if (found == null) {
      // 2-1. 없으면 404
      found = repository.findById(id)
        .map(ItemDto::fromEntity)
        .orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      // 2-2. 있으면 캐시에 저장
      // Duration.ofSeconds(): 3번째 인자로 만료 시간 설정 가능
      cacheOps.set(id, found, Duration.ofSeconds(10));
    }

    // 3. 최종적으로 데이터를 변환한다.
    return found;
  }
}
```
</details>

`02/14`
<details>
<summary><strong>02/14 - keyPoint</strong></summary>

1. @EnableCaching
[CacheConfig]()
```java
@Configuration
// @EnableCaching
// : 캐시를 어노테이션을 바탕으로 만들 수 있게 해주는 기능
// (캐시를 어떻게 다룰지 결정하는 cacheManager 필요)
@EnableCaching
public class CacheConfig {
  @Bean
  public RedisCacheManager cacheManager(
    // RedisConnectionFactory
    // : 어떤 식으로 Redis와 연결할지 구성되어 있다.
    RedisConnectionFactory redisConnectionFactory
  ) {
    // RedisCacheConfiguration
    // : 캐시를 어떤 식으로 구성하고 싶은지에 대한 설정
    RedisCacheConfiguration configuration = RedisCacheConfiguration
      .defaultCacheConfig()
      // null을 캐싱할 것인가 말 것인가?
      .disableCachingNullValues()
      // Time To Live(Ttl): 만료 시간
      .entryTtl(Duration.ofSeconds(60))
      // Key 접두사 설정 (객체를 구분하기 위해 필요)
      .computePrefixWith(CacheKeyPrefix.simple())
      // Value 직렬화 / 역직렬화 방법
      .serializeValuesWith(
        SerializationPair.fromSerializer(RedisSerializer.json())
      );

    // Cacheable의 cacheName을 바탕으로 적용되는 규칙을 바꿔보자
    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
    RedisCacheConfiguration itemAllConfig = RedisCacheConfiguration
      .defaultCacheConfig()
      .disableCachingNullValues()
      .entryTtl(Duration.ofSeconds(10))
      .serializeValuesWith(
        SerializationPair.fromSerializer(RedisSerializer.java())
      );
    // 이름이 "itemAllCache"이면 itemAllConfig 설정 적용
    configMap.put("itemAllCache", itemAllConfig);

    // 실제 매니저를 등록하는 과정
    return RedisCacheManager
      // Connection 전달
      .builder(redisConnectionFactory)
      // 위에서 만든 설정을 기본값으로 설정
      .cacheDefaults(configuration)
      // 캐시 이름에 따라 설정을 따로 적용할 수 있다.
      // Map으로 put하는 방법 외, cacheName과 설정 객체를 넣어서 적용시킬 수 있다.
//      .withCacheConfiguration("itemAllCache", itemAllConfig)
      .withInitialCacheConfigurations(configMap)
      .build();
  }
}
```

2. @Cacheable & @CachePut
[itemService.java]()
```java
  // @Cacheable
  // : 캐시에서 데이터를 찾으면 메서드 자체를 호출하지 않는다.
  // cacheName: 캐시 규칙을 지정하기 위한 이름
  // Key: 캐시를 저장할 때, 개별 데이터를 구분하기 위한 값
  // root : 해당 메소드를 가르킴
  // methodName: 메소드 명
  @Cacheable(cacheNames = "itemAllCache", key = "#root.methodName")
  public List<ItemDto> readAll() {
    return repository.findAll()
      .stream()
      .map(ItemDto::fromEntity)
      .toList();
  }

  // args[0]: 매개변수 중 첫번째
  @Cacheable(cacheNames = "itemCache", key = "#root.args[0]")
  public ItemDto readOne(Long id) {
    log.info("cacheable readOne");
    return repository.findById(id)
      .map(ItemDto::fromEntity)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
```

3. Sorted Set  
RedisConfig & CacheConfig & Item & ItemOrder & ItemDto & ItemRepository & OrderRepository  
SlowDataQuery & ItemService & ItemController

[itemService]()
```java
  // 구매 메소드 (주문 이력 생성)
  // + 주문 이력을 Sorted Set으로 Redis에 저장
  public void purchase(Long id) {
    ItemDto item = ItemDto.fromEntity(repository.purchase(id));
    // Sorted Set에 추가
    rankOps.incrementScore("soldRanks", item, 1);
  }

  public List<ItemDto> getMostSold() {
    // LinkedHashSet으로 반환이 된다.
    // LinkedHashSet: 순서가 존재하는 집합
    Set<ItemDto> ranks = rankOps.reverseRange("soldRanks", 0, 9);
    // null 처리
    if (ranks == null) return Collections.emptyList();

    log.info(String.valueOf(ranks.getClass()));

    // Stream 방식
//    return ranks.stream().toList();
    return new LinkedList<>(ranks);
  }
```
</details>

## GitHub
[강사님 GitHub](https://github.com/edujeeho0/likelion-backend-8-redis)