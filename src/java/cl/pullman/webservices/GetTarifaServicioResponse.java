//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.29 at 02:50:53 PM CDT 
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
 * <p>Java class for GetTarifaServicioResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetTarifaServicioResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DetalleTarifa" type="{http://webservices.pullman.cl/}tarifaServicio" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="GetTarifaServicioResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetTarifaServicioResponse", propOrder = {
    "detalleTarifa"
})
public class GetTarifaServicioResponse {

    @XmlElement(name = "DetalleTarifa")
    protected List<TarifaServicio> detalleTarifa;

    /**
     * Gets the value of the detalleTarifa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detalleTarifa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetalleTarifa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TarifaServicio }
     * 
     * 
     */
    public List<TarifaServicio> getDetalleTarifa() {
        if (detalleTarifa == null) {
            detalleTarifa = new ArrayList<TarifaServicio>();
        }
        return this.detalleTarifa;
    }

}