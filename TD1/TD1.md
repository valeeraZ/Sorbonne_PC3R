## Ex 2.1

- 1

    ```c
    int main(){
        int v = 42;
        if (fork() == 0){
            sleep(1);
            printf("Je suis le fils, v vaut %d \n", v);
        }else{
            v = 38;
            printf("Je suis le père, v vaut %d \n", v);
            sleep(5);
        }
    }
    ```
    `fork()` fait une copie.  
    Je suis le pere, v vaut 38  
    Je suis le fils, v vaut 42

- 2

    ```c
    int v = 0;
    pthread_t th1;

    void *fun_fils(){
        sleep(1);
        printf("Je suis le fils, v vaut %d \n");
    }

    int main(){
        v = 42;
        pthread_create(&th1, NULL, fun_fils, NULL);
        v = 38;
        printf("Je suis le pere, v vaut %d \n");
        sleep(4);
    }
    ```
    `thread` partage les variables globales.  
    Je suis le pere, v vaut 38  
    Je suis le fils, v vaut 38
- 3
    - Thread: léger, partagent une mémoire, ordonnancés par l'application
    - Processus: lourd, ont leur propre mémoire, ordonnancés par le système d'exploitation

## Ex 2.2

```java
class Jourdain implements Runnable{
    int id;
    Jourdain(i){
        id = i;
    }

    run(){
        switch id{
            case 1{
                System.out.println("belle marquise ");
                break;
            }
            case 2{
                //...
            }
        }
    }
}

public class main(String[] args){
    Jourdain j1 = new Jourdain(1);
    Jourdain j2 = new Jourdain(2);
    //...
    j1.start();
    j2.start();
    //...
}

```

```Rust
fn poeme(num: i32) -> i32{
    match num{
        1 => println!("Belle marquise ")
        2 => println!("Vos beaux yeux ")
    };
    num
}

fn main(){
    let mut handles = Vec :: new();
    for i in 1..6{
        handles.push(std::thread::spawn(
            move || poeme(i)
        ))
    }
    for h in handles {
        h.join().unwrap();
    }
}
```

## 2.3

```c

#define NP_THREAD 10
int SHARED_compteur;

void* routine(void* arg){
	int temp = SHARED_compteur;
	sched_yield();
	temp++;
	sched_yield();
	SHARED_compteur = temp;
	printf("Compteur = %d\n", SHARED_compteur); 
}

int main()
{	
	SHARED_compteur = 0;
	pthread_t thread[NP_THREAD];
	for(int i = 0;i<NP_THREAD;i++)
		pthread_create(&thread[i], NULL, routine, NULL);

	for(int i = 0;i<NP_THREAD;i++)
		pthread_join(thread[i], NULL);

	if(NP_THREAD == SHARED_compteur)
		printf("TERMINE\n");
	
}
```