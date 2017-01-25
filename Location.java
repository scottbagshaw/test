import java.util.Arrays;

/**
 * The location class contains all location's in the warehouse The class can be
 * used to find the shortest route between an array of location IDs
 * 
 * @author Scott + Omar
 *
 */
public class Location {
	// default values

	/*
	 * The warehouse is laid out like the aisles of a supermarket You can only
	 * move between 'aisles' (which are sometimes referred to as columns) when
	 * you are at either end of the aisle The worker always starts/finished at
	 * the point 0,0
	 * 
	 * For example, in the diagram below the aisle's are the solid lines, this
	 * is where the items are stored, and where the employee can walk note,
	 * there is no difference between the 'left' or 'right' side of the aisle,
	 * it is only which aisle, and which row In this case diagram 0,0 is at the
	 * bottom left. Then the max height would be 3, because we can only change
	 * aisle when we're at 0 or 3 The max width is irrelevant to the algorithm
	 * as we can keep adding items in aisles very far away _ _ _ _ _ | | | | | |
	 * | | | | | | |_|_|_|_|_|
	 */
	
	// locationID is not actually used, but can illustrate how it refers to the coords
	private int[] locationID = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private int[][] coords = { { 0, 0 }, { 1, 1 }, { 4, 2 }, { 1, 3 },
			{ 5, 5 }, { 9, 8 }, { 2, 13 }, { 6, 21 }, { 5, 34 }, { 3, 1 } };

	// This is the max height explained above
	private int wHS = 100;

	private Location instance = null;

	protected Location() {
		// singleton design pattern
	}

	/**
	 * The first element of LocationID MUST be 0. This array should correspond
	 * to the indices of the coords array e.g. locationID[1] should = 1, and
	 * should correspond to coords[1]
	 * 
	 * @param locationID
	 *            array of location ID's corresponding to the coords
	 */
	public void setLocationID(int[] locationID) {
		// The 0th element must correspond to 0,0
		// WotsDB takes care of this for us
		if (locationID[0] == 0) {
			this.locationID = locationID;
		} else {
			System.out.println("The first element of Location ID MUST be 0");
		}
	}

	/**
	 * The first element must be {0,0}
	 * 
	 * @param coords
	 *            This 2D array should have the second dimension as length 2.
	 *            For example { {1,2}, {2,2} } would be 2 coordinates. One at
	 *            1,2 and the other at 2,2. There should be no duplicate
	 *            coordinates
	 */
	public void setCoords(int[][] coords) {
		this.coords = coords;
	}

	public Location getInstance() {
		if (instance == null) {
			instance = new Location();

		}
		return instance;
	}

	/*
	 * returns an array of coordinates based off the inputted location ids
	 */
	private int[][] getCoords(int[] locationIDs) {
		int[][] coordArray = new int[locationIDs.length][2];
		for (int i = 0; i < locationIDs.length; i++) {
			coordArray[i] = coords[locationIDs[i]];
		}
		return coordArray;

	}

	/*
	 * returns the minimum distance between a pair of coordinates, using the
	 * length of each aisle (i.e. at what point can we move between aisles)
	 */
	private int minDistance(int[] a, int[] b) {
		int distance = 0;
		if (a[1] - b[1] == 0) {
			distance = Math.abs(a[0] - b[0]);
			return distance;
		}
		if (a[0] + b[0] <= wHS) {
			distance = a[0] + b[0] + Math.abs(b[1] - a[1]);
			return distance;
		} else {
			distance = 20 - a[0] - b[0] + Math.abs(b[1] - a[1]);
			return distance;
		}
	}

	/*
	 * iterates through every pair of coordinates, calling the minDistance
	 * method on each pair of points and returning the distances in an array.
	 */
	private int[] getDistances(int[][] coordArray) {

		int a = coordArray.length;
		int b = a * (a - 1) / 2; // The total number of paths is given by this
									// formula (Triangle numbers).
		int[] distances = new int[b];
		int counter = 0;
		for (int i = 0; i < a; i++) {
			for (int j = i + 1; j < a; j++) {
				distances[counter] = minDistance(coordArray[i], coordArray[j]);
				counter++;
			}
		}
		return distances;
	}

	/*
	 * Same as get distances, however returns coordinates corresponding to each
	 * path.
	 */
	private int[][][] getCoordIndex(int[][] coordArray) {
		int a = coordArray.length;
		int b = a * (a - 1) / 2;
		int[] distances = new int[b];
		int[][][] indexes = new int[b][2][2];
		int counter = 0;
		for (int i = 0; i < a; i++) {
			for (int j = i + 1; j < a; j++) {
				distances[counter] = minDistance(coordArray[i], coordArray[j]);
				indexes[counter][0] = coordArray[i];
				indexes[counter][1] = coordArray[j];
				counter++;
			}
		}
		return indexes;

	}

	/*
	 * runs a bubble sort algorithm on the list of distances and returns the
	 * corresponding sorted indexes. This is needed to reshuffle an array, based on the sort order another array
	 */
	private int[] sortIndex(int[] d) {

		int[] sortedIndexes = new int[d.length];

		for (int i = 0; i < d.length; i++) {
			sortedIndexes[i] = i; // original indexes of length
		}

		for (int i = 0; i < d.length - 1; i++) {
			for (int j = 0; j < d.length - i - 1; j++) {
				if (d[j + 1] < d[j]) {
					int temp1 = d[j];
					d[j] = d[j + 1];
					d[j + 1] = temp1;

					int temp = sortedIndexes[j];
					sortedIndexes[j] = sortedIndexes[j + 1];
					sortedIndexes[j + 1] = temp;
				}
			}
		}

		return sortedIndexes;
	}

	/*
	 * changes the original array to match the order of ind the sorted list
	from sortIndex.
	 */
	private int[][][] shuffleIndexes(int[] ind, int[][][] originalIndex) {

		int[][][] shuffledIndex = new int[ind.length][2][2];

		for (int i = 0; i < ind.length; i++) {
			shuffledIndex[i][0] = originalIndex[ind[i]][0];
			shuffledIndex[i][1] = originalIndex[ind[i]][1];
		}

		return shuffledIndex;
	}

	private boolean checkLoop(int[][] coords, int[][][] tour, int counter, int n) {
		// returns false if the path formed by adding a coordinate is a loop,
		// before we add every coordinate.
		if (counter == 0) {
			return true;
		}
		int tracker1 = 0;
		for (int i = 0; i < counter; i++) {
			if (Arrays.equals(tour[i][0], coords[0])
					|| Arrays.equals(tour[i][0], coords[1])) {
				tracker1++;
			}

			if (Arrays.equals(tour[i][1], coords[0])
					|| Arrays.equals(tour[i][1], coords[1])) {
				tracker1++;
			}

			for (int j = i + 1; j < counter; j++) {
				if (Arrays.equals(tour[i][0], tour[j][0])
						|| Arrays.equals(tour[i][0], tour[j][1])) {
					tracker1++;
				}

				if (Arrays.equals(tour[i][1], tour[j][0])
						|| Arrays.equals(tour[i][1], tour[j][1])) {
					tracker1++;
				}

			}
		}
		if (tracker1 == counter + 1 && counter + 1 != n) {
			return false;
		} else {
			return true;
		}
	}

	private boolean checkValid(int[][] coords, int[][][] trip, int counter,
			int n) {
		// returns false if adding a new path causes a coordinate to appear more
		// than twice or if check loop returns false. If it returns true then
		// the path can be added to the trip.
		int tracker1 = 0;
		int tracker2 = 0;
		for (int i = 0; i < counter; i++) {
			for (int j = 0; j < 2; j++) {
				if (Arrays.equals(coords[0], trip[i][j])) {
					tracker1 = tracker1 + 1;
				}
				if (Arrays.equals(coords[1], trip[i][j])) {
					tracker2 = tracker2 + 1;
				}
			}
		}

		if (checkLoop(coords, trip, counter, n) == false && counter < n
				&& tracker1 < 2 && tracker2 < 2) {
			return false;
		}

		if (tracker1 >= 2 || tracker2 >= 2) {
			return false;
		} else {
			return true;
		}
	}

	private int[][][] greedyAlg(int[] distances, int[][][] shuffledCoords, int n) {
		// runs greedy algorithm, a travelling salesman algorithm
		int[][][] tour = new int[n][2][2];
		int counter = 0;
		int index = 0;
		while (counter < n) {
			int[][] coord = { shuffledCoords[index][0],
					shuffledCoords[index][1] };
			if (checkValid(coord, tour, counter, n)) {
				tour[counter] = coord;
				counter++;
			}
			index++;
		}
		return tour;
	}

	/**
	 * Runs greedy algorithm on a list of locations given by ids.
	 * 
	 * @param iD
	 * @return String of the tour, given as coordinate pairs
	 */
	public String mapRoute(int[] iD) {
		// Ensures the starting point is always 0,0
		int[] iDs = new int[iD.length + 1];
		iDs[0] = 0; // ensures start point is in path
		System.arraycopy(iD, 0, iDs, 1, iD.length);

		if (iDs.length == 2) {
			return printStuff(getCoordIndex(getCoords(iDs)));
		}

		int[] distances = getDistances(getCoords(iDs));
		int[][][] coords = getCoordIndex(getCoords(iDs));
		int[] sortedIndex = sortIndex(distances);
		int[][][] shuffledCoords = shuffleIndexes(sortedIndex, coords);
		int[][][] tour = greedyAlg(distances, shuffledCoords, iDs.length);
		return printStuff(tour);
	}
	

	/*
	 * Used to format string outputs of 3d array of coordinates
	 */
	private String printStuff(int[][][] coords) {
		String s = "";
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					s = s + String.valueOf(coords[i][j][k]) + ",";
				}
				s = s + " ";
			}
			s = s + "\n";

		}
		return s;
	}

}
