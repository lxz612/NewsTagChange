### NewsTagChange

拖拽重排GridView的Demo

#### 简介

用过网易新闻的同学可能都知道，Net易新闻TAG是可以按自己喜好增添排序的。如下图：

<div style="width: 200px;height:532px;background: #555;border-radius: 40px;padding: 50px 10px; box-sizing: content-box;box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.1), 0 8px 24px 0 rgba(0, 0, 0, 0.1), 0 0 0 1px rgba(0, 0, 0, 0.03), inset 0 -2px 2px 2px rgba(0, 0, 0, 0.05);">
		<img src="image/NetEase.PNG" width ="200" height="355" alt="网易新闻" align=center />
</div>

而这个项目就是自定义GridView来实现新闻栏目拖拽重排效果。项目的初衷主要给Android新人展示一下一个自定义View的由简单到复杂的迭代流程，所以我为项目分版本，具体看下面版本说明，便于由浅到深的学习思考。

#### 版本说明

1. v1.0 基本实现拖拽重排效果，但没有加入动画效果。效果图如下：

<div style="width: 200px;height:355px;background: #555;border-radius: 40px;padding: 50px 10px; box-sizing: content-box;box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.1), 0 8px 24px 0 rgba(0, 0, 0, 0.1), 0 0 0 1px rgba(0, 0, 0, 0.03), inset 0 -2px 2px 2px rgba(0, 0, 0, 0.05);">
		<img src="image/v1.1.gif" width ="200" height="355" alt="gif效果图" align=center />
</div>

2.  v2.0 加入了重排动画和放置动画。效果图如下：

<div style="width: 200px;height:355px;background: #555;border-radius: 40px;padding: 50px 10px; box-sizing: content-box;box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.1), 0 8px 24px 0 rgba(0, 0, 0, 0.1), 0 0 0 1px rgba(0, 0, 0, 0.03), inset 0 -2px 2px 2px rgba(0, 0, 0, 0.05);">
		<img src="image/v2.0.gif" width ="200" height="355" alt="gif效果图" align=center />
</div>

#### 未完待续...
