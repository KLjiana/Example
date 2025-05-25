# 学习世界渲染
本文章主要介绍了关于Minecraft在渲染世界时所做的东西  
并且本文需要有对关于[渲染](https://docs.fabricmc.net/zh_cn/develop/rendering/basic-concepts)有一定的基础才能阅读  
笔者在写本篇时会较为口语化，而且会有一定的知识错误，多多包容  

## 使用mc版本与工具版本
IDE: IntelliJ IDEA 2024.3.5 (Community Edition)  
Minecraft: 1.20.1  
ModLoader: Forge 47.3.0  
Mapping: Parchment 2023.09.03  

## 关于LevelRenderer
我们将LevelRenderer的介绍分为三个部分：  
- 字段
- 方法
- 具体渲染过程

可按需跳转

### 字段
本篇会介绍关于LevelRenderer类当中的字段

#### LOGGER


