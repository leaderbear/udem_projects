#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <pthread.h>
#include "blocking_q.h"

#pragma clang diagnostic push
#pragma ide diagnostic ignored "OCUnusedGlobalDeclarationInspection"

#define TODO printf("TODO!\n");

/**
 * Internal function to blocking_q. Takes an element
 * in the queue. This functions assumes the following
 * preconditions:
 *  - The thread has safe access to the queue
 *  - The queue is NOT empty
 * Also update the size.
 * @param q the queue
 * @return an element
 */
task_ptr __blocking_q_take(blocking_q *q) { // NOLINT(bugprone-reserved-identifier)
    // TODO
    // return NULL;
    task_ptr element = q->first->data;
    blocking_q_node *new_first = q->first->next;
    free(q->first);
    q->first = new_first;
    q->sz--;
    return element;
}

/**
 * Create a blocking queue. Initializes the synchronisation primitives
 * @param q the queue
 * @return if init was successful.
 */
bool blocking_q_init(blocking_q *q) {

    if (pthread_mutex_init(&q->lock, NULL) != 0) {
        return false;
    }

    if (pthread_cond_init(&q->cond, NULL) != 0) {
        pthread_mutex_destroy(&q->lock);
        return false;
    }

    q->sz = 0;
    q->first = NULL;

    return true;
}

/**
 * Destroy a blocking queue. Removes the allocations of the data
 * and destroys the sync. primitives.
 * @param q ptr to the blocking queue
 */
void blocking_q_destroy(blocking_q *q) {

    pthread_mutex_lock(&q->lock);

    blocking_q_node *curr = q->first;
    blocking_q_node *temp;
    while (curr) {
        temp = curr;
        curr = curr->next;
        free(temp);
    }
    q->sz = 0;

    pthread_mutex_unlock(&q->lock);

    pthread_mutex_destroy(&q->lock);
    pthread_cond_destroy(&q->cond);
}

/**
 * Put a task in the blocking queue. This task can fail if no
 * memory is available to allocate a new entry in the queue
 * @param q the queue
 * @param data the data description to put inside the queue
 * @returns if the data was put correctly inside the queue.
 */
bool blocking_q_put(blocking_q *q, task_ptr data) {

    blocking_q_node *new_node = (blocking_q_node*) malloc(sizeof(blocking_q_node));
    if (!new_node) {
        return false;
    }

    new_node->data = data;
    new_node->next = NULL;

    pthread_mutex_lock(&q->lock);

    if (!q->first) {
        q->first = new_node;
    } else {
        blocking_q_node *curr = q->first;
        while (curr->next) {
            curr = curr->next;
        }
        curr->next = new_node;
    }
    q->sz++;

    pthread_cond_signal(&q->cond);
    pthread_mutex_unlock(&q->lock);

    return true;
}

/**
 * Get an element in the blocking queue. If the queue is empty,
 * the current thread is put to sleep until an element is added
 * to the queue.
 * @param q the blocking queue
 * @return the element
 */
task_ptr blocking_q_get(blocking_q *q) {

    task_ptr task;

    pthread_mutex_lock(&q->lock);

    while (q->sz == 0) {
        pthread_cond_wait(&q->cond, &q->lock);
    }

    task = __blocking_q_take(q);

    pthread_cond_signal(&q->cond);
    pthread_mutex_unlock(&q->lock);

    return task;

}

/**
 * Drain as many elements as possible into the area allowed
 * by the pointer. This function does not block.
 * @param q the queue
 * @param data the pointer where to store the data
 * @param sz the maximum area available in the buffer
 * @return the number of entries written.
 */
size_t blocking_q_drain(blocking_q *q, task_ptr *data, size_t sz) {


    size_t count = 0;

    pthread_mutex_lock(&q->lock);

    while (q->first && count < sz) {
        //This function doesn't block, no need for a wait
        data[count++] = blocking_q_get(q);
    }

    //Do we need to add a signal here?
    // pthread_mutex_unlock(&q->lock);
    return count;
}

/**
 * Drain at least min elements in the buffer. This function
 * might block if there are not enough elements to drain.
 * @param q the queue
 * @param data the pointer where to store the data
 * @param sz the maximum area available in the buffer
 * @param min the minimum amounts of elements to drain (must be less than sz)
 * @return the number of elements written
 */
size_t blocking_q_drain_at_least(blocking_q *q, task_ptr *data, size_t sz, size_t min) {

    size_t count = 0;

    pthread_mutex_lock(&q->lock);

    while ((q->first && count < sz) || count < min) {
        task_ptr node = blocking_q_get(q);
        //Block if not enough elements to drain
        if (!node) {
            pthread_cond_wait(&q->cond, &q->lock);
            continue;
        }
        data[count++] = node;
    }

    //Do we need to add a signal here?
    // pthread_mutex_unlock(&q->lock);
    return count;
}

/**
 * Check the first element in the queue. This will allocate storage for a copy
 * of the character. If the allocation fails, this function returns false.
 * @param q the queue
 * @param c pointer to a pointer where an allocated char will be stored
 * @return if there is an element allocated in the pointer
 */
bool blocking_q_peek(blocking_q *q, task **c) {

    pthread_mutex_lock(&q->lock);

    if (!q->first) {
        pthread_mutex_unlock(&q->lock);
        return false;
    }

    *c = malloc(sizeof(task));
    if (!c) {
        pthread_mutex_unlock(&q->lock);
        return false;
    }

    **c = *q->first->data;

    //Do we need to add a signal here?
    // pthread_mutex_unlock(&q->lock);
    return true;
}
