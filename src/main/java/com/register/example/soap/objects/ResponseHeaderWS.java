package com.register.example.soap.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "ResponseHeaderWS")
@XmlType(name = "ResponseHeaderWS")
@Getter
@Setter
@NoArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseHeaderWS extends HeaderWS implements Serializable {
    @XmlElement(name = "StatusWS", required = true)
    private StatusWS statusWS;

    @XmlAttribute(name = "isFailed")
    private Boolean isFailed;

    @XmlAttribute(name = "errorMsg")
    private String errorMsg;

}
