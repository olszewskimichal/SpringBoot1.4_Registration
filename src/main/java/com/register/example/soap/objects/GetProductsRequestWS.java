package com.register.example.soap.objects;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "GetProductsRequestWS")
@XmlType(name = "GetProductsRequestWS")
@Getter
@Setter
public class GetProductsRequestWS extends RequestWS {
    @XmlAttribute(name = "productLimit")
    private Integer productLimit;
}
