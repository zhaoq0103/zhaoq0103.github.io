CSS (Cascading Style Sheets，层叠样式表）

# CSS 选择器和声明
	id 和 class 选择器

	id 选择器以 "#" 来定义
	类选择器以一个点 . 号显示
# CSS 的创建
	<link rel="stylesheet" type="text/css" href="mystyle.css">   外部样式表，多文件共用
	<style></style> 内部样式表，本文件专用
	<p style="color:sienna;margin-left:20px">这是一个段落。</p>   内联样式表，用于部分样式


	多重样式优先级
    （内联样式）Inline style > （内部样式）Internal style sheet >（外部样式）External style sheet > 浏览器默认样式

    外部样式放在内部样式的后面，则外部样式将覆盖内部样式，也就是在HTML中出现的顺序有关系。

# CSS 背景
	background-color background-image background-position

	background-image:url('gradient2.png');

	background-repeat:no-repeat; repeat-x; repeat-y;
	background-position:right top;

	背景简写：background:#ffffff url('img_tree.png') no-repeat right top;
	简写时属性有顺序要求 

	background-attachment:fixed; 背景图像固定

# CSS 文本
	颜色和背景色
	对齐方式： text-align:justify; right; left;center;

	 text-decoration 下划线相关
	 text-transform:uppercase;  文本转换
	 text-indent:50px; 文本缩进

	 text-shadow 阴影

# CSS 字体
	CSS中，有两种类型的字体系列
		通用字体系列
		特定字体系列
	font-family:"Times New Roman", Times, serif;

	Serif：字体有装饰
	Sans-serif：字体无装饰
	Monospace：等宽字体

	font-style:normal;italic;oblique;

## 字体大小
	h1 {font-size:40px;} 绝对大小
	h1 {font-size:2.5em;} /* 40px/16=2.5em */


	百分比和EM组合：
	body {font-size:100%;}
	h1 {font-size:2.5em;}

	font-weight

# CSS 链接
# CSS 列表
	list-style-type: circle;square;upper-roman;lower-alpha;

	不同浏览器效果不一样：
	list-style-image: url('sqpurple.gif');

	做浏览器兼容处理:
	ul
	{
    	list-style-type: none;
    	padding: 0px;
    	margin: 0px;
	}
	ul li
	{
    	background-image: url(sqpurple.gif);
    	background-repeat: no-repeat;
    	background-position: 0px 5px; 
    	padding-left: 14px; 
	}

	如果属性简写的话，也有顺序要求，具体查一下相关文档。

# CSS 表格
	border-collapse:collapse; 边框折叠
	text-align:right; left; center;
	vertical-align:bottom;top;middle;
	 padding:15px;  边框和内容之间的距离

	 caption {caption-side:top;} 表格标题

# CSS 盒子模型
	margin border padding content
	

# CSS 边框
	border-style:none;dotted;solid;dashed;double;groove;.........
	简写： border-style:dotted solid dashed double;
	border-width:medium;5px; 配合 border-style使用
	border-color:
	也可以单独设置每条边框 border-top-style
	简写： border:5px solid red;

	border: 2px solid red;
  	border-radius: 50px 20px;


# CSS 轮廓（outline）
	outline outline-color outline-style outline-width
# CSS margin(外边距)
	margin auto; length(4px, 3cm..); %;
# CSS padding（填充）
	padding: 10px; 100cm
# CSS 分组 和 嵌套 选择器
	选择器用逗号分隔;
	嵌套选择器:
		p{ }: 为所有 p 元素指定一个样式。
		.marked{ }: 为所有 class="marked" 的元素指定一个样式。
		.marked p{ }: 为所有 class="marked" 元素内的 p 元素指定一个样式。
		p.marked{ }: 为所有 class="marked" 的 p 元素指定一个样式。

# CSS 尺寸 (Dimension)
	height;width;line-height
# CSS Display(显示) 与 Visibility（可见性）
	display:none 与 visibility:hidden 的区别
	visibility:hidden; 占用空间
	display:none; 不占用空间

	CSS Display - 块元素 block;
		<h1> <p> <div>
				- 内联元素 inline;
		<span> <a>

# CSS Position(定位): bottom,clip,cursor,overflow,left
	static(元素的默认值) relative fixed absolute sticky

	这个粘性定位还挺有意思
	.sticky {
	  	position: -webkit-sticky; /* Safari */
	  	position: sticky;
	  	top: 0;
	  	padding: 5px;
	  	background-color: #cae8ca;
	  	border: 2px solid #4CAF50;
	}

	z-index： 元素的堆叠顺序

	img 
	{
		position:absolute;
		clip:rect(0px,60px,200px,0px);
	}

	overflow: scroll; visible;inherit;...

# CSS 布局 - Overflow
	overflow: visible;hidden;scroll;auto
# CSS Float(浮动)
	float:right;...

	clear 属性指定元素两侧不能出现浮动元素
	clear:both; left;....






























