Javascript docs

1. 基本语法与数据类型
	大小比较用 === 
	浮点数的大小比较：  1 / 3 === (1 - 2 / 3); // false

2. 不在任何函数内定义的变量就具有全局作用域. 
	用let替代var可以申明一个块级作用域的变量
	ES6后， 可以解构赋值

	var x, y;
	// 解构赋值:
	{x, y} = { name: '小明', x: 100, y: 200};
	// 语法错误: Uncaught SyntaxError: Unexpected token =

	JavaScript引擎把{开头的语句当作了块处理，于是=不再合法。解决方法是用小括号括起来：
	({x, y} = { name: '小明', x: 100, y: 200});

3. 高阶函数 与 闭包：MapReduce: Simplified Data Processing on Large Clusters  https://research.google/pubs/pub62/
	map
	reduce
	filter

		var r, arr = ['apple', 'strawberry', 'banana', 'pear', 'apple', 'orange', 'orange', 'strawberry'];
		r = arr.filter(function (element, index, self) {return self.indexOf(element) === index; });
	    去除重复元素依靠的是indexOf总是返回数组中第一个元素的位置，后续的重复元素位置与indexOf返回的位置不相等，因此被filter滤掉

	sort

	当一个函数返回了一个函数后，其内部的局部变量还被新函数引用，所以，闭包用起来简单，实现起来可不容易
	返回闭包时牢记的一点就是：返回函数不要引用任何循环变量，或者后续会发生变化的变量。
	一个立即执行的匿名函数可以把函数体拆开，一般这么写：
	需要用括号把整个函数定义括起来

		(function (x) {
		    return x * x;
		})(3);


	参考： https://www.liaoxuefeng.com/wiki/1022910821149312/1023021250770016

	箭头函数 参考：https://www.liaoxuefeng.com/wiki/1022910821149312/1031549578462080
	generator



#Node

Node 
Node.js平台是在后端运行JavaScript代码

/*
JavaScript在设计时，有两种比较运算符：
第一种是==比较，它会自动转换数据类型再比较，很多时候，会得到非常诡异的结果；
第二种是===比较，它不会自动转换数据类型，如果数据类型不一致，返回false，如果一致，再比较。
由于JavaScript这个设计缺陷，不要使用==比较，始终坚持使用===比较
*// {/* block select <action>a<object> */}

// 赋值语句的等号不完全等同于数学的等号
// 浮点数的相等比较, Java, javascript 都存在这种情况，和零值，其它值相等比较，遵循相同的原理
// 那么大于小于的比较呢？
// Java浮点数的比较（要先考虑精度范围）  https://www.cnblogs.com/zhloong/p/java-float-number-compare.html
// 严格来讲，应该是分数的大小比较有问题， 因为分数可能是无限小数，有限小数的大小是可以直接比较的。。。
// 1 / 3 === (1 - 2 / 3); // false


NodeJs 模块加载机制被称为CommonJS规范,使用 require
