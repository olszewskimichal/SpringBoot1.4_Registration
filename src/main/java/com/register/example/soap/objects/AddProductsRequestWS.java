package com.register.example.soap.objects;

import com.register.example.dto.ProductDTO;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "AddProductsRequestWS")
@XmlType(name = "AddProductsRequestWS")
@Getter
@Setter
public class AddProductsRequestWS extends RequestWS {

    @XmlElement(name = "products")
    private List<ProductDTO> products;
}
