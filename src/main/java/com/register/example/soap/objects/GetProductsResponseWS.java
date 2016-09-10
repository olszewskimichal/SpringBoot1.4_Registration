package com.register.example.soap.objects;

import com.register.example.dto.ProductDTO;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "GetProductsResponseWS")
@XmlType(name = "GetProductsResponseWS")
@Getter
@Setter
public class GetProductsResponseWS extends ResponseWS {
    @XmlElement(name = "products")
    private List<ProductDTO> products;
}
