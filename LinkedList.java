import Interface_form.Listinterface;

import java.util.NoSuchElementException;


class Node<E> {     // 노드 클래스
    E data;
    Node<E> next;   // 다음 노드를 가리키는 참조변수

    Node(E data) {
        this.data = data;
        this.next = null;
    }
}


public class LinkedList <E> implements Listinterface<E> {

    private Node<E> head;  // node 첫 부분
    private Node<E> tail;  // node 마지막 부분
    private int size;      // 요소 개수

    public LinkedList() {  // 처음 단일 연결리스트를 생성할 때는 아무런 데이터가 없음
        this.head = null;
        this.tail = null;
        this.size = 0;
    }


    // 특정 위치의 노드를 반환하는 메서드
    private Node<E> search(int index) {

        if (index < 0 || index >= size) {                   // 현재 존재하지 않는 위치의 노드일 경우 예외 발생

            throw new IndexOutOfBoundsException();
        }


        Node<E> x = head;                                   // head가 가리키는 노드부터 시작


        for (int i = 0; i < index; i++) {
            x = x.next;                                     // x노드 다음 노드를 x에 저장 후 반복
        }

        return x;
    }

    @Override
    public boolean add(E value) {
        addLast(value);
        return true;
    }


    public void addFirst(E value) {
        Node<E> newNode = new Node<E>(value);   // 새 노드 생성
        newNode.next = head;                    // 새 노드의 다음 노드로 head 노드 연결
        head = newNode;                         // head가 가리키는 노드를 새 노드로 변경
        size++;

        /**
         * 데이터가 한 개, 즉 노드가 하나밖에 없을 경우 새 노드는 시작노드이자
         * 마지막 노드이다. 따라서 tail = head.
         */

        if (head.next == null) {
            tail = head;
        }
    }

    public void addLast(E value) {
        Node<E> newNode = new Node<E>(value);    // 새 노드 생성

        if (size == 0) {
            addFirst(value);                     // 처음으로 생성하는 노드일 경우 addFirst로 추가
            return;
        }

        /**
         * 마지막 노드의 다음 노드가 새 노드를 가리키도록 하고
         * tail이 가리키는 노드도 새 노드로 바꾼다.
         */

        tail.next = newNode;
        tail = newNode;
        size++;
    }

    @Override
    public void add(int index, E value) {

        // 잘못된 범위를 설정할 경우 예외 발생
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }


        // 추가하려는 index가 가장 앞에 추가되는 경우 addFirst
        if (index == 0) {
            addFirst(value);
            return;
        }


        // 추가하려는 index가 가장 뒤에 추가되는 경우 addLast
        if (index == size) {
            addLast(value);
            return;
        }


        /**
         * 이전 노드가 가리키는 노드를 끊은 뒤
         * 새 노드로 변경
         * 또한 새 노드가 가리키는 노드는 next_Node로 설정
         */
        Node<E> prev_Node = search(index - 1);

        Node<E> next_Node = prev_Node.next;

        Node<E> newNode = new Node<E>(value);


        prev_Node.next = null;
        prev_Node.next = newNode;
        newNode.next = next_Node;
        size++;

    }

    public E remove() {


        Node<E> headNode = head;

        if (headNode == null){
            throw new NoSuchElementException();
        }

        E element = headNode.data;

        Node<E> nextNode = head.next;

        head.data = null;
        head.next = null;

        head = nextNode;        //기존 첫 번째 노드의 데이터를 null로 변환 후,
        size--;                 // 원래는 2번째였던 노드의 데이터를 1번째로 옮김. 이 후 사이즈는 하나 감소

        if (size == 0) {
            tail = null;
        } return element;

    }

    @Override
    public E remove(int index) {

        if (index == 0) {
            return remove();
        }

        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> preNode = search(index - 1);
        Node<E> removeNode = preNode.next;
        Node<E> nextNode = removeNode.next;

        E element = removeNode.data;

        if (preNode.next == null) {
            tail = preNode;
        }

        removeNode.next = null;
        removeNode.data = null;
        size--;

        return element;
    }

    @Override
    public boolean remove(Object value) {

        Node<E> preNode = head;
        boolean hasValue = false;
        Node<E> x = head;

        for (; x != null; x = x.next) {
            if (value.equals(x.data)) {
                hasValue = true;
                break;
            }
            preNode = x;
        }

        if (x == null) {
            return false;
        }

        if (x.equals(head)) {
            remove();
            return true;
        }

        else {
            preNode.next = x.next;

            if (preNode.next == null) {
                tail = preNode;
            }

            x.data = null;
            x.next = null;
            size--;
            return true;
        }
    }

    @Override
    public E get(int index) {
        return search(index).data;          // search() 내부에서 예외발생하면 해당 예외를 던짐
    }                                       // 다만 search는 노드를 반환하고, get은 노드의 데이터를 반환하는 차이점

    @Override
    public void set(int index, E value) {

        Node<E> replaceNode = search(index);    // search()로 해당 노드를 찾고, 데이터를 새로운 데이터로 변경
        replaceNode.data = null;
        replaceNode.data = value;

    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    @Override
    public int indexOf(Object value) {
        int index = 0;

        for (Node<E> x = head; x != null; x = x.next ) {
            if (value.equals(x.data)) {         // 객체 간의 동등성 비교는 == 이 아닌 .equals
                return index;
            }

            index++;
        }

        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (Node<E> x = head; x != null;) {
            Node<E> nextNode = x.next;
            x.data = null;
            x.next = null;
            x = nextNode;
        }

        tail = null;
        head = tail;
        size = 0;
    }
}
