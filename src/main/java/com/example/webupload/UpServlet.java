package com.example.webupload;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
@MultipartConfig(
        location="/",
        fileSizeThreshold=1024*1024,    // 1MB *
        maxFileSize=1024*1024*100,      // 100MB **
        maxRequestSize=1024*1024*10*10  // 100MB ***
)
@WebServlet(name = "upServlet", value = "/up-servlet")
public class UpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        //super.doPost(request, response);
        PrintWriter writer=null;
        try {
            writer = response.getWriter();

            response.setContentType("text/html;charset=UTF-8");
            // lê a pasta de destino
            String pasta = request.getParameter("foldername");
            String novonome=request.getParameter("newname");
            Part filePart = request.getPart("filename");  // Lê o arquivo de upload
            //String fileName = filePart.getSubmittedFileName();

            OutputStream out = null;
            InputStream filecontent = null;

            File fpasta = new File(getServletContext().getRealPath("/") + "/" + pasta);
            fpasta.mkdir();
            out = new FileOutputStream(new File(fpasta.getAbsolutePath() + "/" + novonome));
            filecontent = filePart.getInputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("Novo arquivo " + novonome + " criado na pasta " + pasta);
            out.close();
            filecontent.close();
            writer.close();
        } catch (Exception fne) {
            writer.println("Erro ao receber o arquivo");
            writer.println("<br/> ERRO: " + fne.getMessage());
        }
    }
}
