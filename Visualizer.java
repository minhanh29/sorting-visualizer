import javax.swing.JOptionPane;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.awt.Graphics;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Visualizer
{
	private static final int PADDING = 20;
	private static final int MAX_BAR_HEIGHT = 350, MIN_BAR_HEIGHT = 30;
	private Integer[] array;
	private int capacity, speed;
	private Bar[] bars;
	private boolean hasArray;

	// statistic
	private long startTime, time;
	private int comp, swapping;

	private Color originalColor, swappingColor, comparingColor;

	private BufferStrategy bs;
	private Graphics g;

	private SortedListener listener;

	public Visualizer(int capacity, int fps, SortedListener listener)
	{
		this.capacity = capacity;
		this.speed = (int) (1000.0/fps);
		this.listener = listener;
		startTime = time = comp = swapping = 0;

		originalColor = ColorManager.BAR_WHITE;
		comparingColor = Color.YELLOW;
		swappingColor = ColorManager.BAR_RED;

		bs = listener.getBufferStrategy();

		hasArray = false;
	}


	public void createRandomArray(int canvasWidth, int canvasHeight)
	{
		array = new Integer[capacity];
		bars = new Bar[capacity];
		hasArray = true;

		// initial position
		double x = PADDING;
		int y = canvasHeight- PADDING;

		// width of all bars
		double width = (double) (canvasWidth - PADDING*2) / capacity;

		// get graphics
        g = bs.getDrawGraphics();
		g.setColor(ColorManager.CANVAS_BACKGROUND);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		Random rand = new Random();
		int value;
		Bar bar;
		for (int i = 0; i < array.length; i++)
		{
			value = rand.nextInt(MAX_BAR_HEIGHT) + MIN_BAR_HEIGHT;
			array[i] = value;

			bar = new Bar((int)x, y, (int) width, value, originalColor);
			bar.draw(g);
			bars[i] = bar;

			// move to the next bar
			x += width;
		}

		bs.show();
		g.dispose();
	}


	// return a color for a bar
	private Color getBarColor(int value)
	{
		int interval = (int) (array.length / 5.0);
		if (value < interval)
			return ColorManager.BAR_ORANGE;
		else if (value < interval * 2)
			return ColorManager.BAR_YELLOW;
		else if (value < interval * 3)
			return ColorManager.BAR_GREEN;
		else if (value < interval * 4)
			return ColorManager.BAR_CYAN;
		return ColorManager.BAR_BLUE;

	}


	/* BUBBLE SORT */
	public void bubbleSort()
	{
		if (!isCreated())
			return;

		// get graphics
        g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.bubbleSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;
		int count = 0;
		for (int i = array.length-1; i >= 0; i--)
		{
			count = 0;
			for (int j = 0; j < i; j++)
			{
				colorPair(j, j+1, comparingColor);

				if (array[j] > array[j+1])
				{
					swap(j, j+1);
					count++;
					swapping++;
				}

				comp++;
			}

			bars[i].setColor(getBarColor(i));
			bars[i].draw(g);
			bs.show();

			if (count == 0)  // the array is sorted
				break;
		}

		finishAnimation();

		g.dispose();
	}


	/* SELECTION SORT */
	public void selectionSort()
	{
		if (!isCreated())
			return;

		// get graphics
        g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.selectionSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;
		for (int i = array.length-1; i >= 0; i--)
		{
			// find the max
			int max = array[i], index = i;
			for (int j = 0; j <= i; j++)
			{
				if (max < array[j])
				{
					max = array[j];
					index = j;
				}

				colorPair(index, j, comparingColor);
				comp++;
			}

			swap(i, index);
			swapping++;

			bars[i].setColor(getBarColor(i));
			bars[i].draw(g);
			bs.show();
		}

		finishAnimation();

		g.dispose();
	}


	/* INSERTION SORT */
	public void insertionSort()
	{
		if (!isCreated())
			return;

		// gett graphics
		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.insertionSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		Bar bar;
		for (int i = 1; i < array.length; i++)
		{
			bars[i].setColor(getBarColor(i));

			// find the insertion location by comparing to its predecessor
			int index = i-1, element = array[i];
			while (index >= 0 && element < array[index])
			{
				array[index+1] = array[index];

				bar = bars[index+1];
				bar.clear(g);
				bar.setValue(bars[index].getValue());
				colorBar(index+1, swappingColor);

				index--;
				comp++;
				swapping++;
			}
			comp++;

			index++;

			// insert the element
			array[index] = element;

			bar = bars[index];
			bar.clear(g);
			bar.setValue(element);
			bar.setColor(getBarColor(index));
			bar.draw(g);

			bs.show();
		}

		finishAnimation();

		g.dispose();
	}


	/* QUICK SORT */
	public void quickSort()
	{
		if (!isCreated())
			return;

		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.quickSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		quickSort(0, array.length-1);

		finishAnimation();
		g.dispose();
	}


	// recursive quicksort
	private void quickSort(int start, int end)
	{
		if (start < end)
		{
			// place pivot in correct spot
			int pivot = partition(start, end);

			// coloring
			bars[pivot].setColor(getBarColor(pivot));
			bars[pivot].draw(g);
			bs.show();

			// sort the left half
			quickSort(start, pivot-1);

			// sort the right half
			quickSort(pivot+1, end);
		}
	}


	// quick sort partition
	private int partition(int start, int end)
	{
		// pivot is the last element
		int pivot = array[end];

		// mark it as pivot
		Bar bar = bars[end];
		Color oldColor = bar.getColor();
		bar.setColor(comparingColor);
		bar.draw(g);
		bs.show();

		int index = start-1;
		for (int i = start; i < end; i++)
		{
			if (array[i] < pivot)
			{
				index++;
				swap(index, i);
				swapping++;
			}
			comp++;
		}

		bar.setColor(oldColor);
		bar.draw(g);
		bs.show();

		// move pivot to correct location
		index++;
		swap(index, end);
		swapping++;

		return index;
	}

	/* MERGE SORT */
	public void mergeSort()
	{
		if (!isCreated())
			return;

		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.mergeSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		mergeSort(0, array.length-1);

		finishAnimation();
		g.dispose();
	}


	// recursive mergeSort
	private void mergeSort(int left, int right)
	{
		if (left >= right)
			return;

		// find the middle
		int middle = (right + left) / 2;

		// sort the left half
		mergeSort(left, middle);

		// sort the second half
		mergeSort(middle+1, right);

		// merge them
		merge(left, middle, right);
	}


	// merge for mergeSort
	private void merge(int left, int middle, int right)
	{
		Color mergeColor = getBarColor(middle);

		// number of items in the first half
		int n1 = middle - left + 1;
		int n2 = right - middle;  // second half

		// create array for those parts
		int[] leftArr = new int[n1];
		for (int i = 0; i < n1; i++)
			leftArr[i] = array[left+i];

		int[] rightArr = new int[n2];
		for (int i = 0; i < n2; i++)
			rightArr[i] = array[middle+i+1];

		// starting index
		int l = 0, r = 0, k = left;  // k: for the original array

		// merge
		while (l < n1 && r < n2)
		{
			Bar bar = bars[k];
			bar.clear(g);
			if (leftArr[l] < rightArr[r]) {
				array[k] = leftArr[l];
				bar.setValue(leftArr[l]);
				l++;
			} else {
				array[k] = rightArr[r];
				bar.setValue(rightArr[r]);
				r++;
			}

			bar.setColor(mergeColor);
			colorBar(k, swappingColor);
			k++;
			comp++;
			swapping++;
		}


		// add the remaining in the two arrays if there are any
		while (l < n1)
		{
			Bar bar = bars[k];
			bar.clear(g);

			array[k] = leftArr[l];
			bar.setValue(leftArr[l]);
			bar.setColor(mergeColor);
			colorBar(k, swappingColor);
			l++;
			k++;
			swapping++;
		}

		while (r < n2)
		{
			Bar bar = bars[k];
			bar.clear(g);

			array[k] = rightArr[r];
			bar.setValue(rightArr[r]);
			bar.setColor(mergeColor);
			colorBar(k, swappingColor);
			r++;
			k++;
			swapping++;
		}
	}


	// swap 2 elements given 2 indexes
	private void swap(int i, int j)
	{
		// swap the elements
		int temp = array[j];
		array[j] = array[i];
		array[i] = temp;

		// clear the bar
		bars[i].clear(g);
		bars[j].clear(g);

		// swap the drawings
		bars[j].setValue(bars[i].getValue());
		bars[i].setValue(temp);

		colorPair(i, j, swappingColor);
	}


	private void colorPair(int i, int j, Color color)
	{
		Color color1 = bars[i].getColor(), color2 = bars[j].getColor();
		// drawing
		bars[i].setColor(color);
		bars[i].draw(g);

		bars[j].setColor(color);
		bars[j].draw(g);

		bs.show();

		// delay
		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (Exception ex) {}

		// put back to original color
		bars[i].setColor(color1);
		bars[i].draw(g);

		bars[j].setColor(color2);
		bars[j].draw(g);

		bs.show();
	}


	// color the bar in speed time and put it
	// back to its original color
	private void colorBar(int index, Color color)
	{
		Bar bar = bars[index];
		Color oldColor = bar.getColor();

		bar.setColor(color);
		bar.draw(g);
		bs.show();

		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (Exception ex) {}

		bar.setColor(oldColor);
		bar.draw(g);

		bs.show();
	}


	// swiping effect when the sorting is finished
	private void finishAnimation()
	{
		// swiping to green
		for (int i = 0; i < bars.length; i++)
		{
			colorBar(i, comparingColor);
			bars[i].setColor(getBarColor(i));
			bars[i].draw(g);
			bs.show();
		}

		// show elapsed time and comparisons
		listener.onArraySorted(time, comp, swapping);
	}


	// for restore purpose
	public void drawArray()
	{
		if (!hasArray)
			return;

		g = bs.getDrawGraphics();

		for (int i = 0; i < bars.length; i++)
		{
			bars[i].draw(g);
		}

		bs.show();
		g.dispose();
	}


	// check if array is created
	private boolean isCreated()
	{
		if (!hasArray)
			JOptionPane.showMessageDialog(null, "You need to create an array!", "No Array Created Error", JOptionPane.ERROR_MESSAGE);
		return hasArray;
	}


	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public void setFPS(int fps)
	{
		this.speed = (int) (1000.0/fps);
	}

	public interface SortedListener
	{
		void onArraySorted(long elapsedTime, int comparison, int swapping);
		BufferStrategy getBufferStrategy();
	}
}
