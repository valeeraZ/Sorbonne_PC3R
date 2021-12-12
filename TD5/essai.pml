chan ch = [0] of {int}

active proctype machin(){
    int x = 0
    loop:
        do
        :: x == 10 -> goto end
        :: x < 10 -> x = x + 1; ch!x
        od
    end:
        printf("fini\n")
}

active proctype obs(){
    int res
    do
    :: ch?res -> printf("Recu %d \n", res)
    od
}