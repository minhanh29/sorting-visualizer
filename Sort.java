// Sort algorithms
public class Sort
{
	public static <T extends Comparable<T>> void selectionSort(T[] data)
	{
		for (int i = 0; i < data.length; i++)
		{
			// find the minimum value from i to the end
			int min = i;

			for (int scan = i; scan < data.length; scan++)
				if (data[scan].compareTo(data[min]) < 0)
					min = scan;

			swap(data, i, min);
		}

	}


	public static <T extends Comparable<T>> void insertionSort(T[] data)
	{
		for (int i = 1; i < data.length; i++)
		{
			T current = data[i]; // the current element which needs sorting
			int position = i;

			while (position > 0 && data[position-1].compareTo(current) > 0)
			{
				data[position] = data[position-1];
				position--;
			}

			data[position] = current;
		}
	}


	public static <T extends Comparable<T>> void bubbleSort(T[] data)
	{
		int position, scan;

		for (position = data.length - 1; position > 0; position--)
		{
			for (scan = 0; scan < position; scan++)
			{
				if (data[scan].compareTo(data[scan + 1]) > 0)
					swap(data, scan, scan + 1);
			}
		}
	}


	public static <T extends Comparable<T>> void quickSort(T[] data)
	{
		quickSort(data, 0, data.length - 1);
	}


	private static <T extends Comparable<T>> void quickSort(T[] data, int start, int end)
	{
		if (start < end)
		{
			// get the pivot index (here I choose the middle one)
			int middle = partition(data, start, end);

			// sort the left partition
			quickSort(data, start, middle - 1);

			// sort the right partition
			quickSort(data, middle + 1, end);
		}
	}


	private static <T extends Comparable<T>> int partition(T[] data, int start, int end)
	{
		int pivot = (start + end) / 2;
		int left, right;
		T pivotElem = data[pivot];  // the pivot element

		// move the pivot to the beginning of the array
		swap(data, pivot, start);

		left = start + 1;
		right = end;

		while (left < right)
		{
			// find the next element that is greater than pivot
			while (left < right && data[left].compareTo(pivotElem) <= 0)
				left++;

			// find the next element that is less than pivot
			while (data[right].compareTo(pivotElem) > 0)
				right--;

			if (left < right)
				swap(data, left, right);
		}

		// move pivot back to its position
		// right is now the index of an element that less than pivot
		swap(data, start, right);

		return right;
	}


	public static <T extends Comparable<T>> void mergeSort(T[] data)
	{
		mergeSort(data, 0, data.length - 1);
	}


	private static <T extends Comparable<T>> void mergeSort(T[] data, int start, int end)
	{
		if (start < end)
		{
			int middle = (start + end) / 2;

			// recursively divide the list in half until there's only 1 left
			mergeSort(data, start, middle);
			mergeSort(data, middle + 1, end);

			// merge the two partition
			merge(data, start, middle, end);
		}
	}


	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> void merge(T[] data, int start, int middle, int end)
	{
		T[] temp = (T[]) (new Comparable[end - start + 1]);  // store the merged list
		int left = start, right = middle + 1;  // start indexes of the two partitions
		int index = 0;

		// looping until one partition runs out of elements
		while (left <= middle && right <= end)
		{
			if (data[left].compareTo(data[right]) < 0)
			{
				temp[index] = data[left];
				left++;
			}
			else
			{
				temp[index] = data[right];
				right++;
			}

			index++;
		}

		// add the rest of elements to the list
		if (left <= middle)
			for (int i = 0; i <= middle - left; i++)
				temp[index + i] = data[left + i];

		if (right <= end)
			for (int i = 0; i <= end - right; i++)
				temp[index + i] = data[right + i];

		// add all elemnts to the original list
		for (int i = 0; i <= end - start; i++)
			data[start + i] = temp[i];
	}


	// swap element and target in the array
	private static <T extends Comparable<T>> void swap(T[] data, int element1, int element2)
	{
		T temp = data[element2];
		data[element2] = data[element1];
		data[element1] = temp;
	}
}

