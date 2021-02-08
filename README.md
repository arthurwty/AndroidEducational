# Android Educational Project
## Week 6 
### weekly goal
- 完成Minimum viable product
- 完成上周的游戏界面，可以不考虑颜色和其他设计但要有基本elements 比如：4*4 grid， score board。 目前可以将grid里的每一个tile初始成显示数字 0。
- 实现 操作grid的基本功能：getScore(), setScore(s), genRandom()...
- implement onSwipeTouchListener
- 判断操作是否可行的functions：boolean canSlideDown(), boolean isGameOver()
### todo-list
- [ ] View class
- [ ] Game Board class (View group?)
- [ ] swipe functions
- [ ] check functions
#### problems?
- 游戏界面的tile如果用textview感觉有点丑 是不是可以自定义View class 内涵 int value，String numVal， 还有颜色？然后在GameBoard class里操作 感觉这样可以写成GameBoard[][].
