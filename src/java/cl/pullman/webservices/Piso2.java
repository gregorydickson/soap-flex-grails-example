//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.14 at 12:18:07 PM CDT 
//


package cl.pullman.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <p>Java class for piso2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="piso2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Disponibilidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TotalAsientoDisponible" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="Piso2") 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "piso2", propOrder = {
    "disponibilidad",
    "totalAsientoDisponible"
})
public class Piso2 {

    @XmlElement(name = "Disponibilidad")
    protected String disponibilidad;
    @XmlElement(name = "TotalAsientoDisponible")
    protected int totalAsientoDisponible;

    /**
     * Gets the value of the disponibilidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisponibilidad() {
        return disponibilidad;
    }

    /**
     * Sets the value of the disponibilidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisponibilidad(String value) {
        this.disponibilidad = value;
    }

    /**
     * Gets the value of the totalAsientoDisponible property.
     * 
     */
    public int getTotalAsientoDisponible() {
        return totalAsientoDisponible;
    }

    /**
     * Sets the value of the totalAsientoDisponible property.
     * 
     */
    public void setTotalAsientoDisponible(int value) {
        this.totalAsientoDisponible = value;
    }

}
