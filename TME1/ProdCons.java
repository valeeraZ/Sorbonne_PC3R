public class ProdCons {
    static String produits[] = { "pomme", "orange", "peche", "ananas", "banane" };
    static int cible_production = 3;
    static int nb_prod = 5;
    static int nb_con = 4;
    static int size_tapis = 15;
    static int cpt = cible_production * nb_prod;

    class Paquet {
        private String name;

        Paquet(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    class Tapis {
        private Paquet[] fifo;
        private int allocsize;
        private int sz;
        private int begin;

        public Tapis(int allocsize) {
            this.allocsize = allocsize;
            this.begin = 0;
            this.sz = 0;
            this.fifo = new Paquet[allocsize];
        }

        private boolean empty() {
            return this.sz == 0;
        }

        private boolean full() {
            return this.sz == this.allocsize;
        }

        public Paquet defiler() throws InterruptedException {
            synchronized (this) {
                while (this.empty())
                    this.wait();
                Paquet p = fifo[begin];
                sz--;
                begin = (begin + 1) % allocsize;
                cpt--;
                this.notify();
                return p;
            }

        }

        public void enfiler(Paquet p) throws InterruptedException {
            synchronized (this) {
                while (full())
                    this.wait();
                fifo[(begin + sz) % allocsize] = p;
                sz++;
                this.notify();
            }
        }
    }

    class Prod implements Runnable {
        private String nomProduit;
        private int cibleProduction;
        private int nbProduction;
        private Tapis tapis;

        public Prod(String nomProduit, int cibleProduction, ProdCons.Tapis tapis) {
            this.nomProduit = nomProduit;
            this.cibleProduction = cibleProduction;
            this.nbProduction = 0;
            this.tapis = tapis;
        }

        @Override
        public void run() {
            while (this.cibleProduction != this.nbProduction) {
                Paquet p = new Paquet(this.nomProduit + " " + this.nbProduction);
                try {
                    this.tapis.enfiler(p);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.nbProduction++;
            }
        }
    }

    class Cons implements Runnable {
        private int id;
        private Tapis tapis;

        public Cons(int id, ProdCons.Tapis tapis) {
            this.id = id;
            this.tapis = tapis;
        }

        @Override
        public void run() {
            while (cpt > 0) {
                try {
                    Paquet p = this.tapis.defiler();
                    System.out.println("C" + this.id + " mange " + p.getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProdCons prodCons = new ProdCons();
        ProdCons.Tapis tapis = prodCons.new Tapis(size_tapis);
        Thread[] prods = new Thread[nb_prod];
        Thread[] cons = new Thread[nb_con];
        for (int i = 0; i < nb_prod; i++) {
            Thread prod = new Thread(prodCons.new Prod(produits[i], cible_production, tapis));
            prods[i] = prod;
            prod.start();
        }

        for (int j = 0; j < nb_con; j++) {
            Thread conso = new Thread(prodCons.new Cons(j, tapis));
            cons[j] = conso;
            conso.start();
        }

        int i = 0;
        while (i < nb_prod) {
            try {
                prods[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }

        int j = 0;
        while (j < nb_con) {
            try {
                cons[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
        }
    }

}