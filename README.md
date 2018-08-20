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

6.在需要注入的地方注入接口即可使用

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
7.最后，也是最重要的，就是要加上服务配置

#####服务端配置

```
#zookeeper地址
trpc.zookeeper.address=127.0.0.1:2181
#服务名称
trpc.context.provider.name=demo
#服务版本号
trpc.context.provider.version=0.0.1
#服务端ip(可为空，为空时取本机ip)
trpc.context.provider.ip=
#服务端端口号
trpc.context.provider.port=9090
#扫描服务的基础包路径(包含@Provider 注解的类路径)
trpc.context.base.package=com.tracy.trpc.demo.base

```

#####客户端端配置

```
#zookeeper地址
trpc.zookeeper.address=127.0.0.1:2181
#服务名称
trpc.context.consumer.name=demo
#服务版本号
trpc.context.consumer.version=0.0.1
#扫描服务的基础包路径(包含@Consumer 注解的类路径)
trpc.context.base.package=com.tracy.trpc.common
```