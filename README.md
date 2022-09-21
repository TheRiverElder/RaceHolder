# 前言
目前还在测试核心功能，没有进行持久化。

# 一些名词
**raceIndex** 赛事编号，现在以数字编号来确定一个赛事，之后可能会改为字符串

# 指令
## manager
`/raceholder manager list`
列出所有赛事的编号。
返回：编号列表。

`/raceholder manager new`
添加新的赛事。
返回：新的赛事编号。

`/raceholder manager remove <raceIndex>`
移除赛事。
raceIndex：赛事编号。

## plan
`/raceholder plan <raceIndex> list`
列出赛程路径点。
raceIndex：赛事编号。
返回：路径点列表。

`/raceholder plan <raceIndex> add`
添加路径点到路径点列表末尾。
raceIndex：赛事编号。
返回：新的路径点。

`/raceholder plan <raceIndex> insert <waypointIndex>`
插入路径点到指定位置。
raceIndex：赛事编号。
waypointIndex：要插入到的索引。
返回：新的路径点。

`/raceholder plan <raceIndex> remove <waypointIndex>`
移除指定路径点。
raceIndex：赛事编号。
waypointIndex：要移除的路径点的索引。
返回：被移除的路径点。

## raceRuntime
`/raceholder raceRuntime <raceIndex> join`
加入赛事。
raceIndex：赛事编号。

`/raceholder raceRuntime <raceIndex> quit`
退出赛事。
raceIndex：赛事编号。

`/raceholder raceRuntime <raceIndex> ready`
在赛事中表示准备好。
raceIndex：赛事编号。

# 使用流程

```minecraft
（搭建部分）
/raceholder new
新建赛事：0

（跑到起点）
/raceholder plan 0 add
已添加路径点：0(1, 2, 3)

（跑到下一个路径点）
/raceholder plan 0 add
已添加路径点：1(4, 5, 6)

（跑到下一个终点）
/raceholder plan 0 add
已添加路径点：2(7, 8, 9)

（选手部分）
/raceholder raceRuntime 0 join
已加入赛事：0
/raceholder raceRuntime 0 ready
准备好！

（所有人都准备好后）
倒计时10秒
...
倒计时1秒
开始！

（比赛结束）
比赛结束，获胜者是RiverElder
```