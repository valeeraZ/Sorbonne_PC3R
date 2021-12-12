package main

import (
	"bufio"
	"fmt"
	"math/rand"
	"net"
	"os"
	"strconv"
	"strings"

	st "../client/structures"
	tr "./travaux"
)

var ADRESSE = "localhost"

var pers_vide = st.Personne{Nom: "", Prenom: "", Age: 0, Sexe: "M"}

var id_serv = make(map[int] * personne_serv)

// type d'un paquet de personne stocke sur le serveur, n'implemente pas forcement personne_int (qui n'existe pas ici)
type personne_serv struct {
	statut string
	afaire []func(st.Personne) st.Personne
	st.Personne
}

// cree une nouvelle personne_serv, est appelé depuis le client, par le proxy, au moment ou un producteur distant
// produit une personne_dist
func creer(id int) *personne_serv{
	pers := pers_vide
	tableau_afaire := make([]func (st.Personne) st.Personne, 0)
	new_pers_serv := personne_serv{statut: "V", afaire: tableau_afaire, Personne: pers}
	id_serv[id] = &new_pers_serv
	return &new_pers_serv
}

// Méthodes sur les personne_serv, on peut recopier des méthodes des personne_emp du client
// l'initialisation peut être fait de maniere plus simple que sur le client
// (par exemple en initialisant toujours à la meme personne plutôt qu'en lisant un fichier)
func (p *personne_serv) initialise() {
	p.Personne = st.Personne{Prenom: "StarPlatinum", Nom: "TheWorld", Sexe: "M", Age: 18}
	for i := 0; i <= rand.Intn(6); i++{
		p.afaire = append(p.afaire, tr.UnTravail())
	}
	p.statut = "R"
}

func (p *personne_serv) travaille() {
	p.Personne = p.afaire[0](p.Personne)
	p.afaire = p.afaire[1:]
	if len(p.afaire) == 0{
		p.statut = "C"
	}
}

func (p *personne_serv) vers_string() string {
	var add string
	if p.Sexe == "F" {
		add = "Mme "
	}else {
		add = "M "
	}
	return fmt.Sprint(add, p.Prenom, " ",p.Nom, " : ", p.Age, " ans. ")
}

func (p *personne_serv) donne_statut() string {
	return p.statut
}

// Goroutine qui maintient une table d'association entre identifiant et personne_serv
// il est contacté par les goroutine de gestion avec un nom de methode et un identifiant
// et il appelle la méthode correspondante de la personne_serv correspondante
func mainteneur(methode string, id int, retourChan chan string) {
	switch methode {
	case "creer":
		creer(id)
		retourChan <- "ok"
	case "initialise":
		id_serv[id].initialise()
		retourChan <- "ok"
	case "travaille":
		id_serv[id].travaille()
		retourChan <- "ok"
	case "vers_string":
		retourChan <- id_serv[id].vers_string()
	case "donne_statut":
		retourChan <- id_serv[id].donne_statut()
	}
}

// Goroutine de gestion des connections
// elle attend sur la socketi un message content un nom de methode et un identifiant et appelle le mainteneur avec ces arguments
// elle recupere le resultat du mainteneur et l'envoie sur la socket, puis ferme la socket
func gere_connection(conn net.Conn) {
	for{
		message, _ := bufio.NewReader(conn).ReadString('\n')
		requete := strings.TrimSuffix(message, "\n")
		if len(requete) < 1{
			break
		}
		args := strings.Split(requete, ",")
		fmt.Println("Requête reçu de client: " + requete)
		id, _ := strconv.Atoi(args[0])
		methode := args[1]
		reponseChan := make(chan string)
		go func() {
			mainteneur(methode, id, reponseChan)
		}()
		reponse := <- reponseChan
		conn.Write([]byte(reponse + "\n"))
	}
}

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Format: client <port>")
		return
	}
	port, _ := strconv.Atoi(os.Args[1]) // doit être le meme port que le client
	addr := ADRESSE + ":" + fmt.Sprint(port)
	// A FAIRE: creer les canaux necessaires, lancer un mainteneur
	ln, _ := net.Listen("tcp", addr) // ecoute sur l'internet electronique
	fmt.Println("Ecoute sur", addr)
	for {
		conn, _ := ln.Accept() // recoit une connection, cree une socket 
		fmt.Println("Accepte une connection.")
		go gere_connection(conn) // passe la connection a une routine de gestion des connections
	}
}
