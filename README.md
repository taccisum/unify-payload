# Unify Payload

[![Build Status](https://www.travis-ci.com/taccisum/unify-payload.svg?branch=master)](https://www.travis-ci.com/taccisum/unify-payload)
[![codecov](https://codecov.io/gh/taccisum/unify-payload/branch/main/graph/badge.svg?token=AFRo23WYix)](https://codecov.io/gh/taccisum/unify-payload)

帮助你轻松地将 Api 返回值处理成统一格式。

## Getting Started

引入依赖

```xml
<dependency>
    <groupId>com.github.taccisum</groupId>
    <artifactId>unify-payload-starter</artifactId>
    <version>{latest.version}</version>
</dependency>
```

实现 `PayloadFormatter`，自定义转换逻辑

```java
@Configuration
public class PayloadConfiguration {
    @Bean
    public PayloadFormatter payloadFormatter() {
        return o -> {
            Map<String, Object> payload = new HashMap<>();
            payload.put("code", "1");
            payload.put("success", true);
            payload.put("data", o);
            return payload;
        };
    }
}
```

为 RestController 添加 `@Payload` 注解

```java
@Payload	// 为 controller 加上注解即可
@RestController
@RequestMapping("demo")
public class DemoController {
    @GetMapping
    public String index() {
        return "hello";
    }
}
```

访问相应 api，可以看到返回格式已经被修改了

```shell
$ curl http://localhost:8080/demo
{"code":"1","data":"hello","success":true}
```





