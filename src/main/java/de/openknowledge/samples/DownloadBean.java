package de.openknowledge.samples;

import org.apache.commons.io.IOUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Named
@RequestScoped
public class DownloadBean implements Serializable {

  public static final String MIMETYPE_PDF = "application/pdf";

  private String filename;

  protected DownloadBean() {
  }

  public void getFile(String filename) throws IOException {
    setFilename(filename);
    getFile();
  }

  public void getFile() throws IOException {
    FacesContext faces = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
    ServletOutputStream out = response.getOutputStream();

    DataBuffer buffer = getDataBuffer();
    response.setContentType(buffer.getContentType());
    response.setContentLength(buffer.getData().length);
    response.setHeader("Content-disposition", "attachment;filename=" + filename);
    out.write(buffer.getData());
    out.flush();
    out.close();

    faces.responseComplete();
  }

  private DataBuffer getDataBuffer() throws IOException {
    byte[] data = IOUtils.toByteArray(DownloadBean.class.getResourceAsStream(filename));
    return new DataBuffer(MIMETYPE_PDF, data);
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String aFilename) {
    filename = aFilename;
  }

  public class DataBuffer implements Serializable {

    private String contentType;
    private byte[] data;

    public DataBuffer(String aContentType, byte[] aData) {
      // TODO notNull validation
      contentType = aContentType;
      data = aData;
    }

    public String getContentType() {
      return contentType;
    }

    public byte[] getData() {
      return data;
    }
  }
}
