import javax.swing.*;
import java.awt.*;

public class PanelGrafo extends JPanel {
    private Grafo grafo;
    private int[] distancias;

    public PanelGrafo(Grafo grafo) {
        this.grafo = grafo;
        this.distancias = null;
    }

    public void setDistancias(int[] distancias) {
        this.distancias = distancias;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar aristas
        for (Arista arista : grafo.getAristas()) {
            Vertice origen = grafo.getVertices().get(arista.origen);
            Vertice destino = grafo.getVertices().get(arista.destino);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(origen.x, origen.y, destino.x, destino.y);

            // Dibujar peso
            int medioX = (origen.x + destino.x) / 2;
            int medioY = (origen.y + destino.y) / 2;
            g2d.drawString(String.valueOf(arista.peso), medioX, medioY);
        }

        // Dibujar vértices
        for (int i = 0; i < grafo.getCantidadVertices(); i++) {
            Vertice v = grafo.getVertices().get(i);

            // Dibujar círculo del vértice
            g2d.setColor(Color.WHITE);
            g2d.fillOval(v.x - 15, v.y - 15, 30, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(v.x - 15, v.y - 15, 30, 30);

            // Dibujar etiqueta del vértice
            g2d.drawString(v.etiqueta, v.x - 5, v.y + 5);

            // Dibujar distancia si está disponible
            if (distancias != null && distancias[i] != Integer.MAX_VALUE) {
                g2d.setColor(Color.BLUE);
                g2d.drawString("d=" + distancias[i], v.x - 10, v.y - 20);
            }
        }
    }
}
