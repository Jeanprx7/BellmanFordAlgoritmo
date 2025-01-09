import java.util.*;

public class Grafo {
    private ArrayList<Vertice> vertices;
    private ArrayList<Arista> aristas;
    private int[][] distancias;
    private int cantidadVertices;

    public Grafo(int maxVertices) {
        vertices = new ArrayList<>();
        aristas = new ArrayList<>();
        distancias = new int[maxVertices][maxVertices];
        cantidadVertices = 0;

        // Inicializar matriz de distancias
        for (int i = 0; i < maxVertices; i++) {
            Arrays.fill(distancias[i], Integer.MAX_VALUE);
            distancias[i][i] = 0;
        }
    }

    public void agregarVertice(int x, int y) {
        String etiqueta = String.valueOf(cantidadVertices);
        vertices.add(new Vertice(x, y, etiqueta));
        cantidadVertices++;
    }

    public void agregarArista(int origen, int destino, int peso) {
        if (origen >= 0 && origen < cantidadVertices &&
                destino >= 0 && destino < cantidadVertices) {
            aristas.add(new Arista(origen, destino, peso));
            distancias[origen][destino] = peso;
        }
    }

    public ResultadoBellmanFord bellmanFord(int inicio) {
        if (inicio < 0 || inicio >= cantidadVertices) {
            return new ResultadoBellmanFord(null, "Vértice de inicio inválido");
        }

        int[] distancia = new int[cantidadVertices];
        int[] predecesor = new int[cantidadVertices];
        Arrays.fill(distancia, Integer.MAX_VALUE);
        Arrays.fill(predecesor, -1);
        distancia[inicio] = 0;

        // Relajar aristas V-1 veces
        for (int i = 1; i < cantidadVertices; i++) {
            for (Arista arista : aristas) {
                if (distancia[arista.origen] != Integer.MAX_VALUE) {

                    long suma = (long) distancia[arista.origen] + arista.peso;
                    if (suma < Integer.MIN_VALUE || suma > Integer.MAX_VALUE) {
                        continue;
                    }

                    if (distancia[arista.origen] + arista.peso < distancia[arista.destino]) {
                        distancia[arista.destino] = distancia[arista.origen] + arista.peso;
                        predecesor[arista.destino] = arista.origen;
                    }
                }
            }
        }

        // Verificar ciclos negativos
        for (int i = 0; i < cantidadVertices - 1; i++) {
            for (Arista arista : aristas) {
                if (distancia[arista.origen] != Integer.MAX_VALUE &&
                        distancia[arista.origen] + arista.peso < distancia[arista.destino]) {
                    return new ResultadoBellmanFord(null, "El grafo contiene un ciclo de peso negativo");
                }
            }
        }

        return new ResultadoBellmanFord(distancia, "Éxito");
    }

    public ArrayList<Vertice> getVertices() {
        return vertices;
    }

    public ArrayList<Arista> getAristas() {
        return aristas;
    }

    public int getCantidadVertices() {
        return cantidadVertices;
    }
}