import java.io.*;
import java.util.*;

/**
 * This application implements variations on a few sorting algorithms 
 * presented in the lecture notes for CSC205.
 * 
 * An array is created to hold by default 32 numbers and this array
 * is then populated with random integers between 0 and MAX_INT.
 * 
 * If a sort or search menu item is selected then the array is 
 * scrambled using the scrambleArray method to randomly swap the
 * elements before performing the desired sort or search.
 * 
 * If a sort is performed, the following are printed:
 * 		-content of the array prior to sorting
 * 		-content of the array after sorting
 * 		-number of comparisons it took to sort the array
 * 		-number of swaps it took to sort the array
 * 
 * If a search is performed, a message is printed indicating whether
 * the number was found or not. If found, the index location of the 
 * first occurrence of the number is also printed.
 * 
 * If a binary search is requested, the array is sorted using the
 * shellSort method before the search is executed.
 * 
 * @author Natalie Fleischaker
 * @created 04.15.2015
 * @updated 04.20.2015
 */
public class Sorting {
	private static final int DFLT_ARRAY_SIZE  = 32;
	private static final int MAX_INT          = 99;
	private static final int LAST_INDEX       = DFLT_ARRAY_SIZE - 1;
	private static int comparisons;
	private static int swaps;
	
	public static void main(String[] args) {
		final int CHANGE           = 1;
		final int SELECTION        = 2;
		final int BINARY_INSERTION = 3;
		final int LINEAR           = 4;
		final int BINARY           = 5;
		final int EXIT             = 6;
		
		String title     = "Sorting Algorithms";
		int[] sortArray  = new int[DFLT_ARRAY_SIZE];
		Random rng       = new Random(System.currentTimeMillis());
		Scanner input    = new Scanner(System.in);
		
		//Populates array with randomly generated integral numbers between 0 and MAX_INT.
		for (int i = 0; i < sortArray.length; i++) {
			int randomInt = rng.nextInt(MAX_INT + 1);
			sortArray[i]  = randomInt;
		}
		
		MenuItem change          = new MenuItem(CHANGE, "Change Sort Order");
		MenuItem selection       = new MenuItem(SELECTION, "Selection Sort");
		MenuItem binaryInsertion = new MenuItem(BINARY_INSERTION, "Binary Insertion Sort");
		MenuItem linear          = new MenuItem(LINEAR, "Linear Search");
		MenuItem binary          = new MenuItem(BINARY, "Binary Search");
		MenuItem exit            = new MenuItem(EXIT, "Exit");
		
		Menu sorting = new Menu(title);
		sorting.add(change);
		sorting.add(selection);
		sorting.add(binaryInsertion);
		sorting.add(linear);
		sorting.add(binary);
		sorting.add(exit);
		
		int option;
		String order = sorting.getSortOrder();
		
		while ((option = sorting.activate(System.out)) != EXIT) {
			switch (option) {
			case CHANGE:   //switches btw ascending and descending sort order.
				if (order.equalsIgnoreCase(Menu.DFLT_SORT_ORDER)) {
					sorting.setSortOrder("descending");
					order = sorting.getSortOrder();
					System.out.println("Sorting order is now descending.\n");
				} else {
					sorting.setSortOrder(Menu.DFLT_SORT_ORDER);
					order = sorting.getSortOrder();
					System.out.println("Sorting order is now ascending.\n");
				}
				break;
			case SELECTION:
				scrambleArray(sortArray);
				System.out.println("Before sorting: ");
				printArray(sortArray);
				comparisons = 0;
				swaps = 0;
				
				if (order.equalsIgnoreCase(Menu.DFLT_SORT_ORDER)) {
					for (int i = 0, j = LAST_INDEX; i < j; i++, j--) {
						int lowest = i;
						int highest = j;
						for (int k = i; k <= j; k++) {
							comparisons++;
							if (sortArray[k] < sortArray[lowest]) {
								lowest = k;
							}
							else {
								comparisons++;
								if (sortArray[k] > sortArray[highest]) 
									highest = k;
							}
						}
						if (lowest != i) {
							swap(sortArray, i, lowest);
							if (highest == i)
								highest = lowest;
						}
						if (highest != j) {
							swap(sortArray, j, highest);
						}
					}
				} else {
					for (int i = 0, j = LAST_INDEX; i < j; i++, j--) {
						int lowest = i;
						int highest = j;
						for (int k = i; k <= j; k++) {
							comparisons++;
							if (sortArray[k] < sortArray[lowest]) {
								lowest = k;
							}
							else {
								comparisons++;
								if (sortArray[k] > sortArray[highest]) 
									highest = k;
							}
						}
						if (highest != i) {
							swap(sortArray, i, highest);
							if (lowest == i)
								lowest = highest;
						}
						if (lowest != j) {	
							swap(sortArray, j, lowest);
						}
					}
				}
				System.out.println("After sorting: ");
				printArray(sortArray);
				System.out.println("Number of comparisons: " + comparisons);
				System.out.println("Number of swaps: " + swaps + "\n");
				break;
			case BINARY_INSERTION:
				scrambleArray(sortArray);
				System.out.println("Before sorting: ");
				printArray(sortArray);
				comparisons = 0;
				swaps = 0;
				
				if (order.equalsIgnoreCase(Menu.DFLT_SORT_ORDER)) {
					binaryInsertionSortAscending(sortArray, DFLT_ARRAY_SIZE);
				} else {
					binaryInsertionSortDescending(sortArray, DFLT_ARRAY_SIZE);
				}
				System.out.println("After sorting: ");
				printArray(sortArray);
				System.out.println("Number of comparisons: " + comparisons);
				System.out.println("Number of swaps: " + swaps + "\n");
				break;
			case LINEAR:
				scrambleArray(sortArray);
				boolean problem = false;
				int i;
				
				do {
					System.out.print("Enter a number between 0 and "+ MAX_INT + " to find: ");
					try {
						int num = input.nextInt();
						if (0 < num && num <= MAX_INT) {
							for (i = 0; i < DFLT_ARRAY_SIZE; i++) {
								if (sortArray[i] == num) {
									System.out.println(num + " is present at index " + i + ".\n");
									problem = false;
									break;
								}
							}
							if (i == DFLT_ARRAY_SIZE) {
								System.out.println(num + " is not present in the array.\n");
								problem = false;
							}
						} else {
							System.out.println("That is not a valid number.\n");
							problem = true;
						}
					} catch (InputMismatchException e1) {
						System.out.println("That is not a number.\n");
						input.next();
						problem = true;
					}
				} while (problem);
				break;
			case BINARY:
				scrambleArray(sortArray);
				problem = false;
				shellSort(sortArray);
				//printArray(sortArray);
				do {
					System.out.print("Enter a number between 0 and "+ MAX_INT + " to find: ");
					try {
						int num = input.nextInt();
						if (0 < num && num <= MAX_INT) {
							int left = 0;
							int right = LAST_INDEX;
							while (left <= right) {
								int mid = (left + right) / 2;
								if (num < sortArray[mid])
									right = mid - 1;
								else if (num > sortArray[mid])
									left = mid + 1;
								else {
									System.out.println(num + " is present at index " + mid + ".\n");
									problem = false;
									break;
								}
							}
							if (left > right) {
								System.out.println(num + " is not present in the array.\n");
								problem = false;
							}
						} else {
							System.out.println("That is not a valid number.\n");
							problem = true;
						}
					} catch (InputMismatchException e2) {
						System.out.println("That is not a number.\n");
						input.next();
						problem = true;
					}
				} while (problem);
				break;
			}
		}
		input.close();
	}
	public static void scrambleArray(int[] array) {
		Random rng = new Random(System.currentTimeMillis());
		for (int i = LAST_INDEX; i > 0; i--) {
			int index    = rng.nextInt(i + 1);
			int temp     = array[index];
			array[index] = array[i];
			array[i]     = temp;
		}
	}
	public static void printArray(int[] array) {
		for (int i = 0; i < LAST_INDEX; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println(array[LAST_INDEX]);
		System.out.println();
	}
	public static void swap(int[] array, int a, int b) {
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
		swaps++;
	}
	public static void binaryInsertionSortAscending(int array[], int length) {
		for (int i = 1; i < length; i++) {
			int temp = array[i];
			int left = 0;
			int right = i - 1;
			while (left <= right) {
				int middle = (left + right) / 2;
			    comparisons++;
			    if (temp < array[middle])
			    	right = middle - 1;
			    else
			    	left = middle + 1;
			}
			for (int j = i; j >= left + 1; j--) {
				array[j] = array[j - 1];
				swaps++;
			}
			array[left] = temp;
			swaps++;
		}
	}
	public static void binaryInsertionSortDescending(int array[], int length) {
		for (int i = 1; i < length; i++) {
			int temp = array[i];
			int left = 0;
			int right = i - 1;
			while (left <= right) {
				int middle = (left + right) / 2;
			    comparisons++;
			    if (temp > array[middle])
			    	right = middle - 1;
			    else
			    	left = middle + 1;
			}
			for (int j = i; j >= left + 1; j--) {
				array[j] = array[j - 1];
				swaps++;
			}
			array[left] = temp;
			swaps++;
		}
	}
	public static void shellSort(int[] a) {
		int j;
		
		for (int gap = a.length / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < a.length; i++) {
				int temp = a[i];
				for (j = i; j >= gap; j -= gap) {
					if (temp < a[j - gap]) {
						a[j] = a[j - gap];
					} else
						break;
				}
				a[j] = temp;
			}
		}
	}
}
/*
 * A MenuItem object consists of a label, a unique id number, 
 * a choice value that the menu item is assigned when displayed by the menu display
 * method, and a flag to indicate whether the menu item is enabled.
 */
class MenuItem {
	private static final int DFLT_UNUSED_NUM = -1;
 
	private String label    = "";
	private int choice      = DFLT_UNUSED_NUM;
	private int id          = DFLT_UNUSED_NUM;
	private boolean enabled = true;
 
	public MenuItem(int id, String label) {
		this.label = label;
		if (id >= 0)
			this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public int getId() {
		return id;
	}
	public int getChoice() {
		return choice;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setChoice(int choice){
		this.choice = choice;
	}
	public void setEnabled(boolean on_or_off) {
		enabled = on_or_off;
	}
}
/*
 * A Menu is an object consisting of zero or more MenuItem objects. 
 * Each Menu object has a title, a character used to underline the 
 * title, and an ArrayList of MenuItem objects.
 */
class Menu {
	public static final char DFLT_UNDERLINE_CHAR = '=';
	private static final int DFLT_UNUSED_NUM     = -1;
	public static final String DFLT_SORT_ORDER  = "ascending";
 
	private String title              = "";
	private char underline            = DFLT_UNDERLINE_CHAR;
	private ArrayList<MenuItem> items = new ArrayList<>(1);
	private String sortOrder          = DFLT_SORT_ORDER;
 
	public Menu(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public char getUnderlineChar() {
		return underline;
	}
	public int getId(MenuItem menuItem) {
		return menuItem.getId();
	}
	public void add(MenuItem menuItem) {
		items.add(menuItem);
	}
	public void insert(int pos, MenuItem menuItem){
		items.add(pos, menuItem);
	}
	public void remove(MenuItem menuItem) {
		items.remove(menuItem);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setSortOrder(String sortChange) {
		sortOrder = sortChange;
	}
	public void setUnderlineChar(char underlineChar) {
		underline = underlineChar;
	}
	public void display(PrintStream out) {
		final int FIRST_CHOICE = 1;
  
		out.println(title);
  
		int lineLen = title.length();
		underline(out, underline, lineLen);
  
		int arraySize = items.size();
		int choiceIndex = FIRST_CHOICE;
		MenuItem menuItem = null;
  
		for (int i = 0; i < arraySize; i++) {
			menuItem = items.get(i);
			if (menuItem.getEnabled()) {
				menuItem.setChoice(choiceIndex);
				out.println(choiceIndex + ") " + menuItem.getLabel());
				choiceIndex++;
			} else {
				out.println("*) " + menuItem.getLabel());
			}
		}
	}
	public int activate(PrintStream out) {
		while(true) {
			display(System.out);
			System.out.println();
			System.out.print("\tChoice: ");
   
			final String ERROR_MSG = "\n*** error: incorrect choice entered -- try again\n";
   
			try {
				int nextChoice = input.nextInt();
    
				int arraySize = items.size();
				int choice = DFLT_UNUSED_NUM;
				MenuItem menuItem = null;
   
				for (int i = 0; i < arraySize; i++) {
					menuItem = items.get(i);
					choice = menuItem.getChoice();
					if (choice == nextChoice && menuItem.getEnabled())
						return menuItem.getId();
				}
				System.out.println(ERROR_MSG);
			} catch (InputMismatchException e) {
				System.out.println(ERROR_MSG);
				input.next();
				continue;
			}
		}
	}
	private Scanner input = new Scanner(System.in);
 
	private static void underline(PrintStream out, char c, int n) {
		for (int i = 0; i < n; i++)
			out.print(c);
		out.println();
    }
}
