import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class RotatingSquareFrame {
    private static final Log log = LogFactory.getLog(RotatingSquareFrame.class);

    private static float angleX = 0;
    private static float angleZ = 0;
    private static final int SIZE = 320;
    private final float textXCoords = 75.0f;
    private int angleY = 0;
    private TextRenderer renderer;
    private Texture tex;
    private TextRenderer renderer1;
    private TextRenderer renderer2;
    private TextRenderer renderer3;
    private int cFloatB=0;
    private int cFloatR=0;
    private int cFloatG=0;
    private int frame;
    private boolean repeat;

    RotatingSquareFrame() {
        GLCanvas canvas = getGLCanvas();
        canvas.addGLEventListener(new RotatingSquareListener());
        Animator anim = new Animator(canvas);
        addCanvasToFrame(canvas, anim);

        anim.start();
    }

    private void addCanvasToFrame(
            GLCanvas canvas, final Animator anim) {
        Frame f = new Frame("Über dieses Programm");
        f.setSize(400, 400);
        f.add(canvas);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                anim.stop();
                System.exit(0);
            }
        });
    }

    private GLCanvas getGLCanvas() {
        return new GLCanvas();
    }


    public static void main(String[] args) {
        new RotatingSquareFrame();
    }

    private Texture loadTexture(String fnm)
    {
        Texture tex = null;
        BufferedImage im = null;
        try
        {
            im = ImageIO.read(getClass().getResource(fnm));
            tex = TextureIO.newTexture(im, false);
            tex.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            tex.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        }
        catch (Exception e)
        {
            log.error("Error loading texture " + fnm);
        }
        return tex;
    }

    private void drawCenteredCube(GL gl) {
        //gl.glColor4f(1, 0, 0, 0);
        drawSquareFace(gl);
        //gl.glColor4f(1, 1, 0, 0);
        gl.glRotatef(90, 1, 0, 0);
        drawSquareFace(gl);
        //gl.glColor4f(0, 0, 1, 0);
        gl.glRotatef(90, 0, 1, 0);
        drawSquareFace(gl);
        //gl.glColor3f(0.0f, 0.0f, 0.0f);
    }

    private void drawSquareFace(GL gl) {
        boolean enableLightsAtEnd = false;

        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL.GL_GREATER, 0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        TextureCoords tc = tex.getImageTexCoords();
        tex.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(tc.left(), tc.bottom());
        gl.glVertex3f(-SIZE / 2, -SIZE / 2, SIZE / 2);
        gl.glTexCoord2f(tc.right(), tc.bottom());
        gl.glVertex3f(-SIZE / 2, SIZE / 2, SIZE / 2);
        gl.glTexCoord2f(tc.right(), tc.top());
        gl.glVertex3f(SIZE / 2, SIZE / 2, SIZE / 2);
        gl.glTexCoord2f(tc.left(), tc.top());
        gl.glVertex3f(SIZE / 2, -SIZE / 2, SIZE / 2);
        gl.glEnd();
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glDisable(GL.GL_ALPHA);
        gl.glDisable(GL.GL_BLEND);
        gl.glEnable(GL.GL_LIGHTING);
    }

    class RotatingSquareListener implements GLEventListener {

        public void init(GLAutoDrawable drawable) {
            GL gl = drawable.getGL();
            gl.setSwapInterval(1);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //erasing color
            gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color

            //Lichtquelle
            float light_ambient[] =
            { 0.0f, 0.0f, 0.0f, 1.0f };
            float light_diffuse[] =
            { 1.0f, 1.0f, 1.0f, 1.0f };
            float light_specular[] =
            { 1.0f, 1.0f, 1.0f, 1.0f };
            float light_position[] =
            { 1.0f, 1.0f, 1.0f, 0.0f };

            gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
            gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
            gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light_specular, 0);
            gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);

            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_LIGHT0);
            gl.glDepthFunc(GL.GL_LESS);
            gl.glEnable(GL.GL_DEPTH_TEST);
           
            renderer = new TextRenderer(new Font("Arial", Font.BOLD, 20));
            renderer1 = new TextRenderer(new Font("Tahoma", Font.BOLD, 14));
            renderer2 = new TextRenderer(new Font("Verdana", Font.BOLD, 10));
            renderer3 = new TextRenderer(new Font("Comic Sans MS", Font.BOLD, 10));
            tex = loadTexture("Folder.jpg");
            gl.glShadeModel(GL.GL_SMOOTH);
        }

        public void display(GLAutoDrawable drawable) {
            GL gl = drawable.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            //renderer.dispose();
            //drawCenteredCube(gl);
            angleX++;
            if (angleX % 4 == 0) {
                if (angleZ > 850) {
                    angleZ = 0;
                } else {
                    angleZ++;
                }
            }
            if (angleX % 5 == 0)
                angleY++;
            gl.glMatrixMode(GL.GL_MODELVIEW0_EXT);
            gl.glLoadIdentity();
            gl.glPushMatrix();
            gl.glRotatef(-80, 1, 1, 0);
            gl.glRotatef(angleX / 16, 1, -1, 1);
            gl.glPushMatrix();
            gl.glTranslatef(-0.75f, 0.5f, 0.0f);
            drawCenteredCube(gl);
            gl.glPopMatrix();
            textRenderer(drawable, angleZ);
            textNamesRenderer(drawable,angleX,angleZ);
            textRendererFooter(drawable,angleZ);
            textRTRenderer(drawable,angleZ,getPulseColor());
            frame++;
            gl.glPopMatrix();
            gl.glFlush();
        }

        private Color getPulseColor() {
            if(cFloatB < 255 && frame%10 == 0 && repeat && cFloatB>0){
                cFloatR++;
                cFloatG++;
                cFloatB++;
            }
             else if(cFloatB < 255 && frame%10 == 0 && !repeat && cFloatB>0){
                cFloatR--;
                cFloatG--;
                cFloatB--;

             } else if(cFloatB == 255 && frame%10 == 0){
                cFloatR--;
                cFloatG--;
                cFloatB--;
                repeat = false;
             }
             else if(cFloatB == 0 && frame%10 == 0){
                cFloatR++;
                cFloatG++;
                cFloatB++;
                repeat = true;
            }

            return new Color(cFloatR,cFloatG,cFloatB);
        }

        private void textRenderer(GLAutoDrawable drawable, float angleZ) {
            renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
            renderer.setColor(new Color(cFloatR,0,0));
            renderer.draw3D("Hibernate-Programmierung", textXCoords-20, angleZ, 0.1f, 1.0f);
            renderer.draw3D("Datenbank", textXCoords-20, angleZ-50, 0.1f, 1.0f);
            renderer.draw3D("GUI-Programmierung", textXCoords-20, angleZ-100, 0.1f, 1.0f);
            renderer.draw3D("Animationen & Grafik", textXCoords-20, angleZ-150, 0.1f, 1.0f);
            renderer.draw3D("Musik", textXCoords-20, angleZ-200, 0.1f, 1.0f);
            //renderer.flush();
            renderer.endRendering();
        }

        private void textNamesRenderer(GLAutoDrawable drawable,float angleX, float angleZ){
            renderer1.beginRendering(drawable.getWidth(),drawable.getHeight());
            renderer1.setColor(new Color(255-cFloatR,255,255-cFloatG));
            renderer1.draw3D("Roman Stumm", textXCoords, angleZ-20, 0.1f, 1.0f);
            renderer1.draw3D("Roman Stumm", textXCoords, angleZ-70, 0.1f, 1.0f);
            renderer1.draw3D("Stephan Pudras", textXCoords, angleZ-120, 0.1f, 1.0f);
            renderer1.draw3D("Stephan Pudras", textXCoords, angleZ-170, 0.1f, 1.0f);
            renderer1.draw3D("\"Swamped\" by Lacuna Coil", textXCoords, angleZ-220, 0.1f, 1.0f);
            renderer1.draw3D("(C) Century Media Records", textXCoords, angleZ-240, 0.1f, 1.0f);
            //renderer1.flush();
            renderer1.endRendering();
        }

        private void textRendererFooter(GLAutoDrawable drawable, float angleZ){
            renderer2.beginRendering(drawable.getWidth(),drawable.getHeight());
            renderer2.setColor(Color.YELLOW);
            renderer2.draw3D("jogl.dev.java.net", textXCoords, angleZ-350, 0.1f, 1.0f);
            renderer2.draw3D("www.hibernate.com", textXCoords, angleZ-370, 0.1f, 1.0f);
            renderer2.draw3D("www.apache.org", textXCoords, angleZ-390, 0.1f, 1.0f);
            renderer2.draw3D("www.jetbrains.com", textXCoords, angleZ-410, 0.1f, 1.0f);
            renderer2.draw3D("www.sun.com", textXCoords, angleZ-430, 0.1f, 1.0f);
            renderer2.draw3D("www.elusivenotion.com", textXCoords, angleZ-480, 0.1f, 1.0f);
            //renderer1.flush();
            renderer2.endRendering();
        }

        private void textRTRenderer(GLAutoDrawable drawable, float angleZ, Color color){
            renderer3.beginRendering(drawable.getWidth(),drawable.getHeight());
            renderer3.setColor(color);
            renderer3.draw3D("realtime", drawable.getWidth()-45, 5, 0.1f, 1.0f);
            renderer3.endRendering();
        }


        public void reshape(GLAutoDrawable drawable,
                            int x,
                            int y,
                            int width,
                            int height) {
            GL gl = drawable.getGL();
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(-width, width, -height, height, -SIZE, SIZE);
        }

        public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {
            // do nothing
        }

        public void displayChanged(GLDrawable drawable,
                                   boolean modeChanged,
                                   boolean deviceChanged) {
        }
    }
}