package com.register.example.soap.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "HeaderWS")
@XmlType(name = "HeaderWS")
@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class HeaderWS implements Serializable {
    @XmlAttribute(name = "messageId", required = true)
    private String messageId;

    @XmlAttribute(name = "source", required = true)
    private String source;

    @XmlElement(name = "dateTime")
    private Date dateTime;

}
