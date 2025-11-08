package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class TripletDeque<E> implements Deque<E>, Containerable {
    private static final int DEFAULT_CONTAINER_SIZE = 5;
    private static final int DEFAULT_QUEUE_SIZE = 1000;
    private final int containerSize;
    private final int maxQueueSize;
    private Container<E> head;
    private Container<E> tail;
    private int totalSize;

    private static class Container<E> {
        E[] elements;
        Container<E> prev;
        Container<E> next;
        int size;

        Container() {
            elements = (E[]) new Object[5];
            size = 0;
        }
    }

    public TripletDeque() {
        this(DEFAULT_CONTAINER_SIZE, DEFAULT_QUEUE_SIZE);
    }

    public TripletDeque(int containerSize, int maxQueueSize) {
        this.containerSize = containerSize;
        this.maxQueueSize = maxQueueSize;
        this.head = new Container<>();
        this.tail = head;
        this.totalSize = 0;
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException("Добавляется null!!!");
        if (totalSize >= maxQueueSize) throw new IllegalStateException("Очередь переполнена");
        if (head.size == containerSize) {
            Container<E> newContainerHead = new Container<>();
            newContainerHead.next = head;
            head.prev = newContainerHead;
            head = newContainerHead;
        }
        for (int i = head.size; i > 0; i--) {
            head.elements[i] = head.elements[i - 1];
        }
        head.elements[0] = e;
        head.size++;
        totalSize++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        if (totalSize >= maxQueueSize) throw new IllegalStateException("Queue is full");
        if (tail.size == containerSize) {
            Container<E> newContainerTail = new Container<>();
            newContainerTail.prev = tail;
            tail.next = newContainerTail;
            tail = newContainerTail;
        }
        tail.elements[tail.size] = e;
        tail.size++;
        totalSize++;
    }

    @Override
    public boolean offerFirst(E e) {
        if (e == null || totalSize >= maxQueueSize) {
            return false;
        }
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null || totalSize >= maxQueueSize) {
            return false;
        }
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) return null;
        E e = head.elements[0];
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
        return e;
    }

    @Override
    public E pollLast() {
        if (isEmpty()) return null;
        E e = tail.elements[tail.size - 1];
        tail.elements[tail.size - 1] = null;
        tail.size--;
        totalSize--;
        if (tail.size == 0 && tail.prev != null) {
            tail = tail.prev;
            tail.next = null;
        }

        return e;
    }

    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return head.elements[0];
    }

    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return tail.elements[tail.size - 1];
    }

    @Override
    public E peekFirst() {
        if (isEmpty()) return null;
        return head.elements[0];
    }

    @Override
    public E peekLast() {
        if (isEmpty()) return null;
        return tail.elements[tail.size - 1];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Container<E> current = head;
        while (current != null) {
            for (int i = 0; i < current.size; i++) {
                if (Objects.equals(o, current.elements[i])) {
                    for (int j = i; j < current.size - 1; j++) {
                        current.elements[j] = current.elements[j + 1];
                    }
                    current.elements[current.size - 1] = null;
                    current.size--;
                    totalSize--;
                    if (current.size == 0) {
                        if (current.prev != null) {
                            current.prev.next = current.next;
                        }
                        if (current.next != null) {
                            current.next.prev = current.prev;
                        }
                        if (current == head) {
                            head = current.next;
                        }
                        if (current == tail) {
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
                if (Objects.equals(o, current.elements[i])) {
                    for (int j = i; j < current.size - 1; j++) {
                        current.elements[j] = current.elements[j + 1];
                    }
                    current.elements[current.size - 1] = null;
                    current.size--;
                    totalSize--;
                    if (current.size == 0) {
                        if (current.prev != null) {
                            current.prev.next = current.next;
                        }
                        if (current.next != null) {
                            current.next.prev = current.prev;
                        }
                        if (current == head) {
                            head = current.next;
                        }
                        if (current == tail) {
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
        for (E element : c) {
            if (!offerLast(element)) {
                return false;
            }
        }
        return true;
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
    public boolean contains(Object o) {
        for (E element : this) {
            if (Objects.equals(o, element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return totalSize;
    }

    @Override
    public boolean isEmpty() {
        return totalSize == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Container<E> currentContainer = head;
            private int currentIndex = 0;
            private int elementsReturned = 0;

            @Override
            public boolean hasNext() {
                return elementsReturned < totalSize;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                while (currentIndex >= currentContainer.size) {
                    currentContainer = currentContainer.next;
                    currentIndex = 0;
                }
                E element = currentContainer.elements[currentIndex];
                currentIndex++;
                elementsReturned++;
                return element;
            }
        };
    }

    @Override
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

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("не поддерживается");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("не поддерживается");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("не поддерживается");
    }

    @Override
    public void clear() {
        head = new Container<>();
        tail = head;
        totalSize = 0;

    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("не поддерживается");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("не поддержиавается");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("не поддерживается");
    }
}




