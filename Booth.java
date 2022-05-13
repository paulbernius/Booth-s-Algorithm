import java.util.Arrays;
import java.util.Scanner;

/*
Paul Bernius
A20296830
CS 3443
pgm-booth
04/08/22
 */

/*
I had to pass copies of arrays a lot because java is pass by value
but when an array is passed, it passed the reference address to that array.
So if an array is passed and modified in the method, it will be modified globally
instead of just inside the function. To fix this I passed copies of the passed array.
 */

/*
References:
https://www.youtube.com/watch?v=QFXaddi-Ag8&t=254s
 */


public class Booth {
    public static void main(String[] args) {

        // Initialize variables
        String multiplicand;
        int[] m = {0, 0, 0, 0};
        String multiplier;
        int[] q = {0, 0, 0, 0};
        int q0 = 0;
        int[] a = {0, 0, 0, 0};

        // Scanner to get user inputs
        Scanner scnr = new Scanner(System.in);

        while (true) { // Loop to continue running until 'q' is entered
            // Get multiplicand from user and insert into array
            System.out.print("Enter multiplicand (Enter 'q' to quit): ");
            multiplicand = scnr.next();

            // If multiplicand == 'q', quit
            if (multiplicand.toUpperCase().equals("Q")) {
                break;
            }

            for (int i = 0; i < m.length; i++) {
                m[i] = Character.getNumericValue(multiplicand.charAt(i));
            }

            // Print decimal value of inputted binary number
            System.out.println("=> " + getDecimalValue(Arrays.copyOf(m, m.length)));

            // Get multiplier from user and insert into array
            System.out.print("Enter multiplier: ");
            multiplier = scnr.next();

            for (int i = 0; i < q.length; i++) {
                q[i] = Character.getNumericValue(multiplier.charAt(i));
            }

            // Print decimal value of inputted binary number
            System.out.println("=> " + getDecimalValue(Arrays.copyOf(q, q.length)));

            // temp array to print the first product
            int[] temp = new int[a.length + q.length];
            System.arraycopy(a, 0, temp, 0, 4);
            System.arraycopy(q, 0, temp, 4, 4);

            // First iteration print
            System.out.println("Iteration 0: multiplicand " + arrayToString(Arrays.copyOf(m, m.length)) + " (" + getDecimalValue(Arrays.copyOf(m, m.length)) + "); multiplier " + arrayToString(Arrays.copyOf(q, q.length)) + " (" + getDecimalValue(Arrays.copyOf(q, q.length)) + "); product " + arrayToString(Arrays.copyOf(temp, temp.length)) + " (" + getDecimalValue(Arrays.copyOf(temp, temp.length)) + ");");

            // Call Booth's algorithm
            BoothAlg(m, q, q0, a);
        }
    }

    public static void BoothAlg(int[] m, int[] q, int q0, int[] a) {

        // Multiplier constant
        int[] qConstant = Arrays.copyOf(q, q.length);

        // Computer -M
        int[] negM = TwosComp(Arrays.copyOf(m, m.length));

        // if 10, A = A-M; if 01, A = A+M, if 00 or 11, continue
        for (int i = 1; i < m.length + 1; i ++) {
            if (q[3] == 1 && q0 == 0) {
                a = Add(a, negM);
            } else if (q[3] == 0 && q0 == 1) {
                a = Add(a, m);
            }

            // Manually shift q0
            q0 = q[3];

            // Shift A & multiplier
            int[] p = ShiftToRight(a, q);

            // Decompose arrays
            System.arraycopy(p, 0, a, 0, 4);
            System.arraycopy(p, 4, q, 0, 4);

            // Iteration prints
            System.out.println("Iteration " + i + ": multiplicand " + arrayToString(m) + " (" + getDecimalValue(Arrays.copyOf(m, m.length)) +"); multiplier " + arrayToString(qConstant) + " (" + getDecimalValue(Arrays.copyOf(qConstant, qConstant.length)) + "); product " + arrayToString(p) + " (" + getDecimalValue(Arrays.copyOf(p, p.length)) + ");");

        }

        // Print first part of final result
        for (int i = 0; i < 4; i++) {
            System.out.print(a[i]);
        }

        // Print second part of final result
        System.out.print(" ");
        for (int i = 0; i < 4; i++) {
            System.out.print(q[i]);
        }

        // Create result array so it can be passed to getDecimalValue method
        int[] resultToDecimalArray = new int[a.length + q. length];

        // Copy array from a & mulitplier
        System.arraycopy(a, 0, resultToDecimalArray, 0, a.length);
        System.arraycopy(q, 0, resultToDecimalArray, 4, q.length);

        // Print decimal value
        System.out.println(" => " + getDecimalValue(resultToDecimalArray));
    }

    public static int[] TwosComp(int[] s) {

        int addOne = 1; // Bit to be added

        // Flip each bit
        for (int i = s.length - 1; i >= 0; i--) {
            if (s[i] == 0) {
                s[i] = 1;
            } else {
                s[i] = 0;
            }
        }

        int index = s.length - 1;

        // Find where the additional '1' can go and insert it.
        while (addOne == 1) {
            if (s[index] == 0) {
                s[index] = 1;
                addOne--;
            } else {
                s[index] = 0;
                index--;
            }
        }

        return s;
    }

    public static int[] Add(int[] a, int[] b) {

        int carryIn = 0;
        String result = "";
        int[] resultArray = {0, 0, 0, 0};
        int[] carryOut = {0, 0, 0, 0};

        for (int i = 3; i >= 0; i--) {

            carryOut[i] = a[i] + b[i] + carryIn; // Call ADD

            if (carryOut[i] == 3) { // If result is 3, then ADD executed 1 + 1 + 1, meaning carryOut = 1, and result = 1
                result = "1" + result;
                resultArray[i] = 1;
                carryOut[i] = 1;
            } else if (carryOut[i] == 2) { // If result is 2, then ADD executed 1 + 1, meaning carryOut = 1
                result = "0" + result;
                resultArray[i] = 0;
                carryOut[i] = 1;
            } else if (carryOut[i] == 1) { // If result is 1, then ADD executed 1 + 0, meaning carryOut = 0
                result = "1" + result;
                resultArray[i] = 1;
                carryOut[i] = 0;
            } else { // If result is 0, then ADD executed 0 + 0, meaning carryOut = 0
                result = "0" + result;
                resultArray[i] = 0;
                carryOut[i] = 0;
            }
            carryIn = carryOut[i];
        }

        return resultArray; // Return result in array
    }

    public static int[] ShiftToRight(int[] a,int[] b){

        // Assign temp to first value in array, since it can't be modified in the for loop.
        int temp = a[0];
        // Create new array with values from a & b
        int[]c = new int[a.length+b.length];

        // Copy arrays to new array
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, 4, b.length);

        // Shift using c[i] = c[i-1]
        for (int i = c.length - 1; i > 0; i--) {
            c[i] = c[i-1];
        }

        // Manually insert the first value from temp
        a[0] = temp;

        return c;
    }

    public static int getDecimalValue(int[] x) {

        String binaryString = "";
        // Boolean to determine negation
        boolean isNegative = false;

        // If first value is '1', the value will be negative.
        if (x[0] == 1) {
            isNegative = true;
            x = TwosComp(x);
        }

        // Convert array to string
        for (int i = 0; i < x.length; i++) {
            binaryString += x[i];
        }

        // Convert string to int
        int binarynumber = Integer.parseInt(binaryString);
        // Initialize result int
        int decimalnumber = 0;
        // Power variable to keep track of position
        int power = 0;

        while(true) {
            if (binarynumber == 0) {
                break;
            } else {
                int temp = binarynumber%10;
                decimalnumber += temp*Math.pow(2, power);
                binarynumber = binarynumber/10;
                power++;
            }
        }

        // If negation flag is flagged subtract the value by two times the value to get negative value.
        if (isNegative) {
            decimalnumber = decimalnumber - (2 * decimalnumber);
        }

        return decimalnumber;

    }

    // Array to String method to get correct format
    // Arrays.toString(Array) outputs in the form [x, x, x, x]
    // Whereas this user created method outputs xxxx
    public static String arrayToString(int[] a) {
        // Initialize result string
        String result = "";

        // Copy array to string using for loop, looping for the length of the array
        for (int i = 0; i < a.length; i++) {
            if ((i%4) == 0 && a.length > 4) { // If fourth digit, add space
                result = result + " ";
            }
            if (a[i] == 0) {
                result = result + "0";
            } else if (a[i] == 1) {
                result = result + "1";
            } else {
                result = result + "X"; // If niether a 1 or 0, X for error.
            }
        }

        return result;
    }
}
