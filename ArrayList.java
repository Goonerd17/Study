

import Interface_form.Listinterface;

import java.util.Arrays;

public class ArrayList<E> implements Listinterface<E> {

    private static final int DEFAULT_CAPACITY = 10; //최소 용적(담을 수 있는 용량)크기, 기본값 10
    private static final Object[] EMPTY_ARRAY = {}; // 빈 배열

    private int size; // 요소 개수

    Object[] array; // 요소 적재 배열




    // 생성자 1, 초기 공간 할당하지 않음
    // 사용자가 요소 개수를 정할 수 없어 용적 할당을 명시하지 않은 new ArrayList();일 때 해당 생성자 사용
    public ArrayList() {
        this.array = EMPTY_ARRAY;
        this.size = 0;
    }

    // 생성자 2, 초기 공간 할당
    // 사용자가 요소 개수를 예측할 수 있어서 미리 용적 할당을 명시한 new ArrayList(용적); 일 때 해당 생성자 사용
    public ArrayList(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
    }
    // 각 생성자 블럭 안의 size는 용적의 크기 개념이 아닌 해당 용적에 존재하는 요소의 개수를 의미.



    /*
    동적 재할당 메서드

    들어오는 요소의 개수에 따라 최적화된 용적이 필요함
    요소가 적은데 용적이 과도하게 크면 메모리 낭비, 그 반대면 데이터 보관 불가능
    따라서 size가 capacity에 얼마만큼 차있는 지 확인하고 이에 맞게 동적으로 용적을 변경하는 메서드 필요
    외부에서의 접근은 데이터 손상을 야기하므로 private으로 접근제한
     */
    private void resize() {
        int array_capacity = array.length;

        if (Arrays.equals(array, EMPTY_ARRAY)) {        // 해시코드가 아닌 값을 비교해야 하기 때문에 Arrays.equals() 사용
            array = new Object[DEFAULT_CAPACITY];       // 사용자가 용적을 별도로 설정하지 않았을 경우, DEFAULT_CAPACITY의 용적을 가진 배열을 새로 생성해준다
            return;
        }



        if (size == array_capacity) {                     // 용적이 요소로 가득 찼을 경우
            int new_capacity = array_capacity * 2;        // 기존 용적을 두 배로 증가

            array = Arrays.copyOf(array, new_capacity);  // 새롭게 증가한 용적에 기존의 요소들을 복사
            return;                                       // Arrays.copyOf는 첫 번째 파라미터에 복사할 배열, 두 번째 파라미터에 용적의 크기를 넣어서 복사
        }



        if (size < (array_capacity / 2)) {                // 용적의 절반 미만으로 요소가 차지하고 있어, 빈 공간이 메모리의 공간을 불필요하고 낭비하고 있는 경우
            int new_capacity = array_capacity / 2;        // 기존 용적을 반으로 나눔

            array = Arrays.copyOf(array, Math.max(new_capacity, DEFAULT_CAPACITY));
            return;
        }
    }





    // 요소 추가 메서드
    @Override
    public boolean add(E value) {   // 가장 마지막 부분에 추가
        addLast(value);             // add 메서드 호출 시 자동으로 E타입의 value는 파라미터로 addLast로 보내짐
        return true;
    }

    public void addLast(E value) {

        if (size == array.length) { // 요소의 개수가 배열의 길이와 같다면 resize() 메서드를 호출해서 용적을 동적으로 재할당
            resize();
        }

        array[size] = value;        // 배열의 마지막 값에 value 저장
        size++;                     // 사이즈 1 증가
    }

    //해당 인덱스에 특정 요소를 추가하는 메서드
    @Override
    public void add(int index, E value) {           // 요소를 해당 위치에서 뒤로 이동시키는 코드가 필요

        if (index > size || index < 0) {            // 특정 위치가 요소의 개수 보다 크거나 (요소가 연속적으로 나열되어 있어야함)
            throw new IndexOutOfBoundsException();  // 음수가 들어온다면 범위를 벗어나게 되므로 예외 발생
        }



        if (index == size) {                        // index가 요소의 마지막 위치라면 addLast 메서드 사용
            addLast(value);
        }


        else {

            if (size == array.length) {             // 요소의 개수와 용적이 같다면 동적 재할당 resize() 메서드를 통해 용적을 증가
                resize();
            }


            for (int i = size; i > index; i--) {    // index 기준 후자에 있는 모든 요소들을 한 칸씩 뒤로 이동
                array[i] = array[i - 1];            // 한 칸씩 앞서 위치한 요소들을 한 칸 씩 뒤로 이동
            }

            array[index] = value;                   // index 위치에 value 할당
            size++;
        }

    }

    public void addFirst(E value) {
        add(0, value);                        // 위에서 작성한 add 메서드에서 index를 0으로 설정하면 됨
    }







    // 해당 인덱스 위치의 요소를 반환하는 메서드
    @SuppressWarnings("unchecked")                  // Object 에서 E 타입으로 변환할 수 없는 가능성도 있기에 ClassCastingException이 발생할 수도 있지만 이를 무시한다고 알림
    @Override
    public E get(int index) {



        if (index >= size || index < 0) {           // 범위 벗어나면 예외 발생
            throw new IndexOutOfBoundsException();
        }

        return (E) array[index];                    // Object 타입에서 E 타입으로 형변환 후 해당 인덱스의 값을 반환
    }



    // 해당 인덱스 위치의 요소를 다른 요소로 교체하는 메서드
    @Override
    public void set(int index, Object value) {      // add메서드는 요소를 추가, set메서드는 요소를 교체


        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        else {

            array[index] = value;                   // 해당 위치의 요소를 value로 교체
        }

    }



    // 해당 요소의 위치(인덱스)를 반환하는 메서드
    @Override
    public int indexOf(Object value) {

        int i = 0;


        for (i = 0; i < size; i++) {            // 0에서부터 탐색을 시작하여 value와 같은 객체(값)일 경우, 가장 먼저 찾게되는 i(위치) 반환
            if (array[i].equals(value))  {      // 객체끼리 비교할 때는 동등연산자 == 가 아닌 equals로 비교, ==로 비교시 해시코드를 비교하게 됨
                return i;
            }
        }

        return -1;                              // 일치하는 것이 없을 경우 -1을 반환
    }

    public int lastIndexOf(Object value) {      // 가장 마지막에 위치한 값에서부터 탐색을 시작, 사용자가 찾고자 하는 인덱스가 뒤 쪽이라고 예상 가능할 때
        for (int i = size - 1; i >= 0; i--) {
            if (array[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }




    // 찾고자 하는 요소의 존재 유무를 반환하는 메서드
    @Override
    public boolean contains(Object value) {

        if (indexOf(value) >= 0) {      // indexof메서드를 사용하여 값이 0보다 크거나 같을 경우.해당 값은 존재함의 의미하므로 true 반환
            return true;
        }
        else {
            return false;
        }
    }



    // 해당 index 위치를 제거하는 메서드
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {

        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        E element = (E) array[index];                   // index의 요소를 임시 변수 element에 저장
        array[index] = null;                            // 해당 인덱스의 위치에 존재하는 요소는 null로 초기화


        for (int i = index; i < size - 1; i++) {         // 삭제한 요소의 위치 뒤에 있는 모든 요소들을 위치를 한 칸씩 당겨옴
            array[i] = array [i + 1];
            array[i + 1] = null;
        }

        size--;
        resize();
        return element;
    }


    /*
     해당 요소를 제거하고 이 결과를 반환하는 메서드
     해당 요소와 매칭되는 요소가 여러 개 있을 경우 가장 먼저 마주하는 요소만 제거
     value와 같은 요소가 존재하는지 확인하고 이 위치가 어딘지를 파악,
     해당 index의 데이터를 지우고 나머지 뒤의 인덱스의 요소들을 하나씩 당기는 작업이 필요
     */
    @Override
    public boolean remove(Object value) {

        int index = indexOf(value);    // 삭제하고자 하는 요소의 인덱스 탐색

        if (index == -1) {             // 만약 해당 요소를 가진 인덱스가 없다면 false 반환
            return false;
        }

        remove(index);                 // 존재한다면, true 반환
        return true;
    }


    // 요소의 개수를 반환하는 메서드
    @Override
    public int size() {             // ArrayList에서 size 변수는 private 접근제한자를 갖고 있기 때문에 직접 참조 불가능
        return size;
    }


    // 해당 용적에 요소가 한 개도 존재하지 않는지를 반환하는 메서드
    @Override
    public boolean isEmpty() {
        return size == 0;           // 요소가 0개일 경우 이는 존재하지 않음을 의미하므로 true 반환
    }


    // 용적에 존재하는 모든 요소를 비우는 메서드
    @Override
    public void clear() {

        for (int i = 0; i < size; i++) {    // 모든 요소를 null로 변환
            array[i] = null;
        }

        size = 0;                           // 요소의 개수를 0으로 변환
        resize();
    }
}