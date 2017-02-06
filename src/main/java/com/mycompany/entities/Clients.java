/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mitriy
 */
@Entity
@Table(name = "CLIENTS", catalog = "BOOKSTORE", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clients.findAll", query = "SELECT c FROM Clients c"),
    @NamedQuery(name = "Clients.findByClientnum", query = "SELECT c FROM Clients c WHERE c.clientnum = :clientnum"),
    @NamedQuery(name = "Clients.findByEmail", query = "SELECT c FROM Clients c WHERE c.email = :email"),
    @NamedQuery(name = "Clients.findByTitle", query = "SELECT c FROM Clients c WHERE c.title = :title"),
    @NamedQuery(name = "Clients.findByLastname", query = "SELECT c FROM Clients c WHERE c.lastname = :lastname"),
    @NamedQuery(name = "Clients.findByFirstname", query = "SELECT c FROM Clients c WHERE c.firstname = :firstname"),
    @NamedQuery(name = "Clients.findByCompanyname", query = "SELECT c FROM Clients c WHERE c.companyname = :companyname"),
    @NamedQuery(name = "Clients.findByAddress1", query = "SELECT c FROM Clients c WHERE c.address1 = :address1"),
    @NamedQuery(name = "Clients.findByAddress2", query = "SELECT c FROM Clients c WHERE c.address2 = :address2"),
    @NamedQuery(name = "Clients.findByCity", query = "SELECT c FROM Clients c WHERE c.city = :city"),
    @NamedQuery(name = "Clients.findByProvince", query = "SELECT c FROM Clients c WHERE c.province = :province"),
    @NamedQuery(name = "Clients.findByCountry", query = "SELECT c FROM Clients c WHERE c.country = :country"),
    @NamedQuery(name = "Clients.findByPostalcode", query = "SELECT c FROM Clients c WHERE c.postalcode = :postalcode"),
    @NamedQuery(name = "Clients.findByTelhome", query = "SELECT c FROM Clients c WHERE c.telhome = :telhome"),
    @NamedQuery(name = "Clients.findByTelcel", query = "SELECT c FROM Clients c WHERE c.telcel = :telcel")})
public class Clients implements Serializable {
    @Size(max = 32)
    @Column(name = "PASSWORD")
    private String password;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CLIENTNUM")
    private Integer clientnum;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 10)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 50)
    @Column(name = "LASTNAME")
    private String lastname;
    @Size(max = 50)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 50)
    @Column(name = "COMPANYNAME")
    private String companyname;
    @Size(max = 50)
    @Column(name = "ADDRESS1")
    private String address1;
    @Size(max = 50)
    @Column(name = "ADDRESS2")
    private String address2;
    @Size(max = 50)
    @Column(name = "CITY")
    private String city;
    @Size(max = 2)
    @Column(name = "PROVINCE")
    private String province;
    @Size(max = 3)
    @Column(name = "COUNTRY")
    private String country;
    @Size(max = 7)
    @Column(name = "POSTALCODE")
    private String postalcode;
    @Size(max = 20)
    @Column(name = "TELHOME")
    private String telhome;
    @Size(max = 20)
    @Column(name = "TELCEL")
    private String telcel;
    @OneToMany(mappedBy = "clientnum")
    private Collection<Reviews> reviewsCollection;
    @OneToMany(mappedBy = "clientnum")
    private Collection<Invoices> invoicesCollection;

    public Clients() {
    }

    public Clients(Integer clientnum) {
        this.clientnum = clientnum;
    }

    public Integer getClientnum() {
        return clientnum;
    }

    public void setClientnum(Integer clientnum) {
        this.clientnum = clientnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getTelhome() {
        return telhome;
    }

    public void setTelhome(String telhome) {
        this.telhome = telhome;
    }

    public String getTelcel() {
        return telcel;
    }

    public void setTelcel(String telcel) {
        this.telcel = telcel;
    }

    @XmlTransient
    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    @XmlTransient
    public Collection<Invoices> getInvoicesCollection() {
        return invoicesCollection;
    }

    public void setInvoicesCollection(Collection<Invoices> invoicesCollection) {
        this.invoicesCollection = invoicesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientnum != null ? clientnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clients)) {
            return false;
        }
        Clients other = (Clients) object;
        if ((this.clientnum == null && other.clientnum != null) || (this.clientnum != null && !this.clientnum.equals(other.clientnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.Clients[ clientnum=" + clientnum + " ]";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
