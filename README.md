# ItsTools

## 插件简介

**ItsTools** 是一个整合了许多实用功能的，基于TabooLib VI的插件

绝对支持**1.19.x-1.20.x**，理论支持1.8-1.17.x

插件主命令:
- /itstools, /it 常规功能命令 (itstools.access)

- /itsoperation, /io 有关生物操作命令 (itstools.access)

## 详细介绍

### 资源包管理模块

#### 资源包发送管理

支持定义无限制个资源包

每个资源包均可以设置成功加载、拒绝加载、加载失败、接受下载、移除时的Kether动作

还可以为每个世界设置自动加载材质包

**命令**:
- /itstools rp get <id> 获取材质包 (权限由材质包配置确定)
- /itstools rp send <player> <id> 向玩家发送材质包 (itstools.command.resourcepack.send)

#### 资源包上传管理

让你在服务器里就能更新材质包

目前支持阿里云oss和腾讯云cos (详细配置见文末)

默认读取插件目录下packs文件夹中的文件

支持直接上传ItemsAdder生成的材质包

命令:
- /itstools rp upload oss <pack> 上传至阿里云存储桶 (itstools.command.resourcepack.upload)
- /itstools rp upload oss <pack> 上传至腾讯云存储桶 (itstools.command.resourcepack.upload)

PlaceholderAPI变量
- %itstools_rp_<id>% 获取玩家是否使用这个材质包
- %itstools_rp% 获取玩家当前应用的材质包

### 快捷键模块

包含了大部分常用的玩家快捷操作

使用Kether动作，可配置性极高

- 切换副手(F)类：Shift + F, 抬头F, 低头F
- 丢弃物品(Q)类：Shift + Q, 抬头Q, 低头Q
- 互动玩家类: 点击玩家，潜行点击玩家 （支持变量&clicked获取点击玩家名）


### 插件联动模块

- 为AttributeSystem提供原版装备护甲值转换 (Beta)
- 为Sandalphon提供ItemsAdder物品源支持
- 为eco提供Zaphkiel物品源支持 （zaphkiel:id）
- 为RealisticSeasons提供可变色时间条 (支持`SimpleComponent`)

### 调试模块

#### 命令

- /itsoperation addpotion <potion> [args] 为生物添加药水效果 (itstools.command.addpotion)
```
[args]
-duration, -d: 时长
-amplifier, -a: 等级
--ambient: 信标效果
--p: 有粒子效果
--i: 有图标
```
- /itsoperation makemeleehostile [args] 为生物添加攻击ai (itstools.command.makemeleehostile)
```
[args]
-damage, -d: 伤害
-speed, -s: 速度
-target, -t: 目标 (Entityxxx)
-priority, -p: ai优先级
--f: 强制锁定目标
```
- /itsoperation removegoal <goal> 移除生物指定的动作ai (itstools.command.removegoal)
- /itsoperation removegoal <goal> 移除生物指定的目标ai (itstools.command.removetarget)
- /itsoperation getgoal 获取生物所有的动作ai (itstools.command.getgoal)
- /itsoperation gettarget 获取生物所有的目标ai (itstools.command.gettarget)
- /itsoperation togglegravity 切换生物是否受重力 (itstools.command.togglegravity)

#### 调试工具

使用/itstools getdebugitem (itstools.command.getdebugitem)获取调试工具

使用F键切换模式

- 功能一: 获取生物的UUID (GET_ENTITY_UUID)
- 功能二: 为生物设置寻路 (使用方法：先用NAVIGATE模式选定生物，再右键方块)
- 功能三: 为生物设置目标（使用方法：先用工具选定生物，再使用/itsoperation settarget右键其他生物）

### 其他特性

#### 命令
- /itstools reload 重载插件 (itstools.command.reload)
- /itstools forcechat <玩家> <消息> 强制玩家聊天 (itstools.command.forcechat)
- /itstools simplekether <args> 通过命令执行Kether动作 (itstools.command.simplekether)
```
<args>
-source: 动作文本
-namespace: 命名空间
-sender: 发送者
```

##### lore命令 (itstools.command.lore)
- /itstools lore append <文字> 为物品添加lore
- /itstools lore insert <行数> <文字> 为物品在指定行数插入lore
- /itstools lore pop [行数] 为物品删除一行lore

##### 发送成就提示(toast)命令 (itstools.command.sendtoast)
- /itstools sendtoast <玩家> <类型> <材质> <内容>

##### 发送地图画(图片)命令 (itstools.command.sendmap)
- /itstools sendmap file <玩家> <文件路径> 通过本地文件发送图片
- /itstools sendmap url <玩家> <url> 通过网络资源发送图片

##### 设置光源命令 (不保证可用) (itstools.command.light)
- /itstools light create <SKY/BLOCK/BOTH> <强度> 为方块创建光源
- /itstools light delete <SKY/BLOCK/BOTH> 为方块删除光源

#### ~~IP属地~~ (待修复)

从在线API获取并缓存到本地
该功能需要配合PlaceholderAPI使用

```
%itstools_ip_country%	国家
%itstools_ip_shortname%	国家缩写
%itstools_ip_province%	省份
%itstools_ip_city%	城市
%itstools_ip_area%	更精确的位置(不准确)
%itstools_ip_isp%	供应商
```

配合聊天插件显示效果
![](https://attachment.mcbbs.net/data/myattachment/forum/202212/21/165342bpwgw7wr5wwjrmvr.png)

#### 烟花苦力怕

将苦力怕爆炸替换为不破坏地形的烟花爆炸

烟花所有特效均随机生成

#### 混淆种子

#### 移除锋利粒子

#### 虚空生成器