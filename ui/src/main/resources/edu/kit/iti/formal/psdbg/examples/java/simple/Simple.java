public class Simple{



    /*@ public normal_behavior
      @ requires x >= 0;
      @ ensures \result >0;
      @*/
    public int very_simple(int x){
        if(x > 0){
            x--;
        }else{
            x++;
        }
        return ++x;
    }
}