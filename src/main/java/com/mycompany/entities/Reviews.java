/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mitriy
 */
@Entity
@Table(name = "REVIEWS", catalog = "BOOKSTORE", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reviews.findAll", query = "SELECT r FROM Reviews r"),
    @NamedQuery(name = "Reviews.findById", query = "SELECT r FROM Reviews r WHERE r.id = :id"),
    @NamedQuery(name = "Reviews.findByDateofreview", query = "SELECT r FROM Reviews r WHERE r.dateofreview = :dateofreview"),
    @NamedQuery(name = "Reviews.findByRating", query = "SELECT r FROM Reviews r WHERE r.rating = :rating"),
    @NamedQuery(name = "Reviews.findByApprovalstatus", query = "SELECT r FROM Reviews r WHERE r.approvalstatus = :approvalstatus")})
public class Reviews implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATEOFREVIEW")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofreview;
    @Column(name = "RATING")
    private Integer rating;
    @Lob
    @Size(max = 65535)
    @Column(name = "REVIEWTEXT")
    private String reviewtext;
    @Column(name = "APPROVALSTATUS")
    private Boolean approvalstatus;
    @JoinColumn(name = "CLIENTNUM", referencedColumnName = "CLIENTNUM")
    @ManyToOne
    private Clients clientnum;
    @JoinColumn(name = "ISBN", referencedColumnName = "ISBN")
    @ManyToOne
    private Inventory isbn;

    public Reviews() {
    }

    public Reviews(Integer id) {
        this.id = id;
    }

    public Reviews(Integer id, Date dateofreview) {
        this.id = id;
        this.dateofreview = dateofreview;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateofreview() {
        return dateofreview;
    }

    public void setDateofreview(Date dateofreview) {
        this.dateofreview = dateofreview;
    }

    public Integer getRating() {
        return rating;  
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewtext() {
        return reviewtext;
    }

    public void setReviewtext(String reviewtext) {
        this.reviewtext = reviewtext;
    }

    public Boolean getApprovalstatus() {
        return approvalstatus;
    }

    public void setApprovalstatus(Boolean approvalstatus) {
        this.approvalstatus = approvalstatus;
    }

    public Clients getClientnum() {
        return clientnum;
    }

    public void setClientnum(Clients clientnum) {
        this.clientnum = clientnum;
    }

    public Inventory getIsbn() {
        return isbn;
    }

    public void setIsbn(Inventory isbn) {
        this.isbn = isbn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.Reviews[ id=" + id + " ]";
    }
    
}
