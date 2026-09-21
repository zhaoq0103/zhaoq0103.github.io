
docker 安装mysql并启动：
docker run -itd --name mysql8 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql





select
  up.university,
  count(*) / distinct qpd.device_id
  avg(avg_answer_cnt) as avg_answer_cnt
from
  question_practice_detail qpd
  join user_profile up 
on qpd.device_id = up.device_id
group by up.university 



select
  up.university,
  count(qpd.device_id) / count(distinct qpd.device_id) as avg_answer_cnt
from
  question_practice_detail qpd
  join user_profile up 
on qpd.device_id = up.device_id
group by up.university


山东大学的用户在不同难度下的平均答题题目数

select
  up.university,
  qd.difficult_level,
  count(qpd.question_id) / count(distinct qpd.device_id) as avg_answer_cnt
from
  question_practice_detail qpd
  join user_profile up on qpd.device_id = up.device_id
  join question_detail qd on qpd.question_id = qd.question_id
where up.university = '山东大学'
group by up.university,qd.difficult_level



sql case具有两种格式，简单Case函数和Case搜索函数
注意case时，group by 的对象, case else ,if等

select
  case
    when (age is null or  age < 25) then "25岁以下"
    when age >= 25 then "25岁及以上"
  end as age_cut,
  count(*) as number
from
  user_profile
group by
  age_cut   -- 

select
  case when age >= 25 then "25岁及以上" else "25岁以下"
  end as age_cut,
  count(*) as number
from
  user_profile
group by
  age_cut   -- 


select
  if(age >=25, "25岁及以上", "25岁以下") as age_cut,
  count(*) as number
from
  user_profile
group by
  age_cut




select
   device_id, gender,
  case
    when age < 20 then "20岁以下"
    when (age >= 20 and age < 25) then "20-24岁"
    when (age >= 25) then "25岁及以上"
    else "其他"
  end as age_cut
from
  user_profile


2021年8月每天用户练习题目的数量
SELECT
    day(date),
    count(question_id)
from question_practice_detail
where month(date) = 08
group by day(date)



select * from question_practice_detail a jion question_practice_detail b
on a.id = b.id where a.device_id = b.device_id and month(a.date) = month(b.date) and day(a.date)+1 = day(b.date)




select
  university,
  avg(avg_answer_cnt) as avg_answer_cnt
from
  user_profile
group by
  university
order by university ASC;




select count（*) as male_num , avg(gpa) as avg_gpa,from user_profile where gender = 'male'