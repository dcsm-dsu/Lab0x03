package com.dmoracco;

import java.util.ArrayList;
import java.util.Arrays;
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

        long maxTime = 300000;
        int n = 1000000;


        System.out.printf("\n\n%20s%20s%20s%20s\n", "N", "Time (m/s)", "Ratio", "Exp. Ratio");

        for (int i = 1; i < n; i=i*2){
            if (lastBFTime >= maxTime && lastFasterTime >= maxTime && lastFastestTime >= maxTime) break;

            int[] randomList = generateList(i);


            // Brute Force
            if (lastBFTime < maxTime) {
                startTime = getCpuTime();
                ArrayList<int[]> foundSums = ThreeSumBruteForce(randomList, i);
                endTime = getCpuTime();
                currentTime = (endTime - startTime) / 1000000;

                if (lastBFTime != 0){
                    ratio = String.format("%5.4f", (double) currentTime/lastBFTime);
                }


                System.out.printf("%15s%15s%15s%15s", i, currentTime, ratio, "8");
                lastBFTime = currentTime;
            } else System.out.printf("%15s%15s%15s%15s", "na", "na", "na", "na");

            String n2log = String.format("%5.2f", (i*i*Math.log(i))/((i/2)*(i/2)*Math.log(i/2)));
            // Faster
            if (lastFasterTime < maxTime) {
                startTime = getCpuTime();
                ArrayList<int[]> foundSums = ThreeSumFasterBinary(randomList, i);
                endTime = getCpuTime();
                currentTime = (endTime - startTime) / 1000000;

                if (lastFasterTime != 0){
                    ratio = String.format("%5.2f", (double) currentTime/lastFasterTime);
                }


                System.out.printf("%15s%15s%15s%15s", i, currentTime, ratio, n2log);
                lastFasterTime = currentTime;
            } else System.out.printf("%15s%15s%15s%15s", "na", "na", "na", "na");

            // Fast (from textbook)
            if (lastFastestTime < maxTime) {
                startTime = getCpuTime();
                ArrayList<int[]> foundSums = ThreeSumFast(randomList, i);
                endTime = getCpuTime();
                currentTime = (endTime - startTime) / 1000000;

                if (lastFastestTime != 0){
                    ratio = String.format("%5.2f", (double) currentTime/lastFastestTime);
                }


                System.out.printf("%15s%15s%15s%15s\n", i, currentTime, ratio, n2log);
                lastFastestTime = currentTime;
            } else System.out.printf("%15s%15s%15s%15s\n", "na", "na", "na", "na");


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
        System.out.print("Testing known/small list for ThreeSumFasterBinary: ");
        testFoundSums.clear();
        testFoundSums = ThreeSumFasterBinary(testListKnown, 10);
        for (int[] list: testFoundSums
        ) {
            printTriple(list);
        }
        System.out.println();
        System.out.print("Testing known/small list for ThreeSumFast: ");
        testFoundSums.clear();
        testFoundSums = ThreeSumFast(testListKnown, 10);
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

        System.out.println("Testing n = 5000 random list ThreeSumFasterBinary: ");
        foundSums.clear();
        foundSums = ThreeSumFasterBinary(testRandomList, 5000);
        System.out.println("Found: " + foundSums.size());
        if (foundSums.size() > 0){
            for (int[] list: foundSums){
                printTriple(list);
            }
        }

        System.out.println("Testing n = 5000 random list ThreeSumFast: ");
        foundSums.clear();
        foundSums = ThreeSumFast(testRandomList, 5000);
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
        //int[] sortedList = MergeSort(list);
        Arrays.sort(list);


        // Find parity center, starting a center
        int center = n/2;
        if (list[n/2] == 0) center = n/2;
        else if (list[n/2] > 0){
            // roll back until negative is found, set center as the last positive.
            for (int p = n/2; p >=0; p--){
                if (list[p] < 0) {
                    center = p+1;
                    break;
                }
            }
        }
        else{
            // roll forward until first positive is found
            for (int q = n/2; q < n; q++){
                if (list[q] > 0){
                    center = q;
                    break;
                }
            }
        }

        // Brute Force, but smarter (maybe). Only iterating through numbers that will bring it closer to zero.

        // iterate to middle
        for (int i = 0; i < n/2; i++){
            // iterate in reverse, thus "centering" the number with the largest and smallest
            // iterate until it reaches i
            for (int j = n-1; j > i; j--){
                // determine parity
                if (list[i] + list[j] >=0){
                    // parity positive, iterate only though negative numbers
                    for (int neg = center-1; neg >= 0; neg--){
                        if (i != neg && j != neg &&(list[i]+list[j]+list[neg] == 0)){
                            sumList.add(new int[] {list[i], list[j], list[neg] });
                        } else if (list[i] + list[j] + list[neg] < 0) neg = -1; // break if gone too far
                    }
                }
                else {
                    // parity negative, iterate only through positive numbers
                    for (int pos = center; pos < n; pos++){
                        if (i != pos && j != pos && (list[i]+list[j]+list[pos] == 0)){
                            sumList.add(new int[] {list[i], list[j], list[pos] });
                        } else if (list[i] + list[j] + list[pos] > 0) pos = n; // break if gone too far
                    }
                }
            }
        }

        return sumList;
    }

    static ArrayList ThreeSumFast(int[] list, int n){
        ArrayList sumList = new ArrayList();

        // From text book to reference other algorithms
        Arrays.sort(list);
        int cnt = 0;
        int key = -1;
        for (int i = 0; i < n; i++){
            for (int j = i+1; j < n; j++){
                // Search for only possible match using binary search
                key = binarySearch(list, -(list[i]+list[j]));
                if (key >= 0){
                    sumList.add(new int[] {list[i], list[j], list[key]});
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

    public static int binarySearch(int[] list, int key){
        int i = 0;
        int j = list.length / 2;
        int k = list.length - 1;

        while (i <= k){
            if (key == list[j]) return j;
            else {
                if (list[j] < key) i = j+1;
                else k = j-1;
                j = (i+k)/2;
            }
        }
        return -1;

    }
}
