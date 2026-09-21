package main

import (
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
	"log"
	"time"
)

type User struct {
	Name     string    `bson:"name"`
	Age      int       `bson:"age"`
	Email    string    `bson:"email"`
	CreateAt time.Time `bson:"create_at"`
}

func main() {
	if !bson.IsObjectIdHex("45") {
		log.Println("不是HEX？没搞明白。。。")
	}

	id := bson.ObjectIdHex("1f")
	log.Printf("id:%s", id)
}

func mgot() {
	// mongo 4.4 可以， 6.0不可能，应该需要更新mgo框架
	session, err := mgo.Dial("127.0.0.1:27017")
	if err != nil {
		log.Fatal(err)
	}
	defer session.Close()

	conllection := session.DB("zq").C("users")
	log.Println(conllection)

	user := User{
		Name:     "zhaoq",
		Age:      18,
		Email:    "1@2.com",
		CreateAt: time.Now(),
	}

	err = conllection.Insert(user)
	if err != nil {
		log.Fatal(err)
	}

	var result []User
	err = conllection.Find(nil).Sort("-create_at").Limit(10).All(&result)
	if err != nil {
		log.Fatal(err)
	}
	log.Println(result)

	user = User{
		Name:     "zhaoq",
		Age:      28,
		Email:    "1@2.com",
		CreateAt: time.Now(),
	}

	err = conllection.Update(bson.M{"name": "zhaoq"}, user)
	if err != nil {
		log.Fatal(err)
	}

	// err = conllection.Remove(bson.M{"name":"zhaoq"})
	// if err != nil {
	// 	log.Fatal(err)
	// }
}
