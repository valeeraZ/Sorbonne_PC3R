package main

import (
	"fmt"
	"math/rand"
	"os"
	"strconv"
	"time"
)

const PAQUETS = 100
const TAILLE = 5

func main1() {
	comm := make(chan int)
	fini := make(chan int)
	go func() {
		for i := 0; i < PAQUETS; i++ {
			paq := rand.Intn(100)
			comm <- paq
			fmt.Println("Produit evnoyé : ", paq)
		}
	}()
	go func() {
		for j := 0; j < PAQUETS; j++ {
			recu := <-comm
			time.Sleep(200 * time.Millisecond)
			fmt.Println("Paquet", j, "recu :", recu)
		}
		fini <- 0
	}()
	<-fini
}

func main2() {
	comm := make(chan int, TAILLE)
	fini := make(chan int)
	go func() {
		for i := 0; i < PAQUETS; i++ {
			paq := rand.Intn(100)
			comm <- paq
			fmt.Println("Produit evnoyé : ", paq)
		}
	}()
	go func() {
		for j := 0; j < PAQUETS; j++ {
			recu := <-comm
			time.Sleep(200 * time.Millisecond)
			fmt.Println("Paquet", j, "recu :", recu)
		}
		fini <- 0
	}()
	<-fini
}

func main3() {
	in := make(chan int)
	out := make(chan int)
	fini := make(chan int)
	go func() {
		for i := 0; i < PAQUETS; i++ {
			paq := rand.Intn(100)
			in <- paq
			fmt.Println("Produit evnoyé : ", paq)
		}
	}()
	go func() {
		for j := 0; j < PAQUETS; j++ {
			recu := <-out
			time.Sleep(200 * time.Millisecond)
			fmt.Println("Paquet", j, "recu :", recu)
		}
		fini <- 0
	}()
	go func() {
		nombre := 0
		tableau := make([]int, 0)
		for {
			fmt.Println("Il y a ", nombre, " élements dans le tableau.")
			switch {
			case nombre == 0:
				paq := <-in
				tableau = append(tableau, paq)
				nombre++
			case nombre == TAILLE:
				paq := tableau[0]
				out <- paq
				tableau = tableau[1:]
				nombre--
			default:
				paq := tableau[0]
				select {
				case npaq := <-in:
					tableau = append(tableau, npaq)
					nombre++
				case out <- paq:
					tableau = tableau[1:]
					nombre--
				}
			}
		}
	}()
	<-fini
}

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Format: prodcons1 <version de main>")
		return
	}
	version, _ := strconv.Atoi(os.Args[1])
	if version == 1 {
		main1()
	} else if version == 2 {
		main2()
	} else if version == 3 {
		main3()
	}
}
