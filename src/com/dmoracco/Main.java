package com.dmoracco;

import java.util.ArrayList;
import java.util.Random;

import static com.dmoracco.GetCpuTime.getCpuTime;

public class Main {

    public static void main(String[] args) {

        // Validate algorithms
        validate();

        // Testing
        long startTime = 0;
        long endTime = 0;
        long lastBFTime = 0;
        long lastFasterTime = 0;
        long lastFastestTime = 0;
        long currentTime = 0;
        String ratio = "na";

        long maxTime = 10000000;
        int n = 1000000;


        System.out.printf("\n\n%20s%20s%20s%20s\n", "N", "Time (m/s)", "Ratio", "Exp. Ratio");

        for (int i = 1; i < n; i=i*2){
            int[] randomList = generateList(i);


            if (lastBFTime < maxTime) {
                startTime = getCpuTime();
                ArrayList<int[]> foundSums = ThreeSumBruteForce(randomList, i);
                endTime = getCpuTime();
                currentTime = (endTime - startTime) / 1000;

                if (lastBFTime != 0){
                    ratio = String.format("%5.4f", (double) currentTime/lastBFTime);
                }


                System.out.printf("%20s%20s%20s%20s\n", i, currentTime, ratio, "8");
                lastBFTime = currentTime;
            } else System.out.printf("%20s%20s%20s%20s", "na", "na", "na", "na");


        }
    }

    public static void validate() {

        int[] testListKnown = {2, -3, 7, -9, 11, -22, 29, -33, 45, -5};
        ArrayList<int[]> testFoundSums = ThreeSumBruteForce(testListKnown, 10);

        System.out.print("Testing known/small list for BruteForce: ");
        for (int[] list: testFoundSums
             ) {
            printTriple(list);
        }
        System.out.println();
        System.out.print("Testing known/small list for BruteForceBinary: ");
        testFoundSums = ThreeSumFasterBinary(testListKnown, 10);
        for (int[] list: testFoundSums
        ) {
            printTriple(list);
        }
        System.out.println();

        System.out.println("Testing n = 5000 random list BruteForce: ");
        int[] testRandomList = generateList(5000);

        ArrayList<int[]> foundSums = ThreeSumBruteForce(testRandomList, 5000);
        System.out.println("Found: " + foundSums.size());
        if (foundSums.size() > 0){
            for (int[] list: foundSums){
                printTriple(list);
            }
        }

        System.out.println("Testing n = 5000 random list BruteForce: ");

        foundSums = ThreeSumFasterBinary(testRandomList, 5000);
        System.out.println("Found: " + foundSums.size());
        if (foundSums.size() > 0){
            for (int[] list: foundSums){
                printTriple(list);
            }
        }

    }

    public static void printTriple(int[] list){
        for (int i = 0; i < list.length; i++){
            System.out.print(list[i] + ", ");
        }
        System.out.println();
    }

    public static int[] generateList(int n){
       Random r = new Random();
       int[] list = new int[n];

       // Generate initial list
       for (int i = 0; i < n; i++){
           list[i] = r.nextInt();
       }

       // Search for duplicates and replace as needed
       for (int j = 0; j < n; j++){
           for (int k = j+1; k < n-1; k++){
               if (list[j] == list[k]){
                   list[j] = r.nextInt();
                   j = 0;
               }
           }
       }

       return list;

    }

    public static ArrayList ThreeSumBruteForce(int[] list, int n){
        ArrayList sumList = new ArrayList();

        // Iterate through list N^3 times to find sums
        for (int i = 0; i < n-2; i++){
            for (int j = i +1; j < n-1; j++){
                for (int k = j+1; k < n; k++){
                    if ((list[i]+list[j]+list[k] == 0)){
                       sumList.add(new int[] {list[i], list[j], list[k] });
                    }
                }
            }
        }

        return sumList;
    }

    public static ArrayList ThreeSumFasterBinary(int[] list, int n){
        ArrayList sumList = new ArrayList();

        // Sort list first
        int[] sortedList = MergeSort(list);

        // Find parity center, starting a center
        int center = n/2;
        if (sortedList[n/2] == 0) center = n/2;
        else if (sortedList[n/2] > 0){
            // roll back until negative is found, set center as the last positive.
            for (int p = n/2; p >=0; p--){
                if (sortedList[p] < 0) center = p+1;
            }
        }
        else{
            // roll forward until first positive is found
            for (int q = n/2; q < n; q++){
                if (sortedList[q] > 0) center = q;
            }
        }

        // Brute Force, but smarter (maybe). Only iterating through numbers that will bring it closer to zero.
        for (int i = 0; i < n; i++){
            for (int j = n-1; j > 0; j--){
                if (i != j){
                    if (sortedList[i] + sortedList[j] >=0){
                        for (int neg = center-1; neg >= 0; neg--){
                            if (i != neg && j != neg &&(sortedList[i]+sortedList[j]+sortedList[neg] == 0)){
                                sumList.add(new int[] {sortedList[i], sortedList[j], list[neg] });
                            }
                        }
                    }
                    else {
                        for (int pos = center; pos < n; pos++){
                            if (i != pos && j != pos && (sortedList[i]+sortedList[j]+sortedList[pos] == 0)){
                                sumList.add(new int[] {sortedList[i], sortedList[j], sortedList[pos] });
                            }
                        }
                    }
                }
            }
        }
        return sumList;
    }

    static int[] MergeSort(int[] list){

        int size = list.length;

        // One or less is sorted by definition
        if (size <= 1) return list;

        // Create new arrays
        int split = (int) Math.ceil(size/2.0); // get proper size for odd N, 2.0 cause int arithmetic apparently...
        int[] left = new int[split];
        int[] right = new int[size/2];

        // Split list
        int l = 0;
        int r = 0;
        for (int i = 0; i < size; i++){
            if (i < split) left[l++] = list[i];
            else right[r++] = list[i];
        }

        // Recursive calls
        left = MergeSort(left);
        right = MergeSort(right);

        // Merge sorted lists
        return mergeArrays(left, right);

        // Psuedocode from https://en.wikipedia.org/wiki/Merge_sort
    }

    static int[] mergeArrays(int[] left, int[] right){

        int l = 0, r = 0, m = 0;
        int lsize = left.length;
        int rsize = right.length;
        int[] mergedList = new int[lsize + rsize];

        // iterate through list until either list is complete
        while (l < lsize && r < rsize){
            // determine order to fill mergedList
            if (left[l] <= right[r]) mergedList[m++] = left[l++];
            else mergedList[m++] = right[r++];
        }

        // Handle remainder for odd N lists
        while (l < lsize) mergedList[m++] = left[l++];
        while (r < rsize) mergedList[m++] = right[r++];

        return mergedList;
    }

}
