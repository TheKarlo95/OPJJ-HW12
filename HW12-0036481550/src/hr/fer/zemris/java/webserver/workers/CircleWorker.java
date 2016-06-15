package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code CircleWorker} is a worker that creates the {@code png} image f a
 * circle with random color and writes it to context.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see IWebWorker
 */
public class CircleWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = bim.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 200, 200);

        Random rand = new Random();
        g2d.setColor(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        g2d.fillOval(0, 0, 200, 200);

        g2d.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        context.setMimeType("image/png");
        try {
            ImageIO.write(bim, "png", bos);
            context.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
