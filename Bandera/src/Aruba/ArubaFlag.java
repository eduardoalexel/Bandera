package Aruba;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class ArubaFlag implements GLEventListener {

    private static final float W = 3.0f;
    private static final float H = 2.0f;

    private static final float[] BLUE   = {0.255f, 0.561f, 0.871f};
    private static final float[] YELLOW = {0.984f, 0.882f, 0.133f};
    private static final float[] RED    = {0.937f, 0.200f, 0.251f};
    private static final float[] WHITE  = {1f, 1f, 1f};

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        int vw = w, vh = (int) (w * H / W);
        if (vh > h) {
            vh = h;
            vw = (int) (h * W / H);
        }
        gl.glViewport((w - vw) / 2, (h - vh) / 2, vw, vh);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, W, 0, H, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Fondo azul
        rect(gl, 0, 0, W, H, BLUE);

        // Dos franjas amarillas
        float stripe = H / 15f;
        rect(gl, 0, 0.40f, W, 0.40f + stripe, YELLOW);
        rect(gl, 0, 0.40f + 2 * stripe, W, 0.40f + 3 * stripe, YELLOW);

        // Estrella: borde blanco + relleno rojo
        float cx = 0.55f, cy = 1.45f;
        star(gl, cx, cy, 0.34f, 0.11f, WHITE);
        star(gl, cx, cy, 0.27f, 0.075f, RED);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) { }

    private void rect(GL2 gl, float x1, float y1, float x2, float y2, float[] c) {
        gl.glColor3f(c[0], c[1], c[2]);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x2, y1);
        gl.glVertex2f(x2, y2);
        gl.glVertex2f(x1, y2);
        gl.glEnd();
    }

    private void star(GL2 gl, float cx, float cy, float outer, float inner, float[] c) {
        gl.glColor3f(c[0], c[1], c[2]);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (int i = 0; i <= 8; i++) {
            double ang = Math.toRadians(i * 45.0);
            float r = (i % 2 == 0) ? outer : inner;
            gl.glVertex2f(cx + (float) (r * Math.cos(ang)),
                          cy + (float) (r * Math.sin(ang)));
        }
        gl.glEnd();
    }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        GLJPanel canvas = new GLJPanel(caps);
        canvas.addGLEventListener(new ArubaFlag());

        JFrame frame = new JFrame("Bandera de Aruba - JOGL");
        frame.getContentPane().add(canvas);
        frame.setPreferredSize(new Dimension(900, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    });
}
}
