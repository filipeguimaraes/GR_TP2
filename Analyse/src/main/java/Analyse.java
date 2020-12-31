public class Analyse {

    private static Analyse analyse = null;

    public static Analyse getInstance(){
        if(analyse == null){
            analyse = new Analyse();
        }
        return analyse;
    }



}
