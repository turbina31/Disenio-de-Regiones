package Interfaz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class EstiloBoton {

    // ===== Paleta =====
    public static final Color VIOLETA_FONDO      = new Color(167, 139, 250);
    public static final Color VIOLETA_OSCURO     = new Color(30, 27, 75);
    public static final Color VIOLETA_BOTON      = new Color(139, 92, 246);
    public static final Color VIOLETA_BOTON_HOVER = new Color(167, 139, 250);
    public static final Color FONDO_PANEL        = new Color(245, 243, 255);
    public static final Color TEXTO_CLARO        = Color.WHITE;
    public static final Color TEXTO_OSCURO       = new Color(30, 27, 75);

    public static final Color[] PALETA_REGIONES = {
        new Color(139, 92, 246),  // violeta
        new Color(244, 114, 182), // rosa
        new Color(56, 189, 248),  // celeste
        new Color(52, 211, 153),  // verde
        new Color(251, 146, 60),  // naranja
        new Color(248, 113, 113), // rojo suave
        new Color(167, 139, 250),
        new Color(192, 132, 252),
        new Color(251, 191, 36)   // amarillo
    };

    /**
     * Crea un botón con el estilo de la aplicación.
     * @param texto      texto del botón
     * @param principal  true = botón destacado (Ejecutar, Ver Árbol)
     */
    public static JButton crearBoton(String texto, boolean principal) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(TEXTO_CLARO);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        Color colorNormal = principal ? VIOLETA_BOTON : VIOLETA_OSCURO;
        btn.setBackground(colorNormal);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(VIOLETA_BOTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorNormal);
            }
        });

        return btn;
    }
}