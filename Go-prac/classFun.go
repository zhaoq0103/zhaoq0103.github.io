package main

import (
	"fmt"
	"time"
)

//----------------------------------------
/* 定义结构体 */
/* struct tag
https://github.com/golang/go/wiki/Well-known-struct-tags
*/

type Circle struct {
	radius float64 `default:"1.0"`
}

// 该 method 属于 Circle 类型对象中的方法
func (p Circle) getArea() float64 {
	//c.radius 即为 Circle 类型对象中的属性
	p.radius = 5
	return 3.14 * p.radius * p.radius
}

func (p *Circle) getAreeEx() float64 {
	//c.radius 即为 Circle 类型对象中的属性
	p.radius = 20
	return 3.14 * p.radius * p.radius
}

// ----------------------------------------
type Age uint

func (a Age) say() {
	fmt.Println("this persion age:", a)
}

func (age *Age) Modify() {
	*age = 28
}

func (age *Age) modifyEx() {
	*age = 99
}

// ----------------------------------------
type Person struct {
	age int
}

func (a Person) say() {
	// a.age = 18
	fmt.Println("Persion age:", a.age)
}

func (pp Person) Modify() {
	pp.age = 28
	fmt.Println("In Modify Persion age:", pp.age)
}

func (p *Person) modifyEx() int {
	p.age = 99
	return p.age + 1
}

// ----------------------------------------
func say(s string) {
	for i := 0; i < 5; i++ {
		time.Sleep(100 * time.Millisecond)
		fmt.Println(s)
	}
}

func sum(s []int, c chan int) {
	sum := 0
	for _, v := range s {
		sum += v
	}
	c <- sum // 把 sum 发送到通道 c
}

func fibonacci(n int, c chan int) {
	x, y := 0, 1
	for i := 0; i < n; i++ {
		c <- x
		x, y = y, x+y
	}
	close(c)
}

func ClassFunT() {

	// c := make(chan int, 10)
	// // cap(c) 计算通道长度
	// go fibonacci(cap(c), c)
	// // range 函数遍历每个从通道接收到的数据，因为 c 在发送完 10 个
	// // 数据之后就关闭了通道，所以这里我们 range 函数在接收到 10 个数据
	// // 之后就结束了。如果上面的 c 通道不关闭，那么 range 函数就不
	// // 会结束，从而在接收第 11 个数据的时候就阻塞了。
	// for i := range c {
	//         fmt.Printf(" %d",i)
	// }
	// fmt.Println("")

	var c1 Circle
	// c1.radius = 1.00
	ptrc1 := &c1
	fmt.Println("圆的面积 = ", ptrc1.getArea())

	// s := []int{7, 2, 8, -9, 4, 0, 2}

	// c := make(chan int)
	// go sum(s[:len(s)/2], c)
	// go sum(s[len(s)/2:], c)
	// x, y := <-c, <-c // 从通道 c 中接收

	// fmt.Println(x, y, x+y)

	// go say("world")
	// say("hello")

	// map1 := make(map[int]float32)
	// map1[1] = 1.0
	// map1[2] = 2.0
	// map1[3] = 3.0
	// map1[4] = 4.0

	// // 读取 key 和 value
	// for key, value := range map1 {
	//   fmt.Printf("key is: %d - value is: %f\n", key, value)
	// }

	// // 读取 key
	// for key := range map1 {
	//   fmt.Printf("key is: %d\n", key)
	// }

	// // 读取 value
	// for _, value := range map1 {
	//   fmt.Printf("value is: %f\n", value)
	// }

	// for value := range map1 {
	//   fmt.Printf("value is: %d\n", value)
	// }

	// for vvvvv := range map1 {
	//   fmt.Printf("value is: %d\n", vvvvv)
	// }

	// for key := range map1 {
	//   fmt.Printf("value is: %d\n", key)
	// }
}

// func main() {

// pa := Age(20)
// pa.say()

// pa.Modify()
// pa.say()

// pa.modifyEx()
// pa.say()

// pa := Person{10}
// pa.say()

// pa.Modify()
// pa.say()

// pb := &pa
// // pb := &Person{20}
// viewage := pb.modifyEx()
// fmt.Printf("你看起来 %d 岁了。。\n", viewage)
// pb.say()

// var c1 Circle
// c1.radius = 1.00
// ptrc1 := &c1
// fmt.Println("圆的面积 = ", c1.getArea())
// fmt.Println("圆的面积2 = ", ptrc1.getArea())
// fmt.Println("圆的面积Ex = ", ptrc1.getAreeEx())

// fmt.Println("///// =============== ")

// var c2 =  Circle{10.0}
// ptr, ptr2 := &c2, &c2
// fmt.Println("圆的面积 = ", c2.getArea())
// fmt.Println("圆的面积2 = ", ptr.getArea())
// fmt.Println("圆的面积Ex = ", ptr2.getAreeEx())
// }
