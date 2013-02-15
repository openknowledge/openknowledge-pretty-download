/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
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
