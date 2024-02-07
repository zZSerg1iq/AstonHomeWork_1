import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<T extends Comparable<T>> implements MyList<T>, Iterable<T> {

    private enum MoveDirection {FORWARD, BACKWARD}

    private static final int INITIAL_CAPACITY = 16;
    private Object[] elements;
    private int lastIndex;
    private Comparator<? super T> comparator;

    public MyArrayList() {
        arrayInit(0);
    }

    public MyArrayList(Comparator<? super T> comparator) {
        this.comparator = comparator;
        arrayInit(0);
    }

    public MyArrayList(int initialCapacity) {
        arrayInit(initialCapacity);
    }

    public MyArrayList(int initialCapacity, Comparator<? super T> comparator) {
        this.comparator = comparator;
        arrayInit(initialCapacity);
    }

    private void arrayInit(int capacity) {
        if (capacity > INITIAL_CAPACITY) {
            elements = new Object[capacity]; // не забывай про скобки, они улучшают читаемость
        } else
            elements = new Object[INITIAL_CAPACITY];

        lastIndex = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < lastIndex;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elements[index++];
            }
        };
    }

    @Override
    public void add(T element) {
        if (element == null){
            throw new UnsupportedOperationException();
        }

        if (checkSize()) {
            grow();
        }
        elements[lastIndex++] = element;
    }

    @Override
    public void add(int index, T element) {
        if (element == null){
            throw new UnsupportedOperationException();
        }

        checkIndex(index);

        if (checkSize()) {
            grow();
        }

        movePartOfArray(index, MoveDirection.FORWARD);
        elements[index] = element;
        lastIndex++;
    }

    private boolean checkSize() {
        return lastIndex == elements.length - 1;
    }

    private void grow() {
        Object[] objects = new Object[elements.length * 2];
        System.arraycopy(
                elements, 0,
                objects, 0, elements.length);

        elements = objects;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return getElement(index);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= lastIndex) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @SuppressWarnings("unchecked")
    private T getElement(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    @Override
    public T remove(int index) {
        checkIndex(index);

        T element = get(index);

        if (element != null) {
            movePartOfArray(index, MoveDirection.BACKWARD);
            lastIndex--;
            return element;
        }
        return null;
    }

    @Override
    public boolean remove(T element) {
        for (int i = 0; i < lastIndex; i++) {
            if (element.equals(elements[i])) {
                movePartOfArray(i, MoveDirection.BACKWARD);
                lastIndex--;
                return true;
            }
        }
        return false;
    }

    @Override
    public void replace(int index, T element) {
        checkIndex(index);
        elements[index] = element;
    }

    private void movePartOfArray(int index, MoveDirection direction) {
        if (MoveDirection.FORWARD.equals(direction)) {
            System.arraycopy(
                    elements, index,
                    elements, index + 1,
                    lastIndex - index + 1
            );
            return;
        }
        System.arraycopy(
                elements, index + 1,
                elements, index,
                lastIndex - index
        );
    }

    @Override
    public void clear() {
        lastIndex = 0;
    }

    @Override
    public void sort() {
        if (elements.length < 2) {
            return;
        }
        sortElements(0, lastIndex - 1);
    }

    private void sortElements(int left, int right) {
        int length = right - left;
        if (length > 0) {

            if (length <= 11) {
                insertionSort(left, right);
            } else {
                normalize(left, right);

                int pivotPosAfterSort = miniSort(left, right);

                sortElements(left, pivotPosAfterSort - 1);
                sortElements(pivotPosAfterSort + 1, right);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void insertionSort(int leftPos, int rightPos) {
        for (int out = leftPos + 1; out <= rightPos; out++) {
            T obj = (T) elements[out];
            int mark = out;

            if (comparator != null) {
                while (mark > leftPos && comparator.compare((T) elements[mark - 1], obj) > 0) {
                    elements[mark] = elements[mark - 1];
                    mark--;
                }
            } else {
                while (mark > leftPos && ((T) elements[mark - 1]).compareTo(obj) > 0) {
                    elements[mark] = elements[mark - 1];
                    mark--;
                }
            }

            elements[mark] = obj;
        }
    }


    @SuppressWarnings("unchecked")
    private void normalize(int leftPos, int rightPos) {
        T leftElem = (T) elements[leftPos];
        T rightElem = (T) elements[rightPos];

        if (comparator != null) {
            if (comparator.compare(leftElem, rightElem) > 0) {
                swap(leftPos, rightPos);
            }
        } else {
            if (leftElem.compareTo(rightElem) > 0) {
                swap(leftPos, rightPos);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int miniSort(int left, int right) {
        int leftPos = left - 1;
        int rightPos = right;
        T pivotElement = (T) elements[right];

        while (true) {
            if (comparator != null) {
                while (comparator.compare((T) elements[++leftPos], pivotElement) < 0) ;
                while (rightPos > 0 && comparator.compare((T) elements[--rightPos], pivotElement) > 0) ;
            } else {
                while (((T) elements[++leftPos]).compareTo(pivotElement) < 0) ;
                while (rightPos > 0 && ((T) elements[--rightPos]).compareTo(pivotElement) > 0) ;
            }

            if (leftPos >= rightPos) {
                break;
            } else {
                swap(leftPos, rightPos);
            }
        }
        swap(leftPos, right);
        return leftPos;
    }

    private void swap(int left, int right) {
        Object temp = elements[left];
        elements[left] = elements[right];
        elements[right] = temp;
    }


    @Override
    public int size() {
        return lastIndex;
    }

    @Override
    public boolean contains(T element) {
        for (int i = 0; i < lastIndex; i++) {
            T elem = getElement(i);
            if (element.equals(elem)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return
                Arrays.toString(Arrays.copyOf(elements, lastIndex));
    }
}
