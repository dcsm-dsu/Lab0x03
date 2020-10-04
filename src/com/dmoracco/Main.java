package com.dmoracco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        int[] randomList = generateList(100000);


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
    public static List ThreeSumBruteForce(int[] list, int n){
        List sumList = new ArrayList();

        // Iterate through list N^3 times to find sums
        for (int i = 0; i < n-2; i++){
            for (int j = i +1; j < n-1; j++){
                for (int k = j+1; k < n; k++){
                    if ((list[i]+list[j]+list[k] == 0)){
                       int[] threesum = new int[3];
                       threesum[0] = list[i];
                       threesum[1] = list[j];
                       threesum[2] = list[k];
                       sumList.add(threesum);
                    }
                }
            }
        }

        return sumList;
    }
}
