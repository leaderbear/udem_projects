#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "main.h"


typedef unsigned char byte;
typedef int error_code;

#define ERROR (-1)
#define HAS_ERROR(code) ((code) < 0)
#define HAS_NO_ERROR(code) ((code) >= 0)

/**
 * Cette fonction compare deux chaînes de caractères.
 * @param p1 la première chaîne
 * @param p2 la deuxième chaîne
 * @return le résultat de la comparaison entre p1 et p2. Un nombre plus petit que
 * 0 dénote que la première chaîne est lexicographiquement inférieure à la deuxième.
 * Une valeur nulle indique que les deux chaînes sont égales tandis qu'une valeur positive
 * indique que la première chaîne est lexicographiquement supérieure à la deuxième.
 */
int strcmp(char *p1, char *p2) {
    char *s1 = (char *) p1;
    char *s2 = (char *) p2;
    char c1, c2;
    do {
        c1 = (char) *s1++;
        c2 = (char) *s2++;
        if (c1 == '\0')
            return c1 - c2;
    } while (c1 == c2);
    return c1 - c2;
}

/**
 * Ex. 1: Calcul la longueur de la chaîne passée en paramètre selon
 * la spécification de la fonction strlen standard
 * @param s un pointeur vers le premier caractère de la chaîne
 * @return le nombre de caractères dans le code d'erreur, ou une erreur
 * si l'entrée est incorrecte
 */
error_code strlen2(char *s) {
    //If string is NULL throw error
    if (s == NULL) {
        return ERROR;
    }

    int len = 0;

    //Iterate over string char by char using pointer
    while (*s != '\0') {
        len++;
        s++;
    }

    return len;
}

/**
 * Ex.2 :Retourne le nombre de lignes d'un fichier sans changer la position
 * courante dans le fichier.
 * @param fp un pointeur vers le descripteur de fichier
 * @return le nombre de lignes, ou -1 si une erreur s'est produite
 */
error_code no_of_lines(FILE *fp) {
    //Start the stream at start of file
    long int streampos = ftell(fp);
    fseek(fp, 0, SEEK_SET);
    if(fp == NULL) {
        return -1;
    }

    int linecount = 0; //Line count
    char currentchar; //Read character from file
    char lastchar; //Previous char

    do {
        //Get next character in file
        currentchar = getc(fp);
        //If end of line, increment linecount
        if (currentchar == '\n') {
            linecount++;
        }
    }
    while (currentchar != EOF);
    fseek(fp, -1, SEEK_CUR);
    lastchar = getc(fp);
    //Return 0 if file is empty
    if (lastchar == EOF) {
        return 0;
    }
    //Add 1 to linecount since EOF doesn't count as \n
    if (lastchar != '\n') {
        linecount++;
    }

    //Reset cursor to start of file using fseek
    fseek(fp, streampos, SEEK_SET);

    return linecount;
}

/**
 * Ex.3: Lit une ligne au complet d'un fichier
 * @param fp le pointeur vers la ligne de fichier
 * @param out le pointeur vers la sortie
 * @param max_len la longueur maximale de la ligne à lire
 * @return le nombre de caractère ou ERROR si une erreur est survenue
 */
error_code readline(FILE *fp, char **out, size_t max_len) {
    if (fp == NULL) {
        return ERROR;
    }

    if (out == NULL) {
        return ERROR;
    }

    //Allocate max_len + 1 characters
    char *line = (char*) malloc(sizeof(char) * (max_len + 1));
    //Test if allocation is successful
    if (line == NULL) {
        return ERROR;
    }

    int num_chars = 0;
    int c = 0;

    //Read line and iterate over line
    while ((c=getc(fp)) != '\n' && num_chars < max_len) {
        if (c == EOF) {
            break;
        }
        //Add read char to allocated line
        line[num_chars] = (char)c;
        num_chars++;
    }

    //Free allocated memory if no line or allocate right value to out
    if (num_chars == 0) {
        free(line);
        *out = NULL;
        return 0;
    } else {
        line[num_chars] = '\0';
        *out = line;
        return num_chars;
    }
}

/**
 * Ex.4: Copie un bloc mémoire vers un autre
 * @param dest la destination de la copie
 * @param src  la source de la copie
 * @param len la longueur (en byte) de la source
 * @return nombre de bytes copiés ou une erreur s'il y a lieu
 */
error_code memcpy2(void *dest, void *src, size_t len) {
    if (dest == NULL || src == NULL) {
        return ERROR;
    }

    //Get memory pointer of source and destination
    char *src_cpy = src;
    char *dest_cpy = dest;
    int i = 0;
    //Iterate over memory block and overwrite destination by source
    while (i < len) {
        dest_cpy[i] = src_cpy[i];
        i++;
    }
    return i;
}

/**
 * Ex.5: Analyse une ligne de transition
 * @param line la ligne à lire
 * @param len la longueur de la ligne
 * @return la transition ou NULL en cas d'erreur
 */

/** Helper function for parse_line to find char (in our case ',') inside line **/
int find_char(char *line, int start, char target, size_t len) {
    for (int i = start; i < len; i++) {
        if (line[i] == target) {
            return i;
        }
    }
    return ERROR;
}

transition *parse_line(char *line, size_t len) {

    /** NOTE: THIS IMPLEMENTATION ASSUMES OUR LINE ALWAYS HAS THE RIGHT FORMAT: (current_state, read)->(next_state, write, movement) **/

    //If line is empty return null
    if (line == NULL) {
        return NULL;
    }
    if (len ==0) {
        return NULL;
    }

    /**********
     * STATES
     **********/

    //Current state
    //WE ASSUME CORRECT SYNTAX
    int current_state_start = 1;
    int current_state_end = find_char(line, current_state_start, ',', len);
    //Check that character has been found
    if (current_state_end == ERROR) {
        return NULL;
    }
    int current_state_length = current_state_end - current_state_start;
    //Each state has to be a new string that is malloced
    char *current_state = (char*) malloc(sizeof(char) * current_state_length + 1);
    //Test if allocation is successful
    if (current_state == NULL) {
        return NULL;
    }
    //Get current state from line
    memcpy2(current_state, &line[current_state_start], current_state_length);
    //Add terminator to end of state
    current_state[current_state_length] = '\0';

    int read_pos = current_state_end+1;

    //Next State
    //WE ASSUME CORRECT SYNTAX
    int next_state_start = read_pos + 5;
    int next_state_end = find_char(line, next_state_start, ',', len);
    //Check that character has been found
    if (next_state_end == ERROR) {
        return NULL;
    }
    int next_state_length = next_state_end - next_state_start;
    //Each state has to be a new string that is malloced
    char *next_state = (char*) malloc(sizeof(char) * next_state_length + 1);
    //Test if allocation is successful
    if(next_state == NULL) {
        return NULL;
    }
    //Get next state from line
    memcpy2(next_state, &line[next_state_start], next_state_length);
    //Add terminator to end of state
    next_state[next_state_length] = '\0';

    int write_pos = next_state_end+1;

    /**********
     * TRANSITION
     **********/

    //Allocate memory to transition
    transition *trans = (transition*) malloc(sizeof(transition));
    //Test if allocation is successful
    if (trans == NULL) {
        return NULL;
    }

    //Build transition structure
    trans->current_state = current_state;
    trans->next_state = next_state;
    trans->read = line[read_pos];
    trans->write = line[write_pos];

    //GRD movement
    //WE ASSUME CORRECT SYNTAX
    int movement_pos = write_pos + 2;
    //REST
    if (line[movement_pos] == 'R') {
        trans->movement = 0;
    }
    //RIGHT
    else if (line[movement_pos] == 'D') {
        trans->movement = 1;
    }
    //LEFT
    else if (line[movement_pos] == 'R') {
        trans->movement = -1;
    }

    return trans;
}

/**
 * Ex.6: Execute la machine de turing dont la description est fournie
 * @param machine_file le fichier de la description
 * @param input la chaîne d'entrée de la machine de turing
 * @return le code d'erreur
 */

/** Helper function for execute to free all allocated memory **/
void cleanup(char** inital_state, char** acc_state, char** rej_state, FILE* fp, transition** transitions, int num_trans, char* tape, char** cur_state, char **tmp_transition_line) {
    if (inital_state != NULL) {
        free(*inital_state);
    }
    free(inital_state);
    if (acc_state != NULL) {
        free(*acc_state);
    }
    free(acc_state);
    if (rej_state != NULL) {
        free(*rej_state);
    }
    free(rej_state);
    if (fp != NULL) {
        fclose(fp);
    }
    if (transitions != NULL) {
        transition *t;
        for (int i = 0; i < num_trans; i++) {
            t = transitions[i];
            if (t != NULL) {
                free(t->current_state);
                free(t->next_state);
            }
            free(t);
        }
    }
    free(transitions);
    free(tape);
    free(cur_state);
    free(tmp_transition_line);
    return;
}

error_code execute(char *machine_file, char *input) {

    //If no file or no input return error
    if (machine_file == NULL || input == NULL) {
        return ERROR;
    }

    // Define and initiate all variables first for cleanup helper function
    char **initial_state = NULL;
    char **acc_state = NULL;
    char **rej_state = NULL;
    FILE *file = NULL;
    transition **transitions = NULL;
    char **tmp_transition_line = NULL;
    char *tape = NULL;
    char **cur_state = NULL;
    int num_trans = 0;
    //As noted in the interlude
    int state_max = 5;

    file= fopen(machine_file, "r");
    if (file == NULL) {
        return ERROR;
    }
    int num_lines = no_of_lines(file);

    /** NOTE: ONCE AGAIN WE ASSUME THAT MACHINE_FILE WILL ALWAYS HAVE THE SAME SYNTAX **/
    //First 3 lines are always states in the followin order: initial state, accepting state, rejecting state
    initial_state = (char**)malloc(sizeof(char*));
    if (readline(file, initial_state, state_max) == ERROR) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    acc_state = (char**)malloc(sizeof(char*));
    if (readline(file, acc_state, state_max) == ERROR) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    rej_state = (char**)malloc(sizeof(char*));
    if (readline(file, rej_state, state_max) == ERROR) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }

    //Following lines in file will be transitions
    //As asked in the statement of ex.6, allocate array containing transitions using number of lines
    num_trans = num_lines - 3;
    transitions = (transition**)malloc(sizeof(transition*) * num_trans);
    if (transitions == NULL) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    tmp_transition_line = (char**)malloc(sizeof(char*));
    if (tmp_transition_line == NULL) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    for (int i = 0; i < num_trans; i++) {
        if (readline(file, tmp_transition_line, 100) == ERROR) {
            cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
            return ERROR;
        }
        transitions[i] = parse_line(*tmp_transition_line, 100);
        if (transitions[i] == NULL) {
            cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
            return ERROR;
        }
        free(*tmp_transition_line);
    }

    //Create and initiate tape
    int init_tape_len = strlen2(input);
    if (init_tape_len < 0) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    } else if (init_tape_len == 0) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return 1;
    }
    //As asked in the statement of ex.6, allocate at least twice the size of the initial string to our working tape
    int tape_len = init_tape_len * 2;
    tape = (char*)malloc(sizeof(char) * (tape_len + 1));
    if (tape == NULL) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    if (memcpy2(tape, input, init_tape_len) != init_tape_len) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    // We initialize all slots in the tape to an empty space
    for (int i = init_tape_len; i < tape_len; i++) {
        tape[i] = ' ';
    }
    tape[tape_len] = '\0';

    //Place head at the start of the tape
    int cur_pos = 0;
    cur_state = (char**)malloc(sizeof(char*));
    if (cur_state == NULL) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return ERROR;
    }
    //Write and execute tape
    *cur_state = *initial_state;
    while (strcmp(*cur_state, *acc_state) != 0 && strcmp(*cur_state, *rej_state) != 0) {
        int trans_index;
        //Find transition
        int trans_found = 0;
        for (int i = 0; i < num_trans; i++) {
            if (strcmp(transitions[i]->current_state, *cur_state) == 0 && transitions[i]->read == tape[cur_pos]) {
                trans_index = i;
                trans_found = 1;
            }
        }
        if (trans_found == 0) {
            cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
            return ERROR;
        }

        transition* next_trans = transitions[trans_index];
        //Write to tape
        tape[cur_pos] = next_trans->write;
        *cur_state = next_trans->next_state;
        if (next_trans->movement == -1) {
            cur_pos--;
            //We assume tape is "infinite" to the right
            if (cur_pos < 0) {
                cur_pos = 0;
            }
        } else if(next_trans->movement == 1) {
            cur_pos++;
            //Double tape size if needed
            if (cur_pos >= tape_len) {
                int old_tape_len = tape_len;
                tape_len = tape_len * 2;
                tape = (char*)realloc(tape, tape_len + 1);
                if (tape == NULL) {
                    cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
                    return ERROR;
                }
                for (int i = old_tape_len; i < tape_len; i++) {
                    tape[i] = ' ';
                }
                tape[tape_len] = '\0';
            }
        }
    }
    if (strcmp(*cur_state, *acc_state) == 0) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return 1;
    } else if (strcmp(*cur_state, *rej_state) == 0) {
        cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);
        return 0;
    }
    cleanup(initial_state, acc_state, rej_state, file, transitions, num_trans, tape, cur_state, tmp_transition_line);

    return ERROR;
}

// ATTENTION! TOUT CE QUI EST ENTRE LES BALISES ༽つ۞﹏۞༼つ SERA ENLEVÉ! N'AJOUTEZ PAS D'AUTRES ༽つ۞﹏۞༼つ

// ༽つ۞﹏۞༼つ

int main() {

    /** strlen2 tests **/
    char *str1 = "abcd";
    char *str2 = "abc";
    assert(strlen2(str1) == 4);
    assert(strlen2(str2) == 3);
    assert(strlen2(NULL) == ERROR);
    char *empty = "";
    assert(strlen2(empty) == 0);

    /** no_of_lines tests **/
    FILE *file1;

    file1 = fopen("./five_lines", "r");
    printf("Number of lines: %d\n", no_of_lines(file1));
    assert(no_of_lines(file1) == 5);

    file1 = fopen("./empty", "r");
    printf("Number of lines: %d\n", no_of_lines(file1));
    assert(no_of_lines(file1) == 0);

    file1 = fopen("./six_lines", "r");
    printf("Number of lines: %d\n", no_of_lines(file1));
    assert(no_of_lines(file1) == 6);
    fclose(file1);

    /** readline tests **/
    char **out = (char**) malloc(sizeof(char*));
    FILE *file2;

    file2 = fopen("./five_lines", "r");

    assert(readline(NULL, out, 20) == ERROR);
    assert(readline(file2, NULL, 20) == ERROR);

    assert(readline(file2, out, 20) == 8);
    printf("Read line: %s\n", *out);
    free(*out);

    assert(readline(file2, out, 20) == 8);
    printf("Read line: %s\n", *out);
    free(*out);

    assert(readline(file2, out, 20) == 10);
    printf("Read line: %s\n", *out);
    free(*out);

    assert(readline(file2, out, 20) == 9);
    printf("Read line: %s\n", *out);
    free(*out);

    assert(readline(file2, out, 20) == 9);
    printf("Read line: %s\n", *out);
    free(*out);

    file2 = fopen("./empty", "r");
    assert(readline(file2, out, 20) == 0);
    assert(*out == NULL);
    free(*out);

    fclose(file2);

    /** memcpy2 tests **/
    byte *a = malloc(sizeof(byte) * 100);
    byte *b = malloc(sizeof(byte) * 100);

    assert(memcpy2(NULL, b, 10) == ERROR);
    assert(memcpy2(a, NULL, 10) == ERROR);

    for (int i = 0; i < 100; ++i) {
        b[i] = 0;
        a[i] = 0;
    }
    for (int i = 0; i < 10; i++) {
        b[i] = i + 1;
    }

    assert(memcpy2(a, b, 10) == 10);

    for(int i=0; i<10; i++) {
        assert(a[i] == b[i]);
    }

    free(a);
    free(b);

    /** parse_line tests **/
    char *trans1 = "(q0,0)->(qR,0,D)\n";
    transition *t1 = parse_line(trans1, strlen2(trans1));
    assert(strcmp(t1->current_state, "q0") == 0);
    assert(strcmp(t1->next_state, "qR") == 0);
    assert(t1->read == '0');
    assert(t1->write == '0');
    assert(t1->movement == 1);
    printf("Transition is:\nCurrent State: %s\nNext State: %s\nRead: %c\nWrite: %c\nMove: %d\n", t1->current_state, t1->next_state, t1->read, t1->write, t1->movement);
    free(t1->current_state);
    free(t1->next_state);
    free(t1);

    char *trans2 = "\n";
    transition *t2 = parse_line(trans2, strlen2(trans2));
    assert(t2 == NULL);
    free(t2);

    /** execute tests **/
    assert(execute("./youre_gonna_go_far_kid", "STARING AT THE SUN") == -1);
    assert(execute("./has_five_ones", "101010101") ==  1);
    assert(execute("./fake_file", "101010101") == ERROR);
    assert(execute("./has_five_ones", "") == 1);
    assert(execute("./simple.txt", "100000") == 1);
    assert(execute("./simple.txt", "1") == 1);
    assert(execute("./simple.txt", "0") == 0);
    assert(execute("./power_len.txt", "0") == 1);

    return 0;
}

// ༽つ۞﹏۞༼つ