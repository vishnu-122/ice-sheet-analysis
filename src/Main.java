/*I Vishnu M Marwadi, certify that this material is my original work.
 No other person's work has been used without suitable acknowledgment and I have not made my work available to anyone else. */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Analyzes data collected from ice sheets on Planet Clara.
 * Determines potential fracture points and crack points in the ice sheets.
 * Calculates statistics such as total fracture points, sheet with the most fracture points,
 * total crack points, and fraction of fracture points that are also crack points.
 */
public class Main {
    /**
     * Reads data from a file, analyzes the ice sheets, and calculates statistics.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        //attempting to read the file
        File file = new File("src/ICESHEETS .TXT");

        int totalFracturePoints = 0;
        int maxFracturePoints = 0;
        int sheetWithMaxFracture = 0;
        int totalCrackPoints = 0;

        try{
            Scanner scanner = new Scanner(file);
            int numberOfIcesheet = scanner.nextInt();

            //iterating through the icesheet to build the 2d array
            for (int sheet=1;sheet<=numberOfIcesheet;sheet++){
                int rows = scanner.nextInt();
                int columns = scanner.nextInt();
                int[][] arrayOfSheets = new int[rows][columns];

                for (int i = 0;i<rows;i++){
                    for(int j =0;j<columns;j++){
                        if (scanner.hasNextInt()){
                            arrayOfSheets[i][j] = scanner.nextInt();
                        }
                    }
                }
                int[][] fractureIndices = new int[rows * columns][2];
                int fracturePoints = totalFracturePoints(arrayOfSheets,fractureIndices);
                totalCrackPoints += countCrackPoints(arrayOfSheets,fractureIndices,fracturePoints);

                //to store the indices from the totalFracturePoints method
                totalFracturePoints+= fracturePoints;
                if (fracturePoints > maxFracturePoints){
                    maxFracturePoints = fracturePoints;
                    sheetWithMaxFracture = sheet;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Output the total fracture points and the sheet with the most fracture points
        System.out.println("Total Fracture Points: " + totalFracturePoints);
        System.out.println("Sheet with the Most Fracture Points: Sheet #" + sheetWithMaxFracture + " (" + maxFracturePoints + " points)");
        System.out.println("Total Crack Points: " + totalCrackPoints);

        // Calculate and display the fraction of fracture points that are also crack points
        double fractionOfCrackPoints = (double) totalCrackPoints / totalFracturePoints;
        System.out.println("Fraction of fracture points that are also crack points is " + String.format("%.3g%n", fractionOfCrackPoints));
    }

    /**
     * Determines the total number of fracture points in the ice sheets.
     *
     * @param arrayOfSheets the 2D array representing the ice sheets
     * @param fractureIndices array to store indices of fracture points
     * @return the total number of fracture points
     */
    public static int totalFracturePoints(int[][] arrayOfSheets,int[][]fractureIndices){
        int counter = 0;
        for(int i = 0;i < arrayOfSheets.length;i++){
            for (int j =0;j < arrayOfSheets[i].length;j++){
                if (arrayOfSheets[i][j] <= 200 && arrayOfSheets[i][j] % 50 == 0){
                    fractureIndices[counter][0] = i;
                    fractureIndices[counter][1] = j;
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Counts the number of crack points among the fracture points.
     *
     * @param arrayOfSheets the 2D array representing the ice sheets
     * @param fractureIndices array containing indices of fracture points
     * @param fracturePoints the total number of fracture points
     * @return the number of crack points
     */
    public static int countCrackPoints(int[][] arrayOfSheets, int[][] fractureIndices, int fracturePoints) {
        int crackPoints = 0;
        for (int i = 0; i < fracturePoints; i++) {
            int x = fractureIndices[i][0];
            int y = fractureIndices[i][1];
            if (isPotentialCrack(arrayOfSheets, x, y)) {
                crackPoints++;
                System.out.println("Potential Crack Point Summary: Sheet #" + (i + 1) + " (" + x + ", " + y + ")");
            }
        }
        return crackPoints;
    }

    /**
     * Checks if a fracture point could lead to a crack based on neighboring points.
     *
     * @param arrayOfSheets the 2D array representing the ice sheets
     * @param x row index of the fracture point
     * @param y column index of the fracture point
     * @return true if the fracture point could lead to a crack, false otherwise
     */
    public static boolean isPotentialCrack(int[][] arrayOfSheets, int x, int y) {
        int rows = arrayOfSheets.length;
        int columns = arrayOfSheets[0].length;

        // Check neighboring points including diagonals
        for (int i = Math.max(0, x - 1); i <= Math.min(rows - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(columns - 1, y + 1); j++) {
                if ((i != x || j != y) && arrayOfSheets[i][j] % 10 == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
