package main

import (
	"log"
	"strconv"
)

// 在for 循环中， return 和 break 效果是一样的
func Hello() {
	var j int = 0
	var i int = 1

	// 这种情况下，for 后面不加小括号
	// for (let i := 1; i < 32; i++) {
	for ; i < 32; i++ {
		log.Println("fun i:" + strconv.Itoa(i))
		if i%4 == 0 {
			// 这里return 和不return 有什么区别？
			log.Println("fun2 i:" + strconv.Itoa(i))
			// return
			break
		}

		j++
		log.Println("fun j:" + strconv.Itoa(j))
	}

	// j = 20
	// if (j < 30 ){
	//    log.Println("nothing" + strconv.Itoa(j))
	// }
}
