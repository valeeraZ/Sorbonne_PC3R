package main

import "fmt"

const NB_C int = 10
const NB_A int = 10

func additionneur(comm chan int, id int, fin chan int) {
	for i := 0; i < NB_A; i++ {
		temp := <-comm
		temp = temp + 1
		fmt.Println("Add n°", id, "incremente compteur à", temp)
		comm <- temp
	}
	fin <- 0
}

func main() {
	fini := make(chan int)
	compteur := make(chan int)
	for i := 0; i < NB_C; i++ {
		go func(k int) { additionneur(compteur, k, fini) }(i)
	}
	compteur <- 0
	for i := 0; i < NB_C-1; i++ {
		<-fini
	}
	res := <-compteur
	<-fini
	fmt.Println("Fin du main, compteur:", res)
}
