package br.com.fga.aco;

public class AntColonyOptimization {

    public static void main(String[] args) {
        Graph grp = new Graph();
        grp.buildGraph();
        // grp.printGraph();
        
        grp.setStart("Como");
        grp.setEnd("Milano");
        
        ShortestACO aco = new ShortestACO(grp);
        aco.start();
    }
    
}
