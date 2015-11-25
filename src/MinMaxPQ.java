/*
 * Author: Alex Anderson
 * Summer 2014
 */
package a07;

import java.util.Arrays;
import java.util.NoSuchElementException;

/*
 * Double-ended priority queue implemented using 2 arrays of private class Element.
 * One to hold a Max PQ, and the other to hold the Min PQ.
 */
public class MinMaxPQ<T extends Comparable<T>>  {
	private Element<T>[] maxPriorityQueue;
	private Element<T>[] minPriorityQueue;
	private int size;

	@SuppressWarnings("unchecked")
	public MinMaxPQ() {
		maxPriorityQueue = new Element[2];
		minPriorityQueue = new Element[2];
		size = 0;
	}

	/*
	 * Element provides clean/concise way of storing useful information, much like a Node.
	 */
	private class Element<E> {
		E data;
		int maxIndex;
		int minIndex;
		Element(E data) {
			this.data = data;
		}
		@Override
		public String toString() {
			return data.toString();
		}
	}

	/*
	 * Adds element to DEPQ. If element is null, method throws NullPointerException.
	 *
	 * Time Complexity:
	 * maxSwim and minSwim use worst case log(n) comparisons.
	 * At first glance, the resize operation would take O(n) time.
	 * I researched this a little bit and if we use an amortized analysis
	 * of the resize method (since resizing doesn't happen every time), it
	 * turns out to be O(1) time for resizing.
	 * 
	 * This method would use O(2log(n)). This reduces to O(log(n)).
	 */
	public void add(T e) {
		if (e == null) throw new NullPointerException();
		if (size >= maxPriorityQueue.length - 1) 
			resize(maxPriorityQueue.length * 2);

		Element<T> temp = new Element<>(e);
		temp.maxIndex = ++size;
		temp.minIndex = size;

		maxPriorityQueue[size] = temp;
		minPriorityQueue[size] = temp;

		maxSwim(size);
		minSwim(size);
	}

	/*
	 * Removes the max element from the DEPQ.
	 * 
	 * Time Complexity:
	 * removeMax and removeMin time complexity is analogous to the
	 * add method time complexity (see explanation for add method).
	 */
	public T removeMax() {
		if (isEmpty()) throw new NoSuchElementException();

		//return element
		T max = maxPriorityQueue[1].data;

		//index in corresponding min PQ.
		int minIndex = maxPriorityQueue[1].minIndex;

		//Switches maximum element with last element in both PQs.	
		minSwap(minIndex, size);
		maxSwap(1, size);
		
		//Deletes end elements for both PQs and decrements size.
		maxPriorityQueue[size] = null;
		minPriorityQueue[size] = null;
		size--;
		
		if (size > 0) {
			//Shrinks arrays if size becomes 1/4 of the arrays capacity.
			if (size <= (minPriorityQueue.length - 1) / 4)
				resize((minPriorityQueue.length) / 2);

			//Sinks the switched element in both PQs.
			maxSink(1);
			minSink(minIndex);
		}
		return max;
	}


	/*
	 * Removes the min element from the DEPQ.
	 * 
	 * Time Complexity:
	 * removeMax and removeMin time complexity is analogous to the
	 * add method time complexity (see explanation for add method).
	 */
	public T removeMin() {
		if (isEmpty()) throw new NoSuchElementException();

		//return element
		T min = minPriorityQueue[1].data;

		//index in corresponding max PQ.
		int maxIndex = minPriorityQueue[1].maxIndex;

		//Switches minimum element with last element in both PQs.
		maxSwap(maxIndex, size);
		minSwap(1, size);
		
		//Deletes end elements for both PQs and decrements size.
		minPriorityQueue[size] = null;
		maxPriorityQueue[size] = null;
		size--;
		
		if (size > 0) {
			//Shrinks arrays if size becomes 1/4 of the arrays capacity.
			if (size <= (minPriorityQueue.length - 1) / 4)
				resize((minPriorityQueue.length) / 2);

			//Sinks the switched element in both PQs.
			minSink(1);
			maxSink(maxIndex);
		}
		return min;
	}

	/*
	 * Returns maximum element. Throws NoSuchElementException if no max element is found.
	 *
	 * Time Complexity:
	 * max() uses O(1) time.
	 */
	public T max() {
		if (size == 0) throw new NoSuchElementException("There is no Max element");
		return maxPriorityQueue[1].data;
	}

	/*
	 * Returns minimum element. Throws NoSuchElementException if no min element is found.
	 *
	 * Time Complexity:
	 * min() uses O(1) time.
	 */
	public T min() {
		if (size == 0) throw new NoSuchElementException("There is no Min element");
		return minPriorityQueue[1].data;
	}

	/*
	 * Returns true if empty, false otherwise.
	 *
	 * Time Complexity:
	 * isEmpty() uses O(1) time.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/*
	 * Sends element @ index upwards towards the top of the max PQ until it reaches the correct position.
	 */
	private void maxSwim(int index) {
		while (index > 1 && maxPriorityQueue[index / 2].data.compareTo(maxPriorityQueue[index].data) < 0) {
			maxSwap(index / 2, index);
			index /= 2;
		}
	}

	/*
	 * Sends element @ index downwards towards the correct position in the max PQ.
	 */
	private void maxSink(int index) {
		while (index <= size / 2) {
			int comparedIndex = index * 2;
			if (comparedIndex < size && maxPriorityQueue[comparedIndex].data.compareTo(maxPriorityQueue[comparedIndex + 1].data) < 0)
				comparedIndex++;
			if (maxPriorityQueue[index].data.compareTo(maxPriorityQueue[comparedIndex].data) > 0)
				break;
			maxSwap(index, comparedIndex);
			index = comparedIndex;	
		}
	}

	/*
	 * Swaps a & b in the max PQ.
	 */
	private void maxSwap(int a, int b) {
		Element<T> temp = maxPriorityQueue[a];
		maxPriorityQueue[a] = maxPriorityQueue[b];
		maxPriorityQueue[b] = temp;
		maxPriorityQueue[a].maxIndex = a;
		maxPriorityQueue[b].maxIndex = b;
	}

	/*
	 * Sends element @ index upwards towards the top of the min PQ until it reaches the correct position.
	 */
	private void minSwim(int index) {
		while (index > 1 && minPriorityQueue[index / 2].data.compareTo(minPriorityQueue[index].data) > 0) {
			minSwap(index / 2, index);
			minSwim(index / 2);
		}
	}

	/*
	 * Sends element @ index downwards towards the correct position in the min PQ.
	 */
	private void minSink(int index) {
		while (index <= size / 2) {
			int comparedIndex = index * 2;
			if (comparedIndex < size && minPriorityQueue[comparedIndex].data.compareTo(minPriorityQueue[comparedIndex + 1].data) > 0)
				comparedIndex++;
			if (minPriorityQueue[index].data.compareTo(minPriorityQueue[comparedIndex].data) < 0)
				break;
			minSwap(index, comparedIndex);
			index = comparedIndex;
		}
	}

	/*
	 * Swaps a & b in the min PQ.
	 */
	private void minSwap(int a, int b) {
		Element<T> temp = minPriorityQueue[a];
		minPriorityQueue[a] = minPriorityQueue[b];
		minPriorityQueue[b] = temp;
		minPriorityQueue[a].minIndex = a;
		minPriorityQueue[b].minIndex = b;
	}

	/*
	 * Resizes the DEPQ to size newArraySize
	 */
	private void resize(int newArraySize) {
		maxPriorityQueue = Arrays.copyOf(maxPriorityQueue, newArraySize);
		minPriorityQueue = Arrays.copyOf(minPriorityQueue, newArraySize);
	}
}