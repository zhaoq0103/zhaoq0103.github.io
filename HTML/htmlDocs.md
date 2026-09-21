HTML 熟悉

HTML 速查列表：
https://www.runoob.com/html/html-quicklist.html

标签简写及全称：
https://www.runoob.com/html/html-tag-name.html




# HTML 元素(标签)和属性
# HTML 标题
		<h1> - <h6> 标签定义标题（Heading)
		<hr> 标签, 水平线
# HTML 文本格式化
	<b> <strong> <i> <em> <sub> <sup> 
	<big> <small> <ins> <del>
	<code> 代码
# HTML 超级链接
	<a>  广本链接 图像链接 锚点链接 href="#id" 
	download 属性支持文件下载
	id 属性
	发送邮件 mailto:
# HTML HEAD
	可以添加在头部区域的元素标签为: 
	<title>, <style>, <meta>, <link>, <script>, <noscript> 和 <base>

	如：
	<link rel="stylesheet" type="text/css" href="mystyle.css">
	<meta http-equiv="refresh" content="30">

# HTML CSS （这个有个单独的篇章，这里简单记录一下）
	style 
	font-family（字体），color（颜色），和font-size
	text-align（文字对齐）

	<link rel="stylesheet" type="text/css" href="mystyle.css">

# HTML IMG
	<img border="0" src="https://www.runoob.com/images/pulpit.jpg" alt="Pulpit rock" width="304" height="228">
	<map>  <area>  </map> <!-- 图片点击区域 -->

# HTML 表格
	<table>
		<th>
		<tr>
		<td>
	</table>

	 cellpadding  colspan rowspan 等

# HTML 列表
	<ul> <li> <!-- 已废弃 -->
	<ol start="0" type="A"> <li> 
	自定义的列表 <dl> <dt> <dd>

# HTML 区块
	<div> <span> 
	创建高级的布局非常耗时，可以使用第三方免费模板，节省时间

# HTML 表单
	<form> 
	<input>
		type text password radio checkbox submit  button
	<textarea> 
	<legend>标签通常和<fieldset>标签一起使用来将表单内的相关元素分组

# HTML 框架 iframe
	<iframe src="URL"></iframe> 
	frameborder

	<iframe src="demo_iframe.htm" name="iframe_a" frameborder="0"></iframe>
	<p>
		<a href="https://www.163.com" target="iframe_a">点我来玩呀</a>
	</p>

	网页不希望被嵌套, 响应头中有一选项 X-Frame-Options

# HTML 颜色 ，颜色名，颜色值
	#RGB #RRGGBB  每个色位用2位16进制表示
	141个颜色名称是在HTML和CSS颜色规范定义的（17标准颜色，再加124）


# HTML 脚本
	<script>   -- 有专门的学习章节 

# HTML 字符实体 （实体名称对大小写敏感）
	HTML 中的预留字符必须被替换为字符实体：
			空格			&nbsp;	&#160;
		<	小于号		&lt;	&#60;
		>	大于号		&gt;	&#62;
		&	和号			&amp;	&#38;
		"	引号			&quot;	&#34;
		'	撇号 		&apos; (IE不支持)	&#39;
		￠	分			&cent;	&#162;
		£	镑			&pound;	&#163;
		¥	人民币/日元	&yen;	&#165;
		€	欧元			&euro;	&#8364;
		§	小节			&sect;	&#167;
		©	版权			&copy;	&#169;
		®	注册商标		&reg;	&#174;
		™	商标			&trade;	&#8482;
		×	乘号			&times;	&#215;
		÷	除号			&divide;	&#247;


# HTML 统一资源定位器(Uniform Resource Locators) URL
	URL 语法规则：
		scheme://host.domain:port/path/filename

	URL 只能使用 ASCII 字符集
	URL 编码使用 "%" 其后跟随两位的十六进制数来替换非 ASCII 字符 (百分号编码， urlEncode,urlDecode)
	URL 不能包含空格。URL 编码通常使用 + 来替换空格


