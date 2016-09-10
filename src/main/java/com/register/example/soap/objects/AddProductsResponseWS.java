package com.register.example.soap.objects;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "AddProductsResponseWS")
@XmlType(name = "AddProductsResponseWS")
@Setter
public class AddProductsResponseWS extends ResponseWS {

    @XmlElement(name = "productsId")
    private List<Long> productsId;

    public List<Long> getProductsId() {
        if (productsId == null) productsId = new ArrayList<>();
        return productsId;
    }
}
