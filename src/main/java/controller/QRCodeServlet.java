    package controller;

    import com.google.zxing.BarcodeFormat;
    import com.google.zxing.EncodeHintType;
    import com.google.zxing.MultiFormatWriter;
    import com.google.zxing.WriterException;
    import com.google.zxing.client.j2se.MatrixToImageWriter;
    import com.google.zxing.common.BitMatrix;

    import javax.imageio.ImageIO;
    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.*;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.io.OutputStream;
    import java.util.Hashtable;

    @WebServlet(name = "QRCodeServlet", urlPatterns = {"/qrcode"})
    public class QRCodeServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            // Lấy dữ liệu cần mã hóa từ parameter "data"
            String data = request.getParameter("data");
            String sizeParam = request.getParameter("size");
            int size = 200;
            try {
                if (sizeParam != null) {
                    size = Integer.parseInt(sizeParam);
                }
            } catch (NumberFormatException e) {
                size = 200;
            }
            if (data == null || data.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No data provided");
                return;
            }
            try {
                Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints);
                BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
                response.setContentType("image/png");
                OutputStream os = response.getOutputStream();
                ImageIO.write(image, "png", os);
                os.close();
            } catch (WriterException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating QR Code");
            }
        }
    }