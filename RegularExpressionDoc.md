Regular Expression Doc

基本要素：
	1. 特殊字符类
	   .   ==> 任意一个字符 （除换行符） [\s\S]   [^\n\r]  要匹配包括 '\n' 在内的任何字符，请使用像"(.|\n)"的模式
	   []  ==> []中的任意一个字符     [0-9\.\-] // 匹配所有的数字，句号和减号  [ \f\r\t\n] // 匹配所有的白字符
	   -  ==> 在[]中表示范围
	   ^  ==> 在[]中表示排除[]中的字符，匹配这之外的任一字符

	2. 数量限定符
	   ?    ==> (0次或1次）
	   +    ==> (1次或n次）默认贪婪，用?可以限制贪婪
	   *    ==> (0次或n次）默认贪婪，用?可以限制贪婪
	   {N}  ==> 精确匹配N次
	   {N,} ==> 最少匹配N次
	   {,M} ==> 最多匹配M次  [匹配失败，应该是错误或者废弃用法]
	   {N,M}==> 最少匹配N次，最多匹配M次

	3. 位置限定 （不能与 数量限定符一起使用）
	   ^   匹配行首（不在[]中）
	   $   匹配行尾（不在[]中），^$匹配空行
	   \<  匹配单词开头  echo "world aworld worlda aworldb world" | grep -E --color '\<world'
	   \>  匹配单词结尾  echo "world aworld worlda aworldb world" | grep -E --color 'world\>'
	   \b  匹配单词开头或者结尾   echo "world aworld worlda aworldb world" | grep -E --color 'world\b'    'er\b' 可以匹配"never" 中的 'er'，但不能匹配 "verb" 中的 'er'。
	   \B  匹配非单词开头或者结尾 echo "world aworld worlda aworldb world" | grep -E --color 'world\B'    'er\B' 能匹配 "verb" 中的 'er'，但不能匹配 "never" 中的 'er'。

	4. 特殊符号
		\    ==> 转义
		()   ==> 用于分组和捕获子表达式 
		(?: )   ==> 用于分组不捕获子表达式  ?: 是非捕获元之一，还有两个非捕获元是 ?= 和 ?!
		|    ==> 或，用于模式选择  'z|food' 能匹配 "z" 或 "food"。'(z|f)ood' 则匹配 "zood" 或 "food"

	5. 元字符
	  \d   数字    echo "ab123456cd_@#" | grep -E --color '\d' ）
	  \D   非数字
	  \w   数字，字母，下划线
	  \W   非数字，字母，下划线
	  \s   空白
	  \S   非空白

	6. 修饰词
	  g ==> 全局匹配
	  i ==> 不区分大小写
	  m ==> 多行匹配
	  s ==> 圆点 . 中包含换行符




// -------------------测试用例--------------------------------

^[-]?[0-9]+\.?[0-9]+$
^\-?[0-9]{1,}\.?[0-9]{1,}$
^[-]?[0-9]+(\.[0-9]+)?$
所有的浮点数 (浮点数是不是必须得有.)

/^\s*$/ 空行

<div id="drawing" type="PaintView" width="512" height="360"></div>
HTML标记的正则表达式：
<(\S*?)[^>]*>.*?|<.*? /> 
( 首尾空白字符的正则表达式：^\s*|\s*$或(^\s*)|(\s*$) 
(可以用来删除行首行尾的空白字符(包括空格、制表符、换页符等等)，非常有用的表达式)


type Rect struct {
        shapeBase `json:",inline"`
        rectData  `json:"rect"`
}

type rectData struct {
        X      coord      `json:"x"`
        Y      coord      `json:"y"`
        Width  coord      `json:"width"`
        Height coord      `json:"height"`
        Style  ShapeStyle `json:"style"`
}

type pathData struct {
	Points []Point    `json:"points,omitempty"`
	Close  bool       `json:"close,omitempty"`
	Style  ShapeStyle `json:"style" bson:"style"`
}


// ---------------------------------------------------

type Ellipse struct {
        shapeBase   `json:",inline"`
        ellipseData `json:"ellipse"`
}

type ellipseData struct {
        X       coord      `json:"x"`
        Y       coord      `json:"y"`
        RadiusX coord      `json:"radiusX"`
        RadiusY coord      `json:"radiusY"`
        Style   ShapeStyle `json:"\style"`



grep -E '(son:)(",?\w+\s*,?\w?")'  ~/Desktop/vim-regular-test.txt
grep -E 'son[ ]*:[ ]*"[a-zA-Z0-9 ,]*"'  ~/Desktop/vim-regular-test.txt
grep -E 'son\s*:\s*"[a-zA-Z0-9 ,]*"'  ~/Desktop/vim-regular-test.txt

grep -E 'son:.*`$'  ~/Desktop/vim-regular-test.txt

sed -rn 's#son[ ]*:[ ]*".[a-zA-Z0-9 ]*"#& b&#gp' ~/Desktop/vim-regular-test.txt
sed -r 's#son[ ]*:[ ]*"[a-zA-Z0-9 ,]*"#& b&#g' ~/Desktop/vim-regular-test.txt
sed -r 's#(son[ ]*:[ ]*"[a-zA-Z0-9 ,]*")#\1 b\1#g' ~/Desktop/vim-regular-test.txt



sed -r 's#son:.*#& b&#g' ~/Desktop/vim-regular-test.txt


这个应该是最合理的一条：
sed -r 's#`j(son.*)`#`j\1 b\1`#g' ~/Desktop/vim-regular-test.txt









