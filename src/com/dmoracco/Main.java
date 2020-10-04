package com.dmoracco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Object;

public class Main {

    public static void main(String[] args) {

        validate();
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
}
