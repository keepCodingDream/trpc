# trpc
基于zookeeper服务注册与发现;动态代理实现服务调用;Netty实现远程通信

## Quick start

1.首先Clone项目到本地 :

```
git clone git@github.com:keepCodingDream/trpc.git

```

2.在项目根目录执行

```
mvn clean install
```


3.在你需要的项目里引入Maven依赖

       <dependency>
            <groupId>com.tracy.trpc</groupId>
            <artifactId>trpc-core</artifactId>
            <version>${project.version}</version>
        </dependency>


4.在你需要暴露的服务接口加上@Consumer注解

```
@Consumer
public interface Demo {
    String sayHello(String name);
}

```

5.在接口的实现类上加上@Provider注解

```
@Provider
public class DemoImpl implements Demo, Serializable {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
```

6.最后在需要注入的地方注入接口即可使用

```
@RestController
public class TestController {

    @Resource
    private Demo demo;

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test(@RequestParam(value = "world") String world) {
        return demo.sayHello(world);
    }
}

```

