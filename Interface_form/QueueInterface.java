package Interface_form;

public interface QueueInterface<E> {

    boolean offer(E e);

    E poll();

    E peek();
}
