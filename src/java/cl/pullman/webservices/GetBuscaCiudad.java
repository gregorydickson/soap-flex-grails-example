//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.05 at 03:06:25 PM CST 
//


package cl.pullman.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <p>Java class for GetBuscaCiudad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetBuscaCiudad">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoComercio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PuntoComercio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdSession" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="GetBuscaCiudad")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBuscaCiudad", propOrder = {
    "codigoComercio",
    "puntoComercio",
    "idSession"
})
public class GetBuscaCiudad {

    @XmlElement(name = "CodigoComercio")
    protected String codigoComercio;
    @XmlElement(name = "PuntoComercio")
    protected String puntoComercio;
    @XmlElement(name = "IdSession")
    protected String idSession;

    /**
     * Gets the value of the codigoComercio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoComercio() {
        return codigoComercio;
    }

    /**
     * Sets the value of the codigoComercio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoComercio(String value) {
        this.codigoComercio = value;
    }

    /**
     * Gets the value of the puntoComercio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuntoComercio() {
        return puntoComercio;
    }

    /**
     * Sets the value of the puntoComercio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuntoComercio(String value) {
        this.puntoComercio = value;
    }

    /**
     * Gets the value of the idSession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSession() {
        return idSession;
    }

    /**
     * Sets the value of the idSession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSession(String value) {
        this.idSession = value;
    }

}
