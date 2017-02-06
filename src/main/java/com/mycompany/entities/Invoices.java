/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mitriy
 */
@Entity
@Table(name = "INVOICES", catalog = "BOOKSTORE", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoices.findAll", query = "SELECT i FROM Invoices i"),
    @NamedQuery(name = "Invoices.findByInvoicenum", query = "SELECT i FROM Invoices i WHERE i.invoicenum = :invoicenum"),
    @NamedQuery(name = "Invoices.findByDateofsale", query = "SELECT i FROM Invoices i WHERE i.dateofsale = :dateofsale"),
    @NamedQuery(name = "Invoices.findByNetprice", query = "SELECT i FROM Invoices i WHERE i.netprice = :netprice"),
    @NamedQuery(name = "Invoices.findByGstrate", query = "SELECT i FROM Invoices i WHERE i.gstrate = :gstrate"),
    @NamedQuery(name = "Invoices.findByTotalprice", query = "SELECT i FROM Invoices i WHERE i.totalprice = :totalprice")})
public class Invoices implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INVOICENUM")
    private Integer invoicenum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATEOFSALE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofsale;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETPRICE")
    private BigDecimal netprice;
    @Column(name = "GSTRATE")
    private BigDecimal gstrate;
    @Column(name = "TOTALPRICE")
    private BigDecimal totalprice;
    @OneToMany(mappedBy = "invoicenum")
    private Collection<Details> detailsCollection;
    @JoinColumn(name = "CLIENTNUM", referencedColumnName = "CLIENTNUM")
    @ManyToOne
    private Clients clientnum;

    public Invoices() {
    }

    public Invoices(Integer invoicenum) {
        this.invoicenum = invoicenum;
    }

    public Invoices(Integer invoicenum, Date dateofsale) {
        this.invoicenum = invoicenum;
        this.dateofsale = dateofsale;
    }

    public Integer getInvoicenum() {
        return invoicenum;
    }

    public void setInvoicenum(Integer invoicenum) {
        this.invoicenum = invoicenum;
    }

    public Date getDateofsale() {
        return dateofsale;
    }

    public void setDateofsale(Date dateofsale) {
        this.dateofsale = dateofsale;
    }

    public BigDecimal getNetprice() {
        return netprice;
    }

    public void setNetprice(BigDecimal netprice) {
        this.netprice = netprice;
    }

    public BigDecimal getGstrate() {
        return gstrate;
    }

    public void setGstrate(BigDecimal gstrate) {
        this.gstrate = gstrate;
    }

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    @XmlTransient
    public Collection<Details> getDetailsCollection() {
        return detailsCollection;
    }

    public void setDetailsCollection(Collection<Details> detailsCollection) {
        this.detailsCollection = detailsCollection;
    }

    public Clients getClientnum() {
        return clientnum;
    }

    public void setClientnum(Clients clientnum) {
        this.clientnum = clientnum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoicenum != null ? invoicenum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoices)) {
            return false;
        }
        Invoices other = (Invoices) object;
        if ((this.invoicenum == null && other.invoicenum != null) || (this.invoicenum != null && !this.invoicenum.equals(other.invoicenum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.Invoices[ invoicenum=" + invoicenum + " ]";
    }
    
}
