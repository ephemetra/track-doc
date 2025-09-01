# Track Doc

**Track Doc** 是一个 Java 注解处理器框架，用于自动生成业务埋点的 Excel 文档，方便产品、研发和测试团队管理和审查业务事件。

---

## 概述

Track Doc 通过自定义注解标记业务方法或事件，编译时自动生成 Excel 文档，记录事件名称、描述和方法信息，避免人工整理埋点表格，提高效率。

核心模块：

- `track-doc-annotation`：自定义注解定义模块
- `track-doc-processor`：注解处理器，实现 Excel 自动生成
- `track-doc`：集成模块，提供 Maven 依赖
- `track-doc-sample`：示例项目

---

## 快速开始

### 1. 引入 Maven 依赖

```xml
<dependency>
    <groupId>io.github.ephemetra</groupId>
    <artifactId>track-doc</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 注解示例
```java
import io.github.ephemetra.annotation.EventField;
import io.github.ephemetra.annotation.EventTrack;

import java.time.LocalDateTime;

@EventTrack(name = "user_login", desc = "用户登陆", trigger = "小程序认证登陆")
public class UserLoginSample {

    @EventField(desc = "用户名")
    private String username;

    @EventField(desc = "登陆时间")
    private LocalDateTime loginTime;
}
```

---

## 输出说明
- 默认输出路径：`target/classes/EventTrack_yyyyMMddHHmmss.xlsx`
- Excel 文件名包含时间戳，避免覆盖历史记录。
- 列说明：

| 列名   | 来源                | 说明    |
| ---- | ----------------- | ----- |
| 事件名称 | `@EventTrack.name` | 事件标识名称 |
| 事件描述 | `@EventTrack.desc` | 事件描述  |
| 触发场景 | `@EventTrack.trigger` | 事件触发场景 |
| 字段名称 | `@EventField.name` | 事件字段名 |
| 字段描述 | `@EventField.desc` | 事件字段描述  |
| 字段类型 | 类字段类型     | Java 类型 |

---

## 注意事项
- 框架当前只在编译阶段生成 Excel，请确保相关埋点类在编译路径下。
- 埋点文档默认会生成在 target/classes，可根据需要修改生成路径（未来版本可扩展）。
- Java 1.8+ 支持。