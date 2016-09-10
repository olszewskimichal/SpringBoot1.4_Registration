package com.register.example.soap.objects;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "RequestWS")
@XmlType(name = "RequestWS")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestWS implements Serializable {
    @XmlElement(name = "RequestHeaderWS", required = true)
    protected RequestHeaderWS headerWS;
}
