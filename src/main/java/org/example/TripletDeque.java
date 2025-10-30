package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
public class TripletDeque<E> implements Deque<E>, Iterable<E> {
    private static final int defaultContainerSize=5;
    private static final int defaultQueueSize=1000;
    private int containerSize=0;
    private int queueSize=0;

    private static class Container<E> {
        E[] elements;
        Container<E> prev;
        Container<E> next;
        int size;

        Container() {
            elements = (E[]) new Object[5];
            prev = null;
            next = null;
            size = 0;
        }
    }

    private Container<E> head;
    private Container<E> tail;
    private int totalSize;


    public TripletDeque() {
        this(defaultContainerSize, defaultQueueSize);
    }

    public TripletDeque(int containerSize, int queueSize) {
        this.containerSize = containerSize;
        this.queueSize = queueSize;
        head = new Container<>();
        tail = head;
        totalSize = 0;
    }







    @Override
    public void addFirst(E e) {
        if(e==null) throw new NullPointerException("Нулевой элемент");
        if (containerSize>=defaultQueueSize) throw new IllegalStateException("очередь переполнена");
        if(head.size==containerSize) {
            Container<E> newHead = new Container<>();
            newHead.next = head;
            head.prev = newHead;
            head = newHead;
        }
        for(int i=0; i< head.size; i++){
            head.elements[i]=head.elements[i];
        }
        head.elements[0]=e;
        head.size++;
        totalSize++;


    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException("Вы пытаетесь добавить нулевой элемент");
        if (totalSize >= queueSize) {
            throw new IllegalStateException("Queue capacity exceeded");
        }
        if (tail.size == containerSize) {
            Container<E> newTail = new Container<>();
            newTail.prev = tail;
            tail.next = newTail;
            tail = newTail;
        }
        // Добавляем элемент в конец контейнера
        tail.elements[tail.size] = e;
        tail.size++;
        totalSize++;
    }






    @Override
    public boolean offerFirst(E e) {
        try {
            addFirst(e);
            return true;
        } catch (IllegalStateException ex) {
            return false;
        }
    }


    @Override
    public boolean offerLast(E e) {
        try {
            addLast(e);
            return true;
        } catch (IllegalStateException ex) {
            return false;
        }

    }

    @Override
    public E removeFirst() {
        E item = pollFirst();
        if (item == null) throw new NoSuchElementException("Deque is empty");
        return item;

    }

    @Override
    public E removeLast() {
        E item = pollLast();
        if (item == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return item;

    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        E item = head.elements[0];
        for (int i = 0; i < head.size - 1; i++) {
            head.elements[i] = head.elements[i + 1];
        }
        head.elements[head.size - 1] = null;
        head.size--;
        totalSize--;
        if (head.size == 0 && head.next != null) {
            head = head.next;
            head.prev = null;
        }
        return item;

    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        E item = tail.elements[tail.size - 1];
        tail.elements[tail.size - 1] = null;
        tail.size--;
        totalSize--;
        if (tail.size == 0 && tail.prev != null) {
            tail = tail.prev;
            tail.next = null;
        }
        return item;

    }

    @Override
    public E getFirst() {
        E item = peekFirst();
        if (item == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return item;

    }

    @Override
    public E getLast() {
        E item = peekLast();
        if (item == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        return item;

    }

    @Override
    public E peekFirst() {
        if (isEmpty()) {
            return null;
        }
        return head.elements[0];

    }

    @Override
    public E peekLast() {
        if (isEmpty()) {
            return null;
        }
        return tail.elements[tail.size - 1];
    }



    @Override
    public boolean removeFirstOccurrence(Object o) {
        Container<E> current = head;
        while (current != null) {
            for (int i = 0; i < current.size; i++) {
                if (o.equals(current.elements[i])) {
                    for (int j = i; j < current.size - 1; j++) {
                        current.elements[j] = current.elements[j + 1];
                    }
                    current.elements[current.size - 1] = null;
                    current.size--;
                    totalSize--;
                    log.info(String.valueOf(current.size));
                    if (current.size == 0) {
                        if (current.prev != null) {
                            current.prev.next = current.next;
                        } else {
                            head = current.next;
                        }
                        if (current.next != null) {
                            current.next.prev = current.prev;
                        } else {
                            tail = current.prev;
                        }
                    }
                    return true;
                }
            }
            current = current.next;
        }
        return false;

    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Container<E> current = tail;
        while (current != null) {
            for (int i = current.size - 1; i >= 0; i--) {
                if (o.equals(current.elements[i])) {
                    for (int j = i; j < current.size - 1; j++) {
                        current.elements[j] = current.elements[j + 1];
                    }
                    current.elements[current.size - 1] = null;
                    current.size--;
                    totalSize--;
                    if (current.size == 0) {
                        if (current.prev != null) {
                            current.prev.next = current.next;
                        } else {
                            head = current.next;
                        }
                        if (current.next != null) {
                            current.next.prev = current.prev;
                        } else {
                            tail = current.prev;
                        }
                    }
                    return true;
                }
            }
            current = current.prev;
        }
        return false;

    }

    @Override
    public boolean add(E e) {
        return offerLast(e);
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            if (!offerLast(e)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        head = new Container<>();
        tail = head;
        totalSize = 0;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }


    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Container<E> current = head;
        while (current != null) {
            for (int i = 0; i < current.size; i++) {
                if (o.equals(current.elements[i])) {
                    return true;
                }
            }
            current = current.next;
        }
        return false;

    }

    @Override
    public int size() {
        return totalSize;
    }

    @Override
    public boolean isEmpty() {
        return totalSize==0;
    }

    @Override
    public Iterator<E> iterator() {
        return new DequeIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("descendingIterator() is not implemented");
    }
    public Object[] getContainerByIndex(int cIndex) {
        Container<E> current = head;
        int index = 0;
        while (current != null) {
            if (index == cIndex) {
                return current.elements;
            }
            current = current.next;
            index++;
        }
        return null;
    }




    private class DequeIterator implements Iterator<E> {
        private Container<E> currentContainer = head;
        private int index = 0;

        @Override
        public boolean hasNext() {
            return currentContainer != null && (index < currentContainer.size || currentContainer.next != null);
        }

        @Override
        public E next() {
            if (currentContainer == null) {
                throw new NoSuchElementException("No more elements in the deque");
            }
            if (index >= currentContainer.size) {
                currentContainer = currentContainer.next;
                index = 0;
            }
            if (currentContainer == null) {
                throw new NoSuchElementException("No more elements in the deque");
            }
            return currentContainer.elements[index++];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Container<E> current = head;
        while (current != null) {
            for (int i = 0; i < current.size; i++) {
                sb.append(current.elements[i]);
                if (i < current.size - 1 || current.next != null) {
                    sb.append(", ");
                }
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}




