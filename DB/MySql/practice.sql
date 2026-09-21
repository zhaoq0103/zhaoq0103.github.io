# SQL查询从入门到精通学习案例

## 入门部分 (25%)

### 1. SQL基础语法与简单查询

#### 1.1 数据库与表的创建
```sql
-- 创建数据库
CREATE DATABASE learn_sql;

-- 使用数据库
USE learn_sql;

-- 创建基础表结构
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT,
    gender CHAR(1),
    class VARCHAR(20),
    admission_date DATE
);

CREATE TABLE courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credit INT,
    teacher_name VARCHAR(50)
);

CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    score DECIMAL(5,2),
    exam_date DATE,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
```

#### 1.2 基本的SELECT查询
```sql
-- 查询所有学生
SELECT * FROM students;

-- 查询特定列
SELECT name, age, class FROM students;

-- 使用WHERE条件
SELECT * FROM students WHERE age > 18;
SELECT * FROM students WHERE class = '一年级';

-- 排序
SELECT * FROM students ORDER BY age ASC;
SELECT * FROM students ORDER BY admission_date DESC;

-- 限制结果数量
SELECT * FROM students LIMIT 5;
```

### 2. 条件查询与函数

#### 2.1 条件组合
```sql
-- 使用AND和OR组合条件
SELECT * FROM students WHERE age > 18 AND gender = 'M';
SELECT * FROM students WHERE class = '一年级' OR class = '二年级';

-- 使用IN和BETWEEN
SELECT * FROM students WHERE class IN ('一年级', '二年级', '三年级');
SELECT * FROM students WHERE age BETWEEN 15 AND 18;

-- 模糊查询
SELECT * FROM students WHERE name LIKE '张%';
SELECT * FROM students WHERE class LIKE '%一%';
```

#### 2.2 常用SQL函数
```sql
-- 聚合函数
SELECT COUNT(*) FROM students;
SELECT AVG(score) FROM scores WHERE course_id = 101;
SELECT MAX(score), MIN(score) FROM scores WHERE student_id = 1;
SELECT SUM(credit) FROM courses;

-- 字符串函数
SELECT CONCAT(name, '-', class) AS student_info FROM students;
SELECT UPPER(name), LOWER(name) FROM students;
SELECT LENGTH(name) FROM students;

-- 日期函数
SELECT name, YEAR(admission_date) as admission_year FROM students;
SELECT DATEDIFF(CURDATE(), admission_date) AS days_since_admission FROM students;
```

## 深入部分 (75%)

### 3. 多表关联查询

#### 3.1 基本关联
```sql
-- 内连接
SELECT s.name, c.course_name, sc.score 
FROM students s
JOIN scores sc ON s.student_id = sc.student_id
JOIN courses c ON sc.course_id = c.course_id;

-- 左外连接
SELECT s.name, c.course_name, sc.score 
FROM students s
LEFT JOIN scores sc ON s.student_id = sc.student_id
LEFT JOIN courses c ON sc.course_id = c.course_id;

-- 右外连接
SELECT s.name, c.course_name, sc.score 
FROM scores sc
RIGHT JOIN students s ON s.student_id = sc.student_id
RIGHT JOIN courses c ON sc.course_id = c.course_id;
```

#### 3.2 高级关联
```sql
-- 全外连接（MySQL不直接支持，需要使用UNION）
SELECT s.name, c.course_name, sc.score 
FROM students s
LEFT JOIN scores sc ON s.student_id = sc.student_id
LEFT JOIN courses c ON sc.course_id = c.course_id
UNION
SELECT s.name, c.course_name, sc.score 
FROM students s
RIGHT JOIN scores sc ON s.student_id = sc.student_id
RIGHT JOIN courses c ON sc.course_id = c.course_id
WHERE s.student_id IS NULL;

-- 自连接
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    manager_id INT
);

SELECT e1.emp_name AS employee, e2.emp_name AS manager
FROM employees e1
LEFT JOIN employees e2 ON e1.manager_id = e2.emp_id;

-- 交叉连接（笛卡尔积）
SELECT s.name, c.course_name
FROM students s
CROSS JOIN courses c;
```

### 4. 分组与聚合

#### 4.1 基本分组
```sql
-- 按班级分组统计学生数量
SELECT class, COUNT(*) AS student_count
FROM students
GROUP BY class;

-- 按课程分组计算平均分
SELECT c.course_name, AVG(sc.score) AS avg_score
FROM scores sc
JOIN courses c ON sc.course_id = c.course_id
GROUP BY c.course_id, c.course_name;
```

#### 4.2 高级分组与聚合
```sql
-- 使用HAVING过滤分组结果
SELECT class, COUNT(*) AS student_count
FROM students
GROUP BY class
HAVING COUNT(*) > 10;

-- 使用WITH ROLLUP进行小计和总计
SELECT class, gender, COUNT(*) AS student_count
FROM students
GROUP BY class, gender WITH ROLLUP;

-- 组合使用聚合函数
SELECT 
    course_id,
    COUNT(*) AS total_students,
    AVG(score) AS avg_score,
    MAX(score) AS highest_score,
    MIN(score) AS lowest_score,
    STDDEV(score) AS score_deviation
FROM scores
GROUP BY course_id;
```

### 5. 子查询与派生表

#### 5.1 基本子查询
```sql
-- 在WHERE子句中使用子查询
SELECT name
FROM students
WHERE student_id IN (
    SELECT student_id 
    FROM scores 
    WHERE score > 90
);

-- 在SELECT子句中使用子查询
SELECT 
    s.name,
    (SELECT AVG(score) FROM scores WHERE student_id = s.student_id) AS avg_score
FROM students s;

-- 在FROM子句中使用子查询
SELECT high_score_count.class, COUNT(*) as student_count
FROM (
    SELECT s.student_id, s.class
    FROM students s
    JOIN scores sc ON s.student_id = sc.student_id
    WHERE sc.score > 90
    GROUP BY s.student_id
) AS high_score_count
GROUP BY high_score_count.class;
```

#### 5.2 高级子查询
```sql
-- 相关子查询
SELECT s.name, s.class
FROM students s
WHERE EXISTS (
    SELECT 1 
    FROM scores sc 
    WHERE sc.student_id = s.student_id AND sc.score > 95
);

-- 使用ALL、ANY和SOME操作符
SELECT name 
FROM students 
WHERE student_id = ANY (
    SELECT student_id 
    FROM scores 
    WHERE score = 100
);

SELECT course_id, course_name
FROM courses
WHERE credit > ALL (
    SELECT AVG(credit) 
    FROM courses 
    GROUP BY teacher_name
);
```

### 6. 窗口函数与高级分析

#### 6.1 基本窗口函数
```sql
-- 排名函数
SELECT 
    s.name,
    c.course_name,
    sc.score,
    RANK() OVER (PARTITION BY sc.course_id ORDER BY sc.score DESC) AS rank,
    DENSE_RANK() OVER (PARTITION BY sc.course_id ORDER BY sc.score DESC) AS dense_rank,
    ROW_NUMBER() OVER (PARTITION BY sc.course_id ORDER BY sc.score DESC) AS row_num
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
JOIN courses c ON sc.course_id = c.course_id;

-- 分析函数
SELECT 
    s.name,
    c.course_name,
    sc.score,
    AVG(sc.score) OVER (PARTITION BY sc.course_id) AS course_avg,
    sc.score - AVG(sc.score) OVER (PARTITION BY sc.course_id) AS diff_from_avg
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
JOIN courses c ON sc.course_id = c.course_id;
```

#### 6.2 高级窗口函数
```sql
-- 累计分布和百分比排名
SELECT 
    s.name,
    c.course_name,
    sc.score,
    PERCENT_RANK() OVER (PARTITION BY sc.course_id ORDER BY sc.score) AS percent_rank,
    CUME_DIST() OVER (PARTITION BY sc.course_id ORDER BY sc.score) AS cume_dist
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
JOIN courses c ON sc.course_id = c.course_id;

-- 框架规范（ROWS/RANGE）
SELECT 
    s.name,
    sc.score,
    c.course_name,
    SUM(sc.score) OVER (
        PARTITION BY sc.course_id 
        ORDER BY sc.score 
        ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING
    ) AS window_sum,
    AVG(sc.score) OVER (
        PARTITION BY sc.course_id 
        ORDER BY sc.score 
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_avg
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
JOIN courses c ON sc.course_id = c.course_id;

-- LAG和LEAD函数
SELECT 
    s.name,
    c.course_name,
    sc.score,
    LAG(sc.score) OVER (PARTITION BY sc.course_id ORDER BY sc.score) AS prev_score,
    LEAD(sc.score) OVER (PARTITION BY sc.course_id ORDER BY sc.score) AS next_score
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
JOIN courses c ON sc.course_id = c.course_id;
```

### 7. 高级查询技术

#### 7.1 公用表表达式(CTE)
```sql
-- 基本CTE用法
WITH high_scores AS (
    SELECT student_id, COUNT(*) AS high_score_count
    FROM scores
    WHERE score > 90
    GROUP BY student_id
)
SELECT s.name, h.high_score_count
FROM students s
JOIN high_scores h ON s.student_id = h.student_id
ORDER BY h.high_score_count DESC;

-- 递归CTE
CREATE TABLE organization (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    parent_id INT
);

WITH RECURSIVE org_hierarchy AS (
    -- 基本情况
    SELECT id, name, parent_id, 0 AS level, CAST(name AS CHAR(200)) AS path
    FROM organization
    WHERE parent_id IS NULL
    
    UNION ALL
    
    -- 递归部分
    SELECT o.id, o.name, o.parent_id, h.level + 1, CONCAT(h.path, ' > ', o.name)
    FROM organization o
    JOIN org_hierarchy h ON o.parent_id = h.id
)
SELECT * FROM org_hierarchy ORDER BY path;
```

#### 7.2 高级数据操作
```sql
-- PIVOT操作（MySQL不直接支持，使用CASE模拟）
SELECT 
    s.class,
    SUM(CASE WHEN c.course_name = '数学' THEN sc.score ELSE 0 END) AS math_total,
    SUM(CASE WHEN c.course_name = '语文' THEN sc.score ELSE 0 END) AS chinese_total,
    SUM(CASE WHEN c.course_name = '英语' THEN sc.score ELSE 0 END) AS english_total
FROM students s
JOIN scores sc ON s.student_id = sc.student_id
JOIN courses c ON sc.course_id = c.course_id
GROUP BY s.class;

-- 字符串聚合（MySQL不直接支持，使用GROUP_CONCAT）
SELECT 
    c.course_id,
    c.course_name,
    GROUP_CONCAT(s.name ORDER BY sc.score DESC SEPARATOR ', ') AS top_students
FROM courses c
JOIN scores sc ON c.course_id = sc.course_id
JOIN students s ON sc.student_id = s.student_id
WHERE sc.score > 90
GROUP BY c.course_id, c.course_name;
```

### 8. 性能优化与实际应用

#### 8.1 SQL性能分析与优化
```sql
-- 使用EXPLAIN分析查询
EXPLAIN SELECT 
    s.name, 
    c.course_name, 
    sc.score
FROM students s
JOIN scores sc ON s.student_id = sc.student_id
JOIN courses c ON sc.course_id = c.course_id
WHERE s.class = '一年级' AND sc.score > 80;

-- 创建适当的索引
CREATE INDEX idx_students_class ON students(class);
CREATE INDEX idx_scores_score ON scores(score);
CREATE INDEX idx_scores_student_course ON scores(student_id, course_id);

-- 查询重写优化
-- 原始查询
SELECT s.name, AVG(sc.score) AS avg_score
FROM students s
LEFT JOIN scores sc ON s.student_id = sc.student_id
WHERE sc.score > 60
GROUP BY s.student_id;

-- 优化后查询
SELECT s.name, avg_scores.avg_score
FROM students s
JOIN (
    SELECT student_id, AVG(score) AS avg_score
    FROM scores
    WHERE score > 60
    GROUP BY student_id
) avg_scores ON s.student_id = avg_scores.student_id;
```

#### 8.2 实际业务场景应用
```sql
-- 学生成绩分析报表
WITH student_stats AS (
    SELECT 
        s.student_id,
        s.name,
        s.class,
        COUNT(sc.id) AS course_count,
        AVG(sc.score) AS avg_score,
        SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) AS excellent_count,
        SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) AS failed_count
    FROM students s
    LEFT JOIN scores sc ON s.student_id = sc.student_id
    GROUP BY s.student_id, s.name, s.class
),
class_stats AS (
    SELECT 
        class,
        AVG(avg_score) AS class_avg_score,
        AVG(excellent_count) AS class_avg_excellent,
        COUNT(*) AS student_count
    FROM student_stats
    GROUP BY class
)
SELECT 
    ss.*,
    cs.class_avg_score,
    cs.class_avg_excellent,
    cs.student_count,
    RANK() OVER (PARTITION BY ss.class ORDER BY ss.avg_score DESC) AS class_rank,
    PERCENT_RANK() OVER (ORDER BY ss.avg_score) AS percentile
FROM student_stats ss
JOIN class_stats cs ON ss.class = cs.class
ORDER BY ss.class, class_rank;

-- 课程难度分析
SELECT 
    c.course_id,
    c.course_name,
    COUNT(sc.id) AS student_count,
    AVG(sc.score) AS avg_score,
    STDDEV(sc.score) AS score_stddev,
    MIN(sc.score) AS min_score,
    MAX(sc.score) AS max_score,
    SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS excellent_rate,
    SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS fail_rate,
    CASE 
        WHEN AVG(sc.score) < 60 THEN '非常困难'
        WHEN AVG(sc.score) < 70 THEN '困难'
        WHEN AVG(sc.score) < 80 THEN '一般'
        WHEN AVG(sc.score) < 90 THEN '简单'
        ELSE '非常简单'
    END AS difficulty_level
FROM courses c
JOIN scores sc ON c.course_id = sc.course_id
GROUP BY c.course_id, c.course_name
ORDER BY avg_score;

-- 学生成长追踪（假设有多学期数据）
CREATE TABLE score_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    semester VARCHAR(20),
    score DECIMAL(5,2)
);

WITH student_progress AS (
    SELECT 
        sh.student_id,
        s.name,
        sh.course_id,
        c.course_name,
        sh.semester,
        sh.score,
        LAG(sh.score) OVER (
            PARTITION BY sh.student_id, sh.course_id 
            ORDER BY sh.semester
        ) AS prev_score,
        LEAD(sh.score) OVER (
            PARTITION BY sh.student_id, sh.course_id 
            ORDER BY sh.semester
        ) AS next_score
    FROM score_history sh
    JOIN students s ON sh.student_id = s.student_id
    JOIN courses c ON sh.course_id = c.course_id
)
SELECT 
    student_id,
    name,
    course_id,
    course_name,
    semester,
    score,
    prev_score,
    (score - prev_score) AS improvement,
    CASE 
        WHEN (score - prev_score) > 10 THEN '显著提升'
        WHEN (score - prev_score) > 0 THEN '有所提升'
        WHEN (score - prev_score) = 0 THEN '保持不变'
        ELSE '有所下降'
    END AS progress_status
FROM student_progress
WHERE prev_score IS NOT NULL
ORDER BY student_id, course_id, semester;
```






===========================================================

EXISTS 是 SQL 中的一个逻辑运算符，用于检查子查询是否返回任何结果。
如果子查询返回至少一行，则 EXISTS 返回 TRUE，否则返回 FALSE


3种查询方法对比：
NOT EXISTS 在关联查询中效率优于 IN 或 JOIN

NOT EXISTS (SELECT 1 FROM t_financial_divide_refund WHERE order_id = di.order_id AND `status` = 'refuned')
LEFT JOIN t_financial_divide_refund fr ON fr.order_id = di.order_id AND fr.status = 'refuned' WHERE fr.order_id IS NULL
WHERE di.order_id NOT IN (SELECT order_id FROM t_financial_divide_refund  WHERE status = 'refuned')


========================================================

DROP TABLE IF EXISTS tb_order_overall;
CREATE TABLE tb_order_overall (
id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
order_id INT NOT NULL COMMENT '订单号',
uid INT NOT NULL COMMENT '用户ID',
event_time datetime COMMENT '下单时间',
total_amount DECIMAL NOT NULL COMMENT '订单总金额',
total_cnt INT NOT NULL COMMENT '订单商品总件数',
`status` TINYINT NOT NULL COMMENT '订单状态'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_product_info;
CREATE TABLE tb_product_info (
id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
product_id INT NOT NULL COMMENT '商品ID',
shop_id INT NOT NULL COMMENT '店铺ID',
tag VARCHAR(12) COMMENT '商品类别标签',
in_price DECIMAL NOT NULL COMMENT '进货价格',
quantity INT NOT NULL COMMENT '进货数量',
release_time datetime COMMENT '上架时间'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_order_detail;
CREATE TABLE tb_order_detail (
id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
order_id INT NOT NULL COMMENT '订单号',
product_id INT NOT NULL COMMENT '商品ID',
price DECIMAL NOT NULL COMMENT '商品单价',
cnt INT NOT NULL COMMENT '下单数量'
) CHARACTER SET utf8 COLLATE utf8_bin;

INSERT INTO tb_product_info(product_id, shop_id, tag, in_price, quantity, release_time) VALUES
(8001, 901, '日用', 60, 1000, '2020-01-01 10:00:00'),
(8002, 901, '零食', 140, 500, '2020-01-01 10:00:00'),
(8003, 901, '零食', 160, 500, '2020-01-01 10:00:00'),
(8004, 902, '零食', 130, 500, '2020-01-01 10:00:00');

INSERT INTO tb_order_overall(order_id, uid, event_time, total_amount, total_cnt, `status`) VALUES
(301004, 102, '2021-09-30 10:00:00', 170, 1, 1),
(301005, 104, '2021-10-01 10:00:00', 160, 1, 1),
(301003, 101, '2021-10-02 10:00:00', 300, 2, 1),
(301002, 102, '2021-10-03 11:00:00', 235, 2, 1);

INSERT INTO tb_order_detail(order_id, product_id, price, cnt) VALUES
(301004, 8002, 180, 1),
(301002, 8001, 85, 1),
(301002, 8003, 180, 1),
(301003, 8004, 140, 1);


问题：请计算店铺901在2021年国庆头3天的7日动销率和滞销率，结果保留3位小数，按日期升序排序。

注：
动销率定义为店铺中一段时间内有销量的商品占当前已上架总商品数的比例（有销量的商品/已上架总商品数)。
滞销率定义为店铺中一段时间内没有销量的商品占当前已上架总商品数的比例。（没有销量的商品/已上架总商品数)。
只要当天任一店铺有任何商品的销量就输出该天的结果，即使店铺901当天的动销率为0

聚合窗口函数（SUM、AVG等）、
排名函数（ROW_NUMBER、RANK、DENSE_RANK）、
分析函数（LEAD、LAG、FIRST_VALUE、LAST_VALUE）

SELECT dt, sale_rate, 1 - sale_rate as unsale_rate
FROM (
    SELECT dt, ROUND(MIN(sale_pid_cnt) / COUNT(all_pid), 3) as sale_rate
    FROM (
        -- 国庆期间店铺901截止每天的近7天有销量的商品数
        SELECT 
        dt
        , COUNT(DISTINCT IF(shop_id!=901, NULL, product_id)) as sale_pid_cnt
        FROM (
            SELECT DISTINCT DATE(event_time) as dt
            FROM mall_db.tb_order_overall
            WHERE DATE(event_time) BETWEEN '2021-10-01' AND '2021-10-03'
        ) as t_dates
        LEFT JOIN (
            SELECT 
              DISTINCT 
        DATE(event_time) as event_dt, product_id
--             *
            FROM mall_db.tb_order_overall
            JOIN mall_db.tb_order_detail USING(order_id)
        ) as t_dt_pid ON DATEDIFF(dt,event_dt) BETWEEN 0 AND 6
        LEFT JOIN mall_db.tb_product_info pi USING(product_id)
        GROUP BY dt
    ) as t_dt_901_pid_cnt
    LEFT JOIN (
        -- 店铺901每个商品上架日期
        SELECT DATE(release_time) as release_dt, product_id as all_pid
        FROM mall_db.tb_product_info
        WHERE shop_id=901
    ) as t_release_dt ON dt >= release_dt # 当天店铺901已上架在售的商品
    GROUP BY dt
) as t_dt_sr;



===========================================

DROP TABLE IF EXISTS tb_order_overall;
CREATE TABLE tb_order_overall (
id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
order_id INT NOT NULL COMMENT '订单号',
uid INT NOT NULL COMMENT '用户ID',
event_time datetime COMMENT '下单时间',
total_amount DECIMAL NOT NULL COMMENT '订单总金额',
total_cnt INT NOT NULL COMMENT '订单商品总件数',
`status` TINYINT NOT NULL COMMENT '订单状态'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_product_info;
CREATE TABLE tb_product_info (
id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
product_id INT NOT NULL COMMENT '商品ID',
shop_id INT NOT NULL COMMENT '店铺ID',
tag VARCHAR(12) COMMENT '商品类别标签',
in_price DECIMAL NOT NULL COMMENT '进货价格',
quantity INT NOT NULL COMMENT '进货数量',
release_time datetime COMMENT '上架时间'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_order_detail;
CREATE TABLE tb_order_detail (
id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
order_id INT NOT NULL COMMENT '订单号',
product_id INT NOT NULL COMMENT '商品ID',
price DECIMAL NOT NULL COMMENT '商品单价',
cnt INT NOT NULL COMMENT '下单数量'
) CHARACTER SET utf8 COLLATE utf8_bin;

INSERT INTO tb_product_info(product_id, shop_id, tag, in_price, quantity, release_time) VALUES
(8001, 901, '日用', 60, 1000, '2020-01-01 10:00:00'),
(8002, 901, '零食', 140, 500, '2020-01-01 10:00:00'),
(8003, 901, '零食', 160, 500, '2020-01-01 10:00:00'),
(8004, 902, '零食', 130, 500, '2020-01-01 10:00:00');

INSERT INTO tb_order_overall(order_id, uid, event_time, total_amount, total_cnt, `status`) VALUES
(301011, 102, '2021-10-31 11:00:00', 260, 2, 1),
(301013, 105, '2021-11-02 10:00:00', 300, 2, 1),
(301004, 103, '2021-09-30 10:00:00', 170, 1, 1),
(301002, 102, '2021-10-01 11:00:00', 235, 2, 1),
(301003, 101, '2021-10-02 10:00:00', 300, 2, 1),
(301005, 104, '2021-10-03 10:00:00', 160, 1, 1);

INSERT INTO tb_order_detail(order_id, product_id, price, cnt) VALUES
  (301002, 8001, 85, 1),
  (301002, 8003, 180, 1),
  (301003, 8004, 140, 1),
  (301003, 8003, 180, 1),
  (301005, 8003, 180, 1);


# 问题：请计算2021年10月商城里所有新用户的首单平均交易金额（客单价）和平均获客成本（保留一位小数）。
# 注：订单的优惠金额 = 订单明细里的{该订单各商品单价×数量之和} - 订单总表里的{订单总金额} 。

select 
    round(sum(oo.total_amount/oo.total_cnt)/NULLIF(count(distinct oo.uid),0),1) as avg_amount
    ,round((sum(od.price*cnt)-sum(oo.total_amount/oo.total_cnt))/NULLIF(count(distinct oo.uid),0),1) as avg_cost
from tb_order_detail od 
join 
(select order_id,uid,event_time,total_amount,total_cnt,status,row_number()over(partition by uid order by event_time asc) as tk from tb_order_overall
) as  -- 查询出新用户首单
 oo using(order_id)
where 
    oo.event_time >= '2021-10-01' and oo.event_time < '2021-11-01'
    and oo.status = 1 and oo.tk = 1



select 
    round(sum(total_amount)/count(distinct too.order_id),1) avg_amount
    ,round(sum(price-total_amount)/count(distinct  too.order_id),1) avg_cost
from tb_order_overall too
inner join (
  select order_id,sum(price*cnt) price from tb_order_detail group by order_id
  ) as t
on too.order_id=t.order_id
where 
    date_format(event_time,'%Y-%m')='2021-10'
    and (uid,date(event_time)) in (select uid,min(date(event_time)) from tb_order_overall group by uid)


=======================================

DROP TABLE IF EXISTS tb_user_event;
CREATE TABLE tb_user_event (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    uid INT NOT NULL COMMENT '用户ID',
    product_id INT NOT NULL COMMENT '商品ID',
    event_time datetime COMMENT '行为时间',
    if_click TINYINT COMMENT '是否点击',
    if_cart TINYINT COMMENT '是否加购物车',
    if_payment TINYINT COMMENT '是否付款',
    if_refund TINYINT COMMENT '是否退货退款'
) CHARACTER SET utf8 COLLATE utf8_bin;

INSERT INTO tb_user_event(uid, product_id, event_time, if_click, if_cart, if_payment, if_refund) VALUES
  (101, 8001, '2021-10-01 10:00:00', 0, 0, 0, 0),
  (102, 8001, '2021-10-01 10:00:00', 1, 0, 0, 0),
  (103, 8001, '2021-10-01 10:00:00', 1, 1, 0, 0),
  (104, 8001, '2021-10-02 10:00:00', 1, 1, 1, 0),
  (105, 8001, '2021-10-02 10:00:00', 1, 1, 1, 0),
  (101, 8002, '2021-10-03 10:00:00', 1, 1, 1, 0),
  (109, 8001, '2021-10-04 10:00:00', 1, 1, 1, 1);



# 请统计2021年10月每个有展示记录的退货率不大于0.5的商品各项指标

select
    product_id,
    round(sum(ue.if_click) / count(ue.product_id), 3) as ctr,
    if(sum(ue.if_click)=0,0,round(sum(ue.if_cart) / sum(ue.if_click), 3)) as cart_rate,
    if(sum(ue.if_cart)=0,0,round(sum(ue.if_payment) / sum(ue.if_cart), 3)) as payment_rate,
    if(sum(ue.if_payment)=0,0,round(sum(ue.if_refund) / sum(ue.if_payment), 3)) as refund_rate
from
    tb_user_event ue
where
    ue.event_time >= "2021-10-01"
    and ue.event_time < "2021-11-01"
group by ue.product_id
having refund_rate <= 0.5
order by product_id asc;



===============================================
DROP TABLE IF EXISTS tb_order_overall;
CREATE TABLE tb_order_overall (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    order_id INT NOT NULL COMMENT '订单号',
    uid INT NOT NULL COMMENT '用户ID',
    event_time datetime COMMENT '下单时间',
    total_amount DECIMAL NOT NULL COMMENT '订单总金额',
    total_cnt INT NOT NULL COMMENT '订单商品总件数',
    `status` TINYINT NOT NULL COMMENT '订单状态'
) CHARACTER SET utf8 COLLATE utf8_bin;

INSERT INTO tb_order_overall(order_id, uid, event_time, total_amount, total_cnt, `status`) VALUES
  (301001, 101, '2021-10-01 10:00:00', 30000, 3, 1),
  (301002, 102, '2021-10-01 11:00:00', 23900, 2, 1),
  (301003, 103, '2021-10-02 10:00:00', 31000, 2, 1);

DROP TABLE IF EXISTS tb_product_info;
CREATE TABLE tb_product_info (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    product_id INT NOT NULL COMMENT '商品ID',
    shop_id INT NOT NULL COMMENT '店铺ID',
    tag VARCHAR(12) COMMENT '商品类别标签',
    in_price DECIMAL NOT NULL COMMENT '进货价格',
    quantity INT NOT NULL COMMENT '进货数量',
    release_time datetime COMMENT '上架时间'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_order_detail;
CREATE TABLE tb_order_detail (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    order_id INT NOT NULL COMMENT '订单号',
    product_id INT NOT NULL COMMENT '商品ID',
    price DECIMAL NOT NULL COMMENT '商品单价',
    cnt INT NOT NULL COMMENT '下单数量'
) CHARACTER SET utf8 COLLATE utf8_bin;

INSERT INTO tb_product_info(product_id, shop_id, tag, in_price, quantity, release_time) VALUES
  (8001, 901, '家电', 6000, 100, '2020-01-01 10:00:00'),
  (8002, 902, '家电', 12000, 50, '2020-01-01 10:00:00'),
  (8003, 901, '3C数码', 12000, 50, '2020-01-01 10:00:00');

INSERT INTO tb_order_detail(order_id, product_id, price, cnt) VALUES
  (301001, 8001, 8500, 2),
  (301001, 8002, 15000, 1),
  (301002, 8001, 8500, 1),
  (301002, 8002, 16000, 1),
  (301003, 8002, 14000, 1),
  (301003, 8003, 18000, 1);


  # 计算2021年10月以来店铺901中商品毛利率大于24.9%的商品信息及该店铺整体毛利率。

select
    "店铺汇总" as product_id,
    CONCAT (
        round(
            (
                1 - sum(pi.in_price * od.cnt) / sum(od.price * od.cnt)
            ) * 100,
            1
        ),
        "%"
    ) as profit_rate
from
    tb_order_overall oo
    join tb_order_detail od on oo.order_id = od.order_id
    join tb_product_info pi on od.product_id = pi.product_id
where
    oo.event_time >= "2021-10-01"
    and pi.shop_id = 901
    and oo.status in (1)
group by
    pi.shop_id
union all
select
    od.product_id as product_id,
    CONCAT (
        round(
            (
                1 - sum(pi.in_price * od.cnt) / sum(od.price * od.cnt)
            ) * 100,
            1
        ),
        "%"
    ) as profit_rate
from
    tb_order_overall oo
    join tb_order_detail od on oo.order_id = od.order_id
    join tb_product_info pi on od.product_id = pi.product_id
where
    oo.event_time >= "2021-10-01"
    and pi.shop_id = 901
    and oo.status in (1)
group by
    od.product_id
having
    ROUND(
        (
            1 - SUM(pi.in_price * od.cnt) / SUM(od.price * od.cnt)
        ) * 100,
        1
    ) > 24.9;


=========================================
DROP TABLE IF EXISTS tb_order_overall;
CREATE TABLE tb_order_overall (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    order_id INT NOT NULL COMMENT '订单号',
    uid INT NOT NULL COMMENT '用户ID',
    event_time datetime COMMENT '下单时间',
    total_amount DECIMAL NOT NULL COMMENT '订单总金额',
    total_cnt INT NOT NULL COMMENT '订单商品总件数',
    `status` TINYINT NOT NULL COMMENT '订单状态'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_product_info;
CREATE TABLE tb_product_info (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    product_id INT NOT NULL COMMENT '商品ID',
    shop_id INT NOT NULL COMMENT '店铺ID',
    tag VARCHAR(12) COMMENT '商品类别标签',
    in_price DECIMAL NOT NULL COMMENT '进货价格',
    quantity INT NOT NULL COMMENT '进货数量',
    release_time datetime COMMENT '上架时间'
) CHARACTER SET utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS tb_order_detail;
CREATE TABLE tb_order_detail (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自增ID',
    order_id INT NOT NULL COMMENT '订单号',
    product_id INT NOT NULL COMMENT '商品ID',
    price DECIMAL NOT NULL COMMENT '商品单价',
    cnt INT NOT NULL COMMENT '下单数量'
) CHARACTER SET utf8 COLLATE utf8_bin;

INSERT INTO tb_product_info(product_id, shop_id, tag, in_price, quantity, release_time) VALUES
  (8001, 901, '零食', 60, 1000, '2020-01-01 10:00:00'),
  (8002, 901, '零食', 140, 500, '2020-01-01 10:00:00'),
  (8003, 901, '零食', 160, 500, '2020-01-01 10:00:00');

INSERT INTO tb_order_overall(order_id, uid, event_time, total_amount, total_cnt, `status`) VALUES
  (301001, 101, '2021-09-30 10:00:00', 140, 1, 1),
  (301002, 102, '2021-10-01 11:00:00', 235, 2, 1),
  (301011, 102, '2021-10-31 11:00:00', 250, 2, 1),
  (301003, 101, '2021-11-02 10:00:00', 300, 2, 1),
  (301013, 105, '2021-11-02 10:00:00', 300, 2, 1),
  (301005, 104, '2021-11-03 10:00:00', 170, 1, 1);

INSERT INTO tb_order_detail(order_id, product_id, price, cnt) VALUES
  (301001, 8002, 150, 1),
  (301011, 8003, 200, 1),
  (301011, 8001, 80, 1),
  (301002, 8001, 85, 1),
  (301002, 8003, 180, 1),
  (301003, 8002, 140, 1),
  (301003, 8003, 180, 1),
  (301013, 8002, 140, 2),
  (301005, 8003, 180, 1);

  # 问题：请统计零食类商品中复购率top3高的商品。

# 注：复购率指用户在一段时间内对某商品的重复购买比例，复购率越大，则反映出消费者对品牌的忠诚度就越高，也叫回头率
#       此处我们定义：某商品复购率 = 近90天内购买它至少两次的人数 ÷ 购买它的总人数
#       近90天指包含最大日期（记为当天）在内的近90天。结果中复购率保留3位小数，并按复购率倒序、商品ID升序排序


select 
 od.product_id as  product_id
 ,round((count(oo.uid)-count(distinct oo.uid))/NULLIF(count(distinct oo.uid),0),3) as  repurchase_rate
from tb_order_detail od 
join tb_order_overall oo on oo.order_id = od.order_id
join tb_product_info pi on pi.product_id = od.product_id
where 
    oo.event_time >= (SELECT DATE_SUB(MAX(event_time), INTERVAL 90 DAY) FROM tb_order_overall)
    and oo.status = 1
    and pi.tag = '零食'
group by od.product_id
order by repurchase_rate desc, product_id asc
limit 3;


==================================








