package main

import (
	"fmt"
	"math/rand"
	"os"
	"strconv"
	"time"
)

var fruits = [5]string{"pomme", "poire", "banane", "orange", "scoubidou"}

const CIBLE = 20
const N_PROD = 4
const N_CONS = 2

// QUESTION 1

func dort() {
	time.Sleep(time.Duration(rand.Intn(10)) * time.Millisecond)
}

func prod1(id int, c1 chan int, c2 chan string) {
	for i := id * CIBLE; i < CIBLE+id*CIBLE; i++ {
		fr := fruits[id]
		fmt.Println("Producteur", id, "envoie", fr, i)
		c1 <- i
		dort()
		c2 <- fr
	}
}

func cons1(id int, c1 chan int, c2 chan string, f chan int) {
	for i := 0; i < (N_PROD * CIBLE / N_CONS); i++ {
		k := <-c1
		fr := <-c2
		fmt.Println("Consommateur", id, "reçoit", fr, k)
	}
	f <- 0
}

func main1() {
	c1 := make(chan int)
	c2 := make(chan string)
	fin := make(chan int)
	for i := 0; i < N_PROD; i++ {
		go func(k int) { prod1(k, c1, c2) }(i)
	}
	for i := 0; i < N_CONS; i++ {
		go func(k int) { cons1(k, c1, c2, fin) }(i)
	}
	for i := 0; i < N_CONS; i++ {
		<-fin
	}
}

// QUESTION 2

type paquet struct {
	fruit  string
	entier int
}

func prod2(id int, c chan paquet) {
	for i := id * CIBLE; i < CIBLE+id*CIBLE; i++ {
		fr := fruits[id]
		fmt.Println("Producteur", id, "envoie", fr, i)
		c <- paquet{fruit: fr, entier: i}
	}
}

func cons2(id int, c chan paquet, f chan int) {
	for i := 0; i < (N_PROD * CIBLE / N_CONS); i++ {
		p := <-c
		fmt.Println("Consommateur", id, "reçoit", p.fruit, p.entier)
	}
	f <- 0
}

func main2() {
	c := make(chan paquet)
	fin := make(chan int)
	for i := 0; i < N_PROD; i++ {
		go func(k int) { prod2(k, c) }(i)
	}
	for i := 0; i < N_CONS; i++ {
		go func(k int) { cons2(k, c, fin) }(i)
	}
	for i := 0; i < N_CONS; i++ {
		<-fin
	}
}

// QUESTION 3

func prod3(id int, c chan chan interface{}) {
	mon_c := make(chan interface{})
	for i := id * CIBLE; i < CIBLE+id*CIBLE; i++ {
		fr := fruits[id]
		fmt.Println("Producteur", id, "envoie", fr, i)
		c <- mon_c
		mon_c <- i
		dort()
		mon_c <- fr
	}
}

func cons3(id int, c chan chan interface{}, f chan int) {
	for i := 0; i < (N_PROD * CIBLE / N_CONS); i++ {
		temp := <-c
		n := <-temp
		fr := <-temp
		fmt.Println("Consommateur", id, "reçoit", fr, n)
	}
	f <- 0
}

func main3() {
	c := make(chan chan interface{})
	fin := make(chan int)
	for i := 0; i < N_PROD; i++ {
		go func(k int) { prod3(k, c) }(i)
	}
	for i := 0; i < N_CONS; i++ {
		go func(k int) { cons3(k, c, fin) }(i)
	}
	for i := 0; i < N_CONS; i++ {
		<-fin
	}
}

// MAIN

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Format: exo4-1 <version du main>")
		return
	}
	version, _ := strconv.Atoi(os.Args[1])
	if version == 1 {
		main1()
	} else if version == 2 {
		main2()
	} else if version == 3 {
		main3()
	} else {
		fmt.Println("Mauvais numero de version")
	}
}
