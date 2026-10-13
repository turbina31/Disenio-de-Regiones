package FuncionamientoAplicacion;

public class UnionFind {
    private final int[] padre;
    private final int[] rango;

    public UnionFind(int n) {
        padre = new int[n];
        rango = new int[n];
        for (int i = 0; i < n; i++) {
            padre[i] = i;
        }
    }

    public int root(int i) {
        while (padre[i] != i) {
            padre[i] = padre[padre[i]];
            i = padre[i];
        }
        return i;
    }

    public boolean mismoConjunto(int i, int j) {
        return root(i) == root(j);
    }

    public boolean union(int i, int j) {
        int ri = root(i);
        int rj = root(j);
        if (ri == rj) {
            return false;
        }
        if (rango[ri] < rango[rj]) {
            padre[ri] = rj;
        } else if (rango[ri] > rango[rj]) {
            padre[rj] = ri;
        } else {
            padre[rj] = ri;
            rango[ri]++;
        }
        return true;
    }
}
