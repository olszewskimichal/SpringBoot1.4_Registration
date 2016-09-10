package com.register.example.soap.objects;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "ResponseWS")
@XmlType(name = "ResponseWS")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseWS implements Serializable {
    @XmlElement(name = "ResponseHeaderWS", required = true)
    protected ResponseHeaderWS headerWS;
}
