package main

import "fmt"

const NB_C int = 10
const NB_A int = 10

func additionneur(imp chan int, id int) {
	for {
		imp <- 0
		fmt.Println("Add n°", id, "incremente.")
	}
}

func compteur(imp chan int, fin chan int) {
	var compt int
	compt = 0
	for compt < NB_C*NB_A {
		<-imp
		compt = compt + 1
		fmt.Println("Le compteur est à ", compt)
	}
	fmt.Println("Compteur au max")
	fin <- 0
}

func main() {
	fini := make(chan int)
	impulsion := make(chan int)
	for i := 0; i < NB_C; i++ {
		go func(k int) { additionneur(impulsion, k) }(i)
	}
	go func() { compteur(impulsion, fini) }()
	<-fini
	fmt.Println("Fin du main.")
}
