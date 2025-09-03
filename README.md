# Track Doc

**Track Doc** 是一个 Java 注解处理器框架，用于自动生成业务埋点的 Excel 文档，方便产品、研发和测试团队管理和审查业务事件。

---

## 概述

Track Doc 通过自定义注解标记业务方法或事件，编译时自动生成 Excel 文档，记录事件名称、描述和字段信息，避免人工整理埋点表格，提高效率。

- **自动扫描**：通过注解收集埋点事件信息。
- **Excel 文档**：生成带格式的 Excel，表头对齐、边框、列宽优化。
- **无侵入性**：无需运行额外代码，编译即可生成。
- **便于审查**：产出表格清晰，产品 & 测试可直接使用

---

## 快速开始

### 1. 引入 Maven 依赖

```xml
<dependency>
    <groupId>io.github.ephemetra</groupId>
    <artifactId>track-doc</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope>
</dependency>
```

### 2. 注解示例

```java
import annotation.io.github.EventField;
import annotation.io.github.EventTrack;

import java.time.LocalDateTime;

@EventTrack(name = "user_login", desc = "用户登陆", trigger = "小程序认证登陆")
public class UserLoginSample {

    @EventField(desc = "用户名")
    private String username;

    @EventField(desc = "登陆时间")
    private LocalDateTime loginTime;
}
```

### 3. 编译生成文档

运行编译命令:
```bash
mvn clean compile
```
生成的 Excel 文件默认输出到：
```bash
target/classes/EventTrack_yyyyMMddHHmmss.xlsx
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
- 环境要求：Java 1.8+  Maven3.6+。