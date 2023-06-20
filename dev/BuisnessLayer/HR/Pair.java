package BuisnessLayer.HR;

 public class Pair<A, B> {
     private final A first;
     private final B second;

     public Pair(A first, B second) {
         this.first = first;
         this.second = second;
     }

     public A getKey() {
         return first;
     }

     public B getValue() {
         return second;
     }
 }
