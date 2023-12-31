import Interface_form.StackInterface;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack<E> implements StackInterface<E> {

    private static final int DEFAULT_CAPACITY = 10;         // 기본 크기
    private static final Object[] EMPTY_ARRAY = {};

    private Object[] array;         // 요소 배열
    private int size;


    public Stack() {
        this.array = EMPTY_ARRAY;
        this. size = 0;
    }

    public Stack(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
    }

    // 동적할당의 위한 private 메서드
    private void resize() {

        if (Arrays.equals(array, EMPTY_ARRAY)) {        // 빈 배열의 경우
            array = new Object[DEFAULT_CAPACITY];
            return;
        }

        int arrayCapacity = array.length;                // 현재 용적 크기

        if (size == arrayCapacity) {                     // 용적이 가득 찰 경우 새로운 사이즈 생성
            int newSize = arrayCapacity * 2;

            array = Arrays.copyOf(array, newSize);      // 배열복사
            return;
        }

        if (size < (arrayCapacity / 2)) {                // 현재 용적의 절반 미만으로 데이터가 존재할 경우 용적 감소

            int newCapacity = (arrayCapacity / 2);

            array = Arrays.copyOf(array, Math.max(DEFAULT_CAPACITY, newCapacity));
            return;
        }
    }

    @Override
    public E push(E item) {

        if (size == array.length) {         // 용적이 가득 차 있다면 용적 재할당
            resize();
        }

        array[size] = item;                 // 마지막 위치에 데이터 추가. size + 1
        size++;

        return item;
    }

    @Override
    public E pop() {

        if (size == 0) {                     // 삭제할 데이터가 없다면 Stack이 빈 예외 발생
            throw new EmptyStackException();
        }

        E object = (E) array[size - 1];      // 삭제 요소를 반환할 임시 변수

        array[size - 1] = null;              // 요소 삭제

        size --;
        resize();

        return object;
    }

    @Override
    public E peek() {

        if (size == 0) {
            throw new EmptyStackException();
        }

        return (E) array[size - 1];
    }

    @Override
    public int search(Object value) {

        if (value == null) {
            for (int idx = size - 1; idx >= 0; idx--) {
                if (array[idx] == null) {
                    return size - idx;
                }
            }
        } else {
            for (int idx = size - 1; idx >= 0; idx--) {

                if (array[idx].equals(value)) {
                    return size - idx;
                }
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {

        for (int i = 0; i < size; i++) {       // 저장되어 있는 모든 데이터 null 처리
            array[i] = null;
        }
        size = 0;
        resize();
    }

    @Override
    public boolean empty() {
        return size == 0;
    }
}
