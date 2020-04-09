# SpringBoot2.0 开发脚手架

## 适用
+ 需要独立部署的服务。

## 功能
 1. 登录认证机制
 2. 配置刷新。
 
## 环境
+ `JDK1.8`
+ `Redis3.5+`
+ `Mysql 5.7+`
+ `Oracle 12c+`

## 框架
+ `Spring boot 2.2.2`
+ `Mybatis Plus 3.1.2`
+ `Lombok 1.18.8`

## 更换项目名称
1. 使用`archetype`构建项目：

该脚手架已打包成`archetype`并上传到私服，在本地执行以下命令:
```
mvn archetype:generate -DarchetypeGroupId=com.xdja -DarchetypeArtifactId=springboot-mybatisPlus-archetype -DarchetypeVersion=1.0.0
```
根据提示填写自己项目的`groupId`、`archetypeId`、`version`、`packageName`等信息，就可在本地生成项目。

以下业务的讲解默认ltap为对应项目名称。
## 模块结构
```
.
├── ltap-web
│   └── src/main/resource/static
├── ltap-domain
├── ltap-common
├── ltap-api
├── ltap-service
├── ltap-dao
├── └── src/main/java/com.xdja.dao.mapper
└── └── src/main/java/resource/mapper
```
模块解释：
+ `ltap-web`: 存放`http`启动相关配置，以及对终端和后台的`controller`。
+ `ltap-web/src/main/resource/static`: 存放前端页面。
+ `ltap-domain`: 存放`java`实体：数据库实体、查询对象，业务类。
+ `ltap-common`: 存放公用的工具类。
+ `ltap-api`: 存放`service`接口。
+ `ltap-service`: 存放`service`实现类。
+ `ltap-dao`: 存放操作`dao`的接口以及实现类。
+ `ltap-dao/src/main/java/com.xdja.dao.mapper`存放`mybatis`的`mapper`。
+ `ltap-dao/src/main/java/resource/mapper`存放`mybatis`的`xml`。

## 启动步骤
1. 修改数据源相关配置
     + 根据项目使用数据库情况，修改`application.properties`配置文件中`spring.datasource`开头的配置项。
     + 删除`ltap-dao/pom.xml`中其他数据库依赖。
     + 修改`MybatisConfigure`类中数据库分页插件方言。
     
2. 登录认证配置
     + 默认使用`redis`存储会话信息，使用时需修改`application.properties`配置文件中`redis`相关配置项。若使用内存存储回话信息，请修改`WebConfigure#TokenFactory`的`token`管理方式为`MemoryTokenOperator`。
     + 修改`LoginController`登录逻辑，`AdminFilter`认证逻辑。
     
3. 运行`ApplicationStart.java`启动项目。

## 运行&部署&升级版本
#### 1.运行

运行：`com.xdja.web.ApplicationStart#main`方法。（修改代码不用`clean install`）
##### 1.1 配置文件
默认配置文件从`classpath: application.properties`、`file:/home/xdja/conf/ltap/application.properties(windows环境下 /home/...以当前磁盘为根目录)`等位置加载`spring`上下文的配置，
可以根据不同项目要求调整或更改配置文件位置。

多个配置文件路径下若存在配置相同的配置项，以后者为准。

`spring`配置文件支持动态刷新，即修改上述文件后程序立马生效，但是需在要刷新的`bean`上加`@RefreshScope`注解。如：
```
@Service
@RefreshScope
public class TestServiceImpl {

    @Value("${switch.option}")
    private String switchOption;
}
```
或
```
@Configuration
public class TestConfiguration {

    @Bean
    @RefreshScope
    public Object getBean(){
        return new Object();
    }
}
```

##### 1.2 日志文件
TODO

#### 2.部署

部署方式为war包，在`ltap-web`模块下执行`maven`的`package`命令即可打包成`war`包。

#### 3.升级版本
推荐以下两种方式进行版本号的变更。

##### 3.1 maven-release插件
本操作需正确配置父`pom`下的`scm`标签下对应的`git`路径。

操作命令和说明如下：
+ 将`SNAPSHOT`版本号改成正式版本号、创建`tag`并上传到`git`(-B参数表示不需要手动指定版本号)：`mvn -B release:prepare`。
+ 回滚刚生成的正式版本号(不会删除线上和本地刚打的`tag`): `mvn release:rollback`。
+ 删除生成的`release`临时文件: `mvn release:clean`。
+ 更改成下一个`SNAPSHOT`版本号: `mvn release:perform`。

发布新版本执行：`mvn -B release:prepare release:perform`即可。
> 常见问题：
>1. `mvn compile` 无效标识: 一般是`jdk`版本不对，或者`java_home`路径不对。
>2. `Cannot prepare the release because you have local modifications`: 本地有修改的文件未commit。
>3. `tag 'x.x.x' already exists`: 重复打相同tag，需删除本地和远程tag。

##### 3.2 maven-version插件
一次性更改父`pom.xml`和所有子`pom`版本号：`mvn versions:set -DgenerateBackupPoms=false -DnewVersion=x.x.x`。
> 该操作仅仅是修改本地`pom`文件的版本号，并且也不会打`tag`，需手动`commit`和`push`。