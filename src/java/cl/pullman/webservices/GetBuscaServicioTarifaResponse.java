//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.06 at 05:18:24 PM CST 
//


package cl.pullman.webservices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <p>Java class for GetBuscaServicioTarifaResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetBuscaServicioTarifaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DetalleServicioTarifa" type="{http://webservices.pullman.cl/}salidaServicio" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="GetBuscaServicioTarifaResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBuscaServicioTarifaResponse", propOrder = {
    "detalleServicioTarifa"
})
public class GetBuscaServicioTarifaResponse {

    @XmlElement(name = "DetalleServicioTarifa")
    protected List<SalidaServicio> detalleServicioTarifa;

    /**
     * Gets the value of the detalleServicioTarifa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detalleServicioTarifa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetalleServicioTarifa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SalidaServicio }
     * 
     * 
     */
    public List<SalidaServicio> getDetalleServicioTarifa() {
        if (detalleServicioTarifa == null) {
            detalleServicioTarifa = new ArrayList<SalidaServicio>();
        }
        return this.detalleServicioTarifa;
    }

}