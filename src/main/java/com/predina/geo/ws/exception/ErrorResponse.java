package com.predina.geo.ws.exception;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "error")
public class ErrorResponse implements Serializable{


    private String message;


    private List<String> details;

    public ErrorResponse(final String message, final List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }


}
