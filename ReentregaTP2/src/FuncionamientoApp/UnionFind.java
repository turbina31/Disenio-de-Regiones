package FuncionamientoApp;

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

    public int find(int i) {
        while (padre[i] != i) {
            padre[i] = padre[padre[i]];
            i = padre[i];
        }
        return i;
    }

    public boolean mismoConjunto(int i, int j) {
        return find(i) == find(j);
    }

    public boolean union(int i, int j) {
        int ri = find(i);
        int rj = find(j);
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