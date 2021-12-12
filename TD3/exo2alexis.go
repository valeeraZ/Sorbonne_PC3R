package main

import (
	"fmt"
)

func routine(vazy chan int, synchro chan int, phrase string) {
	for true {
		<-vazy
		fmt.Println(phrase)
		synchro <- 0
	}
}

func ordonnanceur(vazy chan int, synchro chan int, fin chan int) {
	for j := 0; j < 100; j++ {
		for i := 0; i < 5; i++ {
			vazy <- 0
		}
		for i := 0; i < 5; i++ {
			<-synchro
		}
	}
	fin <- 0
}

func main() {
	v := make(chan int)
	s := make(chan int)
	fin := make(chan int)
	go func() { routine(v, s, "belle Marquise") }()
	go func() { routine(v, s, "vos beaux yeux") }()
	go func() { routine(v, s, "me font") }()
	go func() { routine(v, s, "mourir") }()
	go func() { routine(v, s, "d'amour") }()
	go func() { ordonnanceur(v, s, fin) }()
	<-fin
}
