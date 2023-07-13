#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <memory.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <ctype.h>
#include <string.h>

#define true 1
#define false 0
#define bool int

typedef int error_code;

#define ERROR (-1)
#define HAS_ERROR(code) ((code) < 0)
#define NULL_TERMINATOR '\0'

#define MAX_INPUT_LENGTH (4096)

enum op {   //todo these are your custom shell operators. You might want to use them to represent &&, ||, & and "no operator"
    BIDON, NONE, OR, AND, ALSO    //BIDON is just to make NONE=1, BIDON is unused
};

struct command {    //todo you might want to use this to represent the different commands you find on a line
    char **call;
    enum op operator;
    struct command *next;
    int index;
    int count;
    bool also;
};
//hint hint: this looks suspiciously similar to a linked list we saw in the demo. I wonder if I could use the same ideas here??
/** Commands linked list **/
struct commands {
    struct command* head;
    bool background;
};

struct child_process {
    pid_t id;
    struct child_process* next;
};

/** Child process init **/
struct child_process* create_child(int id) {
    struct child_process* child = (struct child_process*)malloc(sizeof(struct child_process));
    child->id = id;
    child->next = NULL;
    return child;
}

struct children {
    struct child_process* head;
};

//Modified to add size of array
void freeStringArray(char **arr, int size) {  //todo probably add this to free the "call" parameter inside of command
    if (arr != NULL) {
        for (int i = 0; i < size; i++) {
            free(arr[i]);
        }
    }
    free(arr);
}

/** Function for ex.2 AND/OR OPS **/
bool endswith_operator(char* string) {
    int len = strlen(string);
    //Check if operator is at end of line
    if (len > 2 && strcmp(string + len - 2, "||") == 0) {
        return 2;
    } else if (len > 2 && strcmp(string + len - 2, "&&") == 0) {
        return 2;
    } else if (len > 1 && strcmp(string + len - 1, "&") == 0 && strcmp(string, "&&") != 0) {
        return 1;
    } else if (len > 1 && strcmp(string + len - 1, "|") == 0 && strcmp(string, "||") != 0) {
        return 1;
    }
    return 0;
}

/** Function to allocate memory to parsed string **/
char* my_strdup(char *src) {
    int len = strlen(src);
    char* str = (char*)malloc(sizeof(char) * (len +1));
    strcpy(str, src);
    return str;
}

/** Helper function for readLine **/
char** parse_command_line(char* line, int length) {
    if (line == NULL) {
        return NULL;
    }
    //Initiate and allocate array to store read line
    char** command_line = malloc(sizeof(char*) * length);
    for (int i = 0; i < length; i++) {
        command_line[i] = NULL;
    }
    int i = 0;
    int op_size = 0;

    //strtok breaks string into a series of tokens according to delimeters
    char delims[] =" \t\r\n\v\f";
    command_line[i] = strtok(line, delims);
    while (command_line[i] != NULL) {
        //Allocate each line
        command_line[i] = my_strdup(command_line[i]);
        if ((op_size = endswith_operator(command_line[i])) > 0) {
            command_line[i+1] = (char*)malloc(sizeof(char) * (op_size + 1));
            int len = strlen(command_line[i]);
            strncpy(command_line[i+1], command_line[i] + (len - op_size), op_size);
            command_line[i][len-op_size] = '\0';
            command_line[i+1][op_size] = '\0';
            i++;
        }
        command_line[++i] = strtok(NULL, delims);
    }
    return command_line;
}

/** Helper function for readLine, parses command **/
struct command* parse_command(char **command_line, int start) {
    if (command_line == NULL || command_line[start] == NULL) {
        return NULL;
    }
    //Identify command type
    int i = start;
    enum op type = NONE;
    while (command_line[i] != NULL) {
        if (strcmp(command_line[i], "||") == 0) {
            type = OR;
            break;
        } else if (strcmp(command_line[i], "&&") == 0) {
            type = AND;
            break;
        } else if (strcmp(command_line[i], "&") == 0) {
            type = ALSO;
            break;
        }
        i++;
    }

    int size = i - start;
    char** command_arr = (char**)malloc(sizeof(char*)*(size + 1));
    if (command_arr == NULL) return NULL;
    for (int j = 0; j < size; j++) {
        command_arr[j] = command_line[j + start];
    }
    command_arr[size] = NULL;

    int count = parse_special_function(command_arr, size);

    struct command* c = (struct command*)malloc(sizeof(struct command));
    if (c == NULL) return NULL;
    c->operator = type;
    c->call = command_arr;
    c->index = size;
    c->count = count;
    c->next = NULL;
    return c;
}

/** Helper function for readline, iterates over a commandline to parse all commands **/
struct commands* parse_commands(char **command_line) {
    if (command_line == NULL || command_line[0] == NULL) {
        return NULL;
    }
    int position = 0;
    struct command *head = parse_command(command_line, position);
    if (head == NULL) return NULL;

    struct command *cur = head;
    struct command *prev = head;
    while (cur != NULL && cur->operator != NONE && cur->operator != ALSO) {
        position = position + cur->index + 1;
        cur->next = parse_command(command_line, position);
        prev = cur;
        cur = cur->next;
    }

    struct commands *commands = (struct commands*)malloc(sizeof(struct commands));
    if (commands == NULL) {
        return NULL;
    }
    commands->head = head;
    commands->background = (prev->operator == ALSO) || (cur != NULL && cur->operator == ALSO);
    return commands;
}

/** Implementation of rN **/
int parse_special_function(char** command_arr, int num_cmds) {
    int count = 1;
    char* rN = command_arr[0];
    int len_rN = strlen(rN);
    if(len_rN > 2 && rN[0] == 'r' && isdigit(rN[1])) {
        int num_end_idx = -1;
        for (int i = 1; i < len_rN; i++) {
            if (rN[i] == '(') {
                num_end_idx = i - 1;
                break;
            } else if (!isdigit(rN[i])) {
                return count;
            }
        }
        if (num_end_idx > 0) {
            rN++;
            rN[num_end_idx] = '\0';
            num_end_idx--;
            count = atoi(rN);
            rN += num_end_idx + 2;
            command_arr[0] = rN;

            for(int i = 0; i < num_cmds; i++) {
                int l = strlen(command_arr[i]);
                if (command_arr[i][l - 1] == ')') {
                    if (i != num_cmds - 1) {
                        return 1;
                    }
                    command_arr[i][l - 1] = '\0';
                    break;
                }
            }
        }
    }
    return count;
}

void free_commands(struct commands* commands) {
    if (commands != NULL) {
        struct command* cur = commands->head;
        struct command* prev = NULL;
        while (cur) {
            if (cur->call != NULL) {
                free(cur->call);
            }
            prev = cur;
            cur = cur->next;
            free(prev);
        }
        free(commands);
    }
}

void free_children(struct children* children, bool wait) {
    if (children != NULL) {
        struct child_process* c = children->head;
        struct child_process* tmp = NULL;
        int status;
        while (c != NULL) {
            if (wait) {
                waitpid(c->id, &status, WUNTRACED);
            }
            tmp = c;
            c = c->next;
            free(tmp);
        }
        free(children);
    }
}

/** Exec function to run a commands from a line in a process **/
void run_loop(struct commands* commands, struct children* children, char* line, char** tokens) {
    pid_t pid;
    int exec_error;
    int proc_status;
    bool failed = false;
    struct command* command = commands->head;

    while(command != NULL) {
        for (int i = 0; i < command->count; i++) {
            pid = fork();
            if (pid < 0) { // error
                perror("Fork failed");
            } else if (pid == 0) {      // Child process
                exec_error = execvp(command->call[0],command->call);
                if (exec_error < 0) {
                    printf("%s: command not found\n", command->call[0]);
                    freeStringArray(tokens, MAX_INPUT_LENGTH);
                    free(line);
                    free_children(children, false);
                    free_commands(commands);
                    exit(ERROR);
                }
                exit(0);
            } else {
                wait(&proc_status);
                failed = WEXITSTATUS(proc_status) != 0;
            }
        }
        if (command->operator == AND && failed) {
            break;
        } else if (command->operator == OR && !failed) {
            break;
        }
        command = command->next;
    }
}
/** Heavy modifications of readline for our implementation **/
void readline(char *line, struct children* children) {
    char** command_tokens = parse_command_line(line, MAX_INPUT_LENGTH);
    if(strcmp(command_tokens[0], "exit") == 0) {
        freeStringArray(command_tokens, MAX_INPUT_LENGTH);
        free(line);
        // Wait for child background processes
        free_children(children, true);
        exit(0);
    }
    struct commands* commands = parse_commands(command_tokens);

    pid_t pid;
    bool background = commands->background;

    if (background == 1) {
        pid = fork();
        if (pid < 0) {
            perror("Fork failed\n");
        }
        if (pid == 0) {         //Child process
            run_loop(commands, children, line, command_tokens);
            freeStringArray(command_tokens, MAX_INPUT_LENGTH);
            free_commands(commands);
            free(line);
            free_children(children, false);
            exit(0);
        } else if (pid > 0)  {
            freeStringArray(command_tokens, MAX_INPUT_LENGTH);
            free_commands(commands);
            struct child_process* new_child = create_child(pid);
            if (children->head == NULL) {
                children->head = new_child;
            } else {
                struct child_process* c = children->head;
                while (c->next != NULL) {
                    c = c->next;
                }
                c->next = new_child;
            }
            return;
        }
    } else {
        run_loop(commands, children, line, command_tokens);
    }
    freeStringArray(command_tokens, MAX_INPUT_LENGTH);
    free_commands(commands);
}

int main (void) {
    char* buffer = NULL;
    struct children* children = (struct children*)malloc(sizeof(struct children));
    children->head = NULL;
    //Endless loop to run program until exit
    while (1) {
        buffer = (char*)malloc(sizeof(char) * MAX_INPUT_LENGTH);
        fgets(buffer, MAX_INPUT_LENGTH, stdin);
        readline(buffer, children);
        free(buffer);
    }
    return 0;
}
