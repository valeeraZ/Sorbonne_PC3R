package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"
)

const NB_TRAVAILLERUS = 5

type paquet struct{
	arrivee string
	depart string
	//temps d'arrêt en secondes, initilaise à 0
	arret int
}

type requete struct{
	// le paquet reçu
	recu paquet
	// un canal privé pour retrouner au travailleur correspondant
	retour chan paquet
}

func lecteur (donneesChan chan string, file_name string){
	file, err := os.Open(file_name)
	if err != nil{
		panic(err)
	}
	//fermer le fichier après "return"
	defer file.Close()
	scanner := bufio.NewScanner(file)
	//sauter 1 ligne
	scanner.Scan()
	for scanner.Scan(){
		str := scanner.Text() 
		donneesChan <- str
	}
}

func travailleur(donneesChan chan string, serveurChan chan requete, redacteurChan chan paquet){
	for {
		donnees := <- donneesChan
		go func(s string){
			params := strings.Split(s, ",")

			p := paquet{arrivee: params[1], depart: params[2], arret: 0}
			priveRetourChan := make(chan paquet)

			serveurChan <- requete{recu: p, retour: priveRetourChan}

			//attente de recevoir le paquet modifié; bloquant
			paquetRetour := <- priveRetourChan
			redacteurChan <- paquetRetour
		}(donnees)
		
	}
}

func serveur(serveurChan chan requete){
	for{
		req := <- serveurChan
		go func(r requete){
			arrivee,_ := time.Parse("15:04:05", r.recu.arrivee)
			depart,_ := time.Parse("15:04:05", r.recu.depart)
			r.recu.arret = int(depart.Sub(arrivee).Seconds())
			res := r.recu
			r.retour <- res
		}(req)
	}
}

func redacteur(redacteurChan chan paquet, mainChan chan int){
	counter := 0
	arretTotal := 0
	for{
		select {
			case paquet := <- redacteurChan:
				counter ++
				arretTotal += paquet.arret
			case <- mainChan:
				moyenne := arretTotal / counter
				fmt.Println("Temps d'arrêt Total: ", arretTotal)
				fmt.Println("Nombre de trains: ", counter)
				mainChan <- moyenne
				return
		}
	}
}

func main(){
	if len(os.Args) < 2 {
		fmt.Println("Usage: <Time_Waiting>")
		return
	}

	donneesChan := make(chan string)
	serveurChan := make(chan requete)
	redacteurChan := make(chan paquet)
	mainChan := make(chan int)

	go func(){
		lecteur(donneesChan, "./stop_times.txt")
	}()
	
	for i := 0; i < NB_TRAVAILLERUS; i++{
		go func(){
			travailleur(donneesChan, serveurChan, redacteurChan)
		}()
	}

	go func() {
		serveur(serveurChan)
	}()

	go func(){
		redacteur(redacteurChan, mainChan)
	}()
	
	tempsAttente, _ := strconv.Atoi(os.Args[1])
	time.Sleep(time.Duration(tempsAttente) * time.Millisecond)

	mainChan <- 0
	resultat := <- mainChan
	fmt.Println("Temps d'arrêt en moyenne: ", resultat)

}