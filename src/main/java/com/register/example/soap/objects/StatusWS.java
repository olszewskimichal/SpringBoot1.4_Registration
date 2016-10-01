package com.register.example.soap.objects;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "StatusWS")
@XmlType(name = "StatusWS")
public enum StatusWS {
    IN_PROGRESS,
    DONE,
    FAILED
}
